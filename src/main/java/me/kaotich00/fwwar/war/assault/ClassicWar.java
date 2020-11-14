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
import me.kaotich00.fwwar.services.SimpleArenaService;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.*;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;

public class ClassicWar extends AssaultWar {

    public ClassicWar() {
        this.setWarStatus(WarStatus.CREATED);
        this.nations = new ArrayList<>();
        this.players = new HashMap<>();
        this.deathQueue = new ArrayList<>();
        this.killCount = new HashMap<>();
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

            Iterator<Town> iterator = this.players.keySet().iterator();
            Nation firstNation = this.nations.get(0);
            Map<UUID, Location> playersToTeleport = new HashMap<>();

            while(iterator.hasNext()) {
                Town town = iterator.next();
                List<UUID> residents = this.players.get(town);

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
                        double progress = bossBar.getProgress() - 0.03 < 0.0 ? 0.0 : bossBar.getProgress() - 0.03;
                        bossBar.setProgress(progress);
                    });
            timer.scheduleTimer();

        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopWar() {
        Iterator<Town> iterator = this.players.keySet().iterator();

        while(iterator.hasNext()) {
            Town town = iterator.next();
            List<UUID> residents = this.players.get(town);

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
        addPlayerToDeathQueue(player);

        TownyAPI townyAPI = TownyAPI.getInstance();

        boolean shouldWarEnd = false;

        try {
            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town residentTown = resident.getTown();
            Nation residentNation = residentTown.getNation();

            removePlayerFromWar(residentTown, resident.getUUID());

            if(getParticipantsForTown(residentTown) == null) {
                Message.TOWN_DEFEATED.broadcast(residentTown.getName());
                setTownDefeated(residentNation, residentTown);
            }

            if(getTownsForNation(residentNation).size() == 0) {
                Message.NATION_DEFEATED.broadcast(residentNation.getName());
            }

            /* Check if the required amount of Nations is present */
            if(getParticipantsNations().size() < 2) {
                shouldWarEnd = true;
            } else {
                /* Check if at least 2 Nations are considered enemies between each other */
                boolean areThereEnemies = false;
                for(Nation n: getParticipantsNations()) {
                    for(Nation plausibleEnemy: getParticipantsNations()) {
                        if(n.hasEnemy(plausibleEnemy)) {
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
