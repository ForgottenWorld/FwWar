package me.kaotich00.fwwar.task;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.objects.war.OldWar;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarPlotConquestTask implements Runnable {

    private final Fwwar plugin;
    private OldWar oldWar;

    public WarPlotConquestTask(Fwwar plugin, OldWar oldWar) {
        this.plugin = plugin;
        this.oldWar = oldWar;
    }

    @Override
    public void run() {

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        SimplePlotService plotService = SimplePlotService.getInstance();
        TownyAPI townyAPI = TownyAPI.getInstance();

        boolean shouldWarEnd = false;

        for(Nation nation: oldWar.getParticipantNations()) {
            for(Town town: oldWar.getParticipantTownsForNation(nation)) {
                CorePlot corePlot = plotService.getCorePlotOfTown(town.getUuid()).get();
                Chunk corePlotChunk = Bukkit.getServer().getWorld(corePlot.getWorldUUID()).getChunkAt(corePlot.getChunkKey());

                Entity[] entities = corePlotChunk.getEntities();
                List<Player> playerList = new ArrayList<>();

                for(Entity entity: entities) {
                    if(entity instanceof Player) {
                        playerList.add((Player) entity);
                    }
                }

                int allyCount = 0;
                int enemyCount = 0;

                for(Player player: playerList) {
                    try {
                        Resident resident = townyAPI.getDataSource().getResident(player.getName());
                        Town residentTown = resident.getTown();

                        if(residentTown.equals(town)) {
                            allyCount++;
                            continue;
                        }

                        if(residentTown.isAlliedWith(town)) {
                            allyCount++;
                            continue;
                        }

                        if(residentTown.getNation().hasEnemy(town.getNation())) {
                            enemyCount++;
                        }
                    } catch (NotRegisteredException e) {
                        continue;
                    }
                }

                if(allyCount < enemyCount) {
                    int basePercentage = defaultConfig.getInt("war.conquest_base_percentage");

                    boolean shouldMultiply = defaultConfig.getBoolean("war.conquest_multiply");

                    int damage;
                    if(shouldMultiply) {
                        int maxMultiplier = defaultConfig.getInt("war.conquest_max_multiplier");
                        damage = basePercentage * (((enemyCount - allyCount) <= maxMultiplier) ? (enemyCount - allyCount) : maxMultiplier);
                    } else {
                        damage = basePercentage;
                    }


                    corePlot.setConquestPercentage(corePlot.getConquestPercentage() + damage);

                    int townHP = defaultConfig.getInt("war.town_max_hp");
                    Message.TOWN_CONQUER_STATUS.broadcast(town.getName(), townHP - corePlot.getConquestPercentage());

                    if(corePlot.getConquestPercentage() >= townHP) {
                        oldWar.setTownDefeated(nation, town);
                    }

                    if(!oldWar.getParticipantNations().contains(nation)) {
                        Message.NATION_DEFEATED.broadcast(nation.getName());
                    }

                    /* Check if the required amount of Nations is present */
                    if(oldWar.getParticipantNations().size() < 2) {
                        shouldWarEnd = true;
                        break;
                    } else {
                        /* Check if at least 2 Nations are considered enemies between each other */
                        boolean areThereEnemies = false;
                        for(Nation n: oldWar.getParticipantNations()) {
                            for(Nation plausibleEnemy: oldWar.getParticipantNations()) {
                                if(n.hasEnemy(plausibleEnemy)) {
                                    areThereEnemies = true;
                                }
                            }
                        }

                        if(!areThereEnemies) {
                            shouldWarEnd = true;
                            break;
                        }
                    }
                }

            }
        }

        if(shouldWarEnd) {
            SimpleWarService.getInstance().stopWar();
        } else {
            //SimpleScoreboardService.getInstance().updateWarScoreBoard();
        }

    }

}
