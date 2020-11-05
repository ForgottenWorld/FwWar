package me.kaotich00.fwwar.services;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.war.OldWar;
import me.kaotich00.fwwar.task.WarPlotConquestTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class SimpleWarService {

    private static SimpleWarService instance;
    private int warTaskId;

    private War currentWar;

    private SimpleWarService() {
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.currentWar = null;
    }

    public static SimpleWarService getInstance() {
        if(instance == null) {
            instance = new SimpleWarService();
        }
        return instance;
    }

    /**
     * Get the current running war if present
     * @return Optional<War>
     */
    public Optional<War> getCurrentWar() {
        return Optional.ofNullable(this.currentWar);
    }

    /**
     * Set the current war
     * @param war
     */
    public void setCurrentWar(War war) {
        this.currentWar = war;
    }

    /**
     * Start war if all conditions are met
     * @param sender
     */
    /*public void startWar(CommandSender sender) {
        War war = this.currentWar;

        TownyAPI townyAPI = TownyAPI.getInstance();

        for(Nation nation: townyAPI.getDataSource().getNations()) {
            if(!nation.isNeutral()) {
                boolean canJoin = war.addParticipant(nation);
                if(canJoin) {
                    Message.NATION_JOIN_WAR.broadcast(nation.getName());
                } else {
                    Message.NATION_CANNOT_JOIN_WAR.broadcast(nation.getName());
                }
            }
        }

        // Check if the required amount of Nations is present
        if(war.getParticipantNations().size() < 2) {
            Message.NOT_ENOUGH_NATIONS.broadcast();
            return;
        }

        // Check if at least 2 Nations are considered enemies between each other
        boolean areThereEnemies = false;
        for(Nation nation: war.getParticipantNations()) {
            for(Nation plausibleEnemy: war.getParticipantNations()) {
                if(nation.hasEnemy(plausibleEnemy)) {
                    areThereEnemies = true;
                }
            }
        }

        if(!areThereEnemies) {
            Message.NO_ENEMY_NATION.broadcast();
            return;
        }

        Message.WAR_STARTED.broadcast();
        Bukkit.getServer().broadcastMessage(oldWar.getPrintableParticipants());

        this.currentWar = oldWar;
        SimpleScoreboardService.getInstance().initWarScoreboard();

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        Long seconds = defaultConfig.getLong("war.plot_check_time") * 20;

        warTaskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Fwwar.getPlugin(Fwwar.class), new WarPlotConquestTask(Fwwar.getPlugin(Fwwar.class), oldWar), seconds, seconds);

    }*/

    /**
     * Stop the current running war
     */
    public void stopWar() {
        Message.WAR_ENDED.broadcast();
        //SimpleScoreboardService.getInstance().destroyWarScoreboard();
        Bukkit.getScheduler().cancelTask(SimpleWarService.getInstance().getWarTaskId());
    }

    public int getWarTaskId() {
        return this.warTaskId;
    }

}
