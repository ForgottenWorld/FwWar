package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.api.services.WarService;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.assault.SiegeWar;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleWarService implements WarService {

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
    public Optional<War> getWar() {
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
     * Stop the current running war
     */
    public void stopWar() {
        Message.WAR_ENDED.broadcast();
        currentWar.stopWar();
        SimpleScoreboardService.getInstance().removeScoreboards();

        if(currentWar instanceof SiegeWar) {
            Bukkit.getScheduler().cancelTask(SimpleWarService.getInstance().getWarTaskId());
        }
    }

    public void deleteWar() {
        this.currentWar = null;
    }

    public int getWarTaskId() {
        return this.warTaskId;
    }

    public void setWarTaskId(int warTaskId) {
        this.warTaskId = warTaskId;
    }

}
