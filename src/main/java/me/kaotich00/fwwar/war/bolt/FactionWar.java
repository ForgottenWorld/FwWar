package me.kaotich00.fwwar.war.bolt;

import com.destroystokyo.paper.Title;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.objects.war.ParticipantTown;
import me.kaotich00.fwwar.services.SimpleArenaService;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FactionWar extends BoltWar {

    private final Map<UUID, Kit> playerKits;

    public FactionWar() {
        this.setWarStatus(WarStatus.CREATED);
        this.playerKits = new HashMap<>();
    }

    @Override
    public String getDescription() {
        return "The faction kit war consists of two nations facing each other in a merciless combat! Pick a class and kill every opponent.";
    }

    @Override
    public WarTypes getWarType() {
        return WarTypes.BOLT_WAR_FACTION;
    }

    @Override
    public void startWar() {
        try {
            Random random = new Random();
            SimpleArenaService arenaService = SimpleArenaService.getInstance();
            Arena warArena = arenaService.getArenas().get(arenaService.getArenas().size() > 1 ? random.nextInt(arenaService.getArenas().size() - 1) : 0);

            List<UUID> playerWithNoSelectedKit = checkKits();
            if(playerWithNoSelectedKit.size() > 0) {
                Message.WAR_CANNOT_START_KIT_REQUIRED.broadcast();
                for(UUID uuid: playerWithNoSelectedKit) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player != null) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + ">> " + ChatColor.GOLD + player.getName());
                    }
                }
                return;
            }

            Nation firstNation = null;
            Map<UUID, Location> playersToTeleport = new HashMap<>();

            for(ParticipantNation participantNation: this.getParticipants()) {

                if (firstNation == null)
                    firstNation = participantNation.getNation();

                for (ParticipantTown participantTown : participantNation.getTowns()) {
                    Set<UUID> residents = participantTown.getPlayers();
                    Town town = participantTown.getTown();

                    residents.removeIf(resident -> Bukkit.getPlayer(resident) == null);

                    for(UUID uuid: residents) {
                        Player player = Bukkit.getPlayer(uuid);
                        Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());

                        if(player != null) {
                            player.getInventory().clear();

                            Kit kit = this.playerKits.get(uuid);
                            for(ItemStack item: kit.getItemsList()) {
                                player.getInventory().addItem(item);
                            }

                            Location playerSpawn = firstNation.equals(resident.getTown().getNation()) ? warArena.getLocation(LocationType.FIRST_NATION_SPAWN_POINT) : warArena.getLocation(LocationType.SECOND_NATION_SPAWN_POINT);
                            Location playerBattle = firstNation.equals(resident.getTown().getNation()) ? warArena.getLocation(LocationType.FIRST_NATION_BATTLE_POINT) : warArena.getLocation(LocationType.SECOND_NATION_BATTLE_POINT);

                            player.teleport(playerSpawn);
                            playersToTeleport.put(player.getUniqueId(), playerBattle);

                            Message.WAR_WILL_BEGAN.send(player, "30");
                        }
                    }
                }
            }

            setWarStatus(WarStatus.STARTED);

            String bossBarName = "fwwar.startwar";
            BossBar bossBar = Bukkit.getServer().createBossBar(
                    NamespacedKey.minecraft(bossBarName),
                    ChatColor.GREEN + "The war will began in 30 seconds",
                    BarColor.GREEN,
                    BarStyle.SEGMENTED_10
            );
            bossBar.setProgress(1.0);

            FwWarTimer timer = new FwWarTimer(Fwwar.getPlugin(Fwwar.class),
                    30,
                    () -> {
                        for(Map.Entry<UUID, Location> entry: playersToTeleport.entrySet()) {
                            UUID playerUUID = entry.getKey();
                            Player player = Bukkit.getPlayer(playerUUID);
                            if(player != null) {
                                bossBar.addPlayer(player);
                            }
                        }
                    },
                    () -> {
                        Message.WAR_STARTED.broadcast();
                        SimpleScoreboardService.getInstance().initScoreboards();

                        for(Map.Entry<UUID, Location> entry: playersToTeleport.entrySet()) {
                            UUID playerUUID = entry.getKey();
                            Location location = entry.getValue();

                            Player player = Bukkit.getPlayer(playerUUID);
                            if(player != null) {
                                bossBar.removePlayer(player);
                                player.teleport(location);
                            }
                        }
                    },
                    (t) -> {
                        if( t.getSecondsLeft() <= 5) {
                            for(Map.Entry<UUID, Location> entry: playersToTeleport.entrySet()) {
                                UUID playerUUID = entry.getKey();

                                Player player = Bukkit.getPlayer(playerUUID);
                                if(player != null) {
                                    player.sendTitle(new Title(MessageUtils.formatSuccessMessage(String.valueOf(t.getSecondsLeft())), "", 1, 18, 1));
                                }
                            }
                        }

                        bossBar.setTitle(MessageUtils.formatSuccessMessage("The match will began in " + t.getSecondsLeft() + " seconds"));
                        double progress = Math.max(bossBar.getProgress() - 0.03, 0.0);
                        bossBar.setProgress(progress);
                    });
            timer.scheduleTimer();

        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopWar() {
        for(ParticipantNation participantNation: this.getParticipants()) {
            for(ParticipantTown participantTown: participantNation.getTowns()) {
                Set<UUID> residents = participantTown.getPlayers();
                Town town = participantTown.getTown();

                for(UUID uuid: residents) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player != null) {
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);

                        try {
                            player.teleport(town.getSpawn());
                        } catch (TownyException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        setWarStatus(WarStatus.ENDED);
    }

    private List<UUID> checkKits() {
        List<UUID> playerWithNoKits = new ArrayList<>();

        for(ParticipantNation participantNation: this.getParticipants()) {
            for (ParticipantTown participantTown : participantNation.getTowns()) {
                Set<UUID> residents = participantTown.getPlayers();
                Town town = participantTown.getTown();

                for(UUID uuid: residents) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player != null && !getPlayerKit(player).isPresent()) {
                        playerWithNoKits.add(uuid);
                    }
                }
            }
        }

        return playerWithNoKits;
    }

    @Override
    public boolean supportKits() {
        return true;
    }

    @Override
    public void setPlayerKit(Player player, Kit kit) {
        this.playerKits.put(player.getUniqueId(), kit);
    }

    @Override
    public Optional<Kit> getPlayerKit(Player player) {
        return Optional.ofNullable(this.playerKits.get(player.getUniqueId()));
    }

    @Override
    public void handlePlayerDeath(Player player) {

        getDeathQueue().addPlayer(player);

        try {
            TownyAPI townyAPI = TownyAPI.getInstance();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town town = resident.getTown();

            Set<UUID> participants = null;
            if(hasTown(town))
                participants = getParticipant(town.getNation().getUuid()).getTown(town.getUuid()).getPlayers();

            if(participants == null) {
                return;
            }

            if (!participants.contains(player.getUniqueId())) {
                return;
            }

            getParticipant(town.getNation().getUuid()).getTown(town.getUuid()).removePlayer(player.getUniqueId());
            Message.WAR_PLAYER_DEFEATED.send(player);

            boolean shouldRemoveNation = true;
            if(getParticipant(town.getNation().getUuid()).getTowns().size() > 0){
                shouldRemoveNation = false;
            }

            if(shouldRemoveNation) {
                removeParticipant(town.getNation());
                Message.NATION_DEFEATED.broadcast(town.getNation().getName());
            }

            if(getParticipants().size() < 2) {
                SimpleScoreboardService.getInstance().removeScoreboards();
                SimpleWarService.getInstance().stopWar();
            }

        } catch (TownyException ignored) {
        }
    }

}
