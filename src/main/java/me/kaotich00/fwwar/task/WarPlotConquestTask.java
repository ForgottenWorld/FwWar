package me.kaotich00.fwwar.task;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.objects.war.War;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WarPlotConquestTask implements Runnable {

    private final Fwwar plugin;
    private War war;

    public WarPlotConquestTask(Fwwar plugin, War war) {
        this.plugin = plugin;
        this.war = war;
    }

    @Override
    public void run() {

        SimplePlotService plotService = SimplePlotService.getInstance();
        TownyAPI townyAPI = TownyAPI.getInstance();

        for(Nation nation: war.getParticipantNations()) {
            for(Town town: war.getParticipantTownsForNation(nation)) {
                CorePlot corePlot = plotService.getCorePlotOfTown(town.getUuid()).get();
                Chunk corePlotChunk = Bukkit.getServer().getWorld(corePlot.getWorldUUID()).getChunkAt(corePlot.getChunkKey());

                Entity[] entities = corePlotChunk.getEntities();
                List<Player> playerList = new ArrayList<>();

                for(Entity entity: entities) {
                    if(entity instanceof Player) {
                        playerList.add((Player) entity);
                    }
                }

                for(Player player: playerList) {
                    Bukkit.getConsoleSender().sendMessage("Player è entrato nel plot: " + player.getName());
                    try {
                        Resident resident = townyAPI.getDataSource().getResident(player.getName());
                        Town residentTown = resident.getTown();

                        if(residentTown.equals(town)) {
                            Bukkit.getConsoleSender().sendMessage("Le città sono le stesse");
                            continue;
                        }

                        if(residentTown.isAlliedWith(town)) {
                            Bukkit.getConsoleSender().sendMessage("Le città sono alleate");
                            continue;
                        }

                        if(residentTown.getNation().hasEnemy(town.getNation())) {
                            Bukkit.getConsoleSender().sendMessage("Le città sono nemiche");
                            corePlot.setConquestPercentage(corePlot.getConquestPercentage() + 1);
                            player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
                            Message.TOWN_CONQUER_STATUS.broadcast(town.getName(), 100 - corePlot.getConquestPercentage());
                        }

                        if(corePlot.getConquestPercentage() == 100) {
                            war.setTownDefeated(nation, town);
                        }

                        if(!war.getParticipantNations().contains(nation)) {
                            Message.NATION_DEFEATED.broadcast(nation.getName());
                        }

                        /* Check if the required amount of Nations is present */
                        if(war.getParticipantNations().size() < 2) {
                            Message.WAR_ENDED.broadcast();
                            Bukkit.getScheduler().cancelTask(SimpleWarService.getInstance().getWarTaskId());
                        } else {
                            /* Check if at least 2 Nations are considered enemies between each other */
                            boolean areThereEnemies = false;
                            for(Nation n: war.getParticipantNations()) {
                                for(Nation plausibleEnemy: war.getParticipantNations()) {
                                    if(n.hasEnemy(plausibleEnemy)) {
                                        areThereEnemies = true;
                                    }
                                }
                            }

                            if(!areThereEnemies) {
                                Message.WAR_ENDED.broadcast();
                                Bukkit.getScheduler().cancelTask(SimpleWarService.getInstance().getWarTaskId());
                            }
                        }
                    } catch (NotRegisteredException e) {
                        continue;
                    }
                }
            }
        }

    }

}
