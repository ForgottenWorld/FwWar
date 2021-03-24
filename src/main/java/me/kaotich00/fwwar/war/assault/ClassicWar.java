package me.kaotich00.fwwar.war.assault;

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

import java.util.*;

public class ClassicWar extends AssaultWar {

    public ClassicWar() {
        this.setWarStatus(WarStatus.CREATED);
        this.townsForNation = new HashMap<>();
    }

    @Override
    public String getDescription() {
        return "Classic war, wins the nation who kills everyone in the enemy nation or the most players if the time runs out";
    }

    @Override
    public WarTypes getWarType() {
        return WarTypes.ASSAULT_WAR_CLASSIC;
    }

    @Override
    public void startWar() {
        try {
            Random random = new Random();
            SimpleArenaService arenaService = SimpleArenaService.getInstance();
            Arena warArena = arenaService.getArenas().get(arenaService.getArenas().size() > 1 ? random.nextInt(arenaService.getArenas().size() - 1) : 0);

            setWarStatus(WarStatus.STARTED);

            Map<UUID, Location> playersToTeleport = new HashMap<>();
            Nation firstNation = null;
            for(ParticipantNation participantNation: this.getParticipants()) {

                if(firstNation == null)
                    firstNation = participantNation.getNation();

                for(ParticipantTown participantTown: participantNation.getTowns()) {
                    Set<UUID> residents = participantTown.getPlayers();
                    residents.removeIf(resident -> Bukkit.getPlayer(resident) == null);

                    for(UUID uuid: residents) {
                        Player player = Bukkit.getPlayer(uuid);
                        Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());

                        if(player != null) {
                            Location playerSpawn = firstNation.equals(resident.getTown().getNation()) ? warArena.getLocation(LocationType.FIRST_NATION_SPAWN_POINT) : warArena.getLocation(LocationType.SECOND_NATION_SPAWN_POINT);
                            Location playerBattle = firstNation.equals(resident.getTown().getNation()) ? warArena.getLocation(LocationType.FIRST_NATION_BATTLE_POINT) : warArena.getLocation(LocationType.SECOND_NATION_BATTLE_POINT);

                            player.teleport(playerSpawn);
                            playersToTeleport.put(player.getUniqueId(), playerBattle);

                            Message.WAR_WILL_BEGAN.send(player, "30");
                        }
                    }
                }
            }

            String bossBarName = "fwwar.startwar";
            BossBar bossBar = Bukkit.getServer().createBossBar(
                    NamespacedKey.minecraft(bossBarName),
                    Message.WAR_WILL_BEGAN.asString("30"),
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

                        bossBar.setTitle(Message.WAR_WILL_BEGAN.asString(t.getSecondsLeft()));
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

    @Override
    public boolean supportKits() {
        return false;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return false;
    }

    @Override
    public int getMaxAllowedParticipants() {
        return 0;
    }

    @Override
    public void handlePlayerDeath(Player player) {
        getDeathQueue().addPlayer(player);

        TownyAPI townyAPI = TownyAPI.getInstance();

        boolean shouldWarEnd = false;

        try {
            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town residentTown = resident.getTown();
            Nation residentNation = residentTown.getNation();

            if(hasTown(residentTown))
                getParticipant(residentNation.getUuid()).getTown(residentTown.getUuid()).removePlayer(resident.getUUID());

            if(getParticipant(residentNation.getUuid()).getTown(residentTown.getUuid()).getPlayers().size() == 0) {
                Message.TOWN_DEFEATED.broadcast(residentTown.getName());
                setTownDefeated(residentNation, residentTown);
            }

            if(getTownsForNation(residentNation).size() == 0) {
                Message.NATION_DEFEATED.broadcast(residentNation.getName());
            }

            /* Check if the required amount of Nations is present */
            if(getParticipants().size() < 2) {
                shouldWarEnd = true;
            } else {
                /* Check if at least 2 Nations are considered enemies between each other */
                boolean areThereEnemies = false;
                for(ParticipantNation n: getParticipants()) {
                    for(ParticipantNation plausibleEnemy: getParticipants()) {
                        if(n.getNation().hasEnemy(plausibleEnemy.getNation())) {
                            areThereEnemies = true;
                        }
                    }
                }

                if(!areThereEnemies) {
                    shouldWarEnd = true;
                }
            }

            if(shouldWarEnd) {
                SimpleWarService.getInstance().stopWar();
            }
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }

    }

}
