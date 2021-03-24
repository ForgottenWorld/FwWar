package me.kaotich00.fwwar.task;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.objects.war.ParticipantTown;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.war.assault.AssaultWar;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WarPlotConquestTask implements Runnable {

    private final Fwwar plugin;
    private final AssaultWar war;

    public WarPlotConquestTask(Fwwar plugin, War war) {
        this.plugin = plugin;
        this.war = (AssaultWar) war;
    }

    @Override
    public void run() {

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        SimplePlotService plotService = SimplePlotService.getInstance();
        TownyAPI townyAPI = TownyAPI.getInstance();

        boolean shouldWarEnd = false;

        for(ParticipantNation participantNation: war.getParticipants()) {
            Iterator<ParticipantTown> iterator = participantNation.getTowns().iterator();
            while(iterator.hasNext()) {
                ParticipantTown participantTown = iterator.next();
                Town town = participantTown.getTown();

                Nation nation = null;
                try {
                    nation = town.getNation();
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }

                if(nation == null) {
                    continue;
                }

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
                    //Message.TOWN_CONQUER_STATUS.broadcast(town.getName(), townHP - corePlot.getConquestPercentage());

                    if(corePlot.getConquestPercentage() >= townHP) {
                        try {
                            war.setTownDefeated(town.getNation(), town);
                            war.getParticipant(nation.getUuid()).removeTown(town.getUuid());
                            Message.TOWN_DEFEATED_SIEGE_WAR.broadcast(town.getName());
                        } catch (NotRegisteredException e) {
                            e.printStackTrace();
                        }
                    }

                    if(war.getTownsForNation(nation).size() == 0) {
                        Message.NATION_DEFEATED.broadcast(nation.getName());
                        war.removeParticipant(nation);
                    }

                    /* Check if the required amount of Nations is present */
                    if(war.getParticipants().size() < 2) {
                        shouldWarEnd = true;
                        break;
                    } else {
                        /* Check if at least 2 Nations are considered enemies between each other */
                        boolean areThereEnemies = false;
                        for(ParticipantNation n: war.getParticipants()) {
                            for(ParticipantNation plausibleEnemy: war.getParticipants()) {
                                if(n.getNation().hasEnemy(plausibleEnemy.getNation())) {
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

            if(shouldWarEnd) {
                SimpleWarService.getInstance().stopWar();
            } else {
                SimpleScoreboardService.getInstance().updateScoreboards();
            }
        }
    }

}
