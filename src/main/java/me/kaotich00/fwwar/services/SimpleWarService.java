package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.api.services.WarService;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.assault.SiegeWar;
import org.bukkit.Bukkit;

import java.util.*;

public class SimpleWarService implements WarService {

    private static SimpleWarService instance;
    private int warTaskId;

    Map<WarTypes, Map<String, Kit>> kits;

    private War currentWar;

    private SimpleWarService() {
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.currentWar = null;
        this.kits = new HashMap<>();
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

    public void addKit(WarTypes warType, Kit kit){
        if(!this.kits.containsKey(warType)) {
            this.kits.put(warType, new HashMap<>());
        }
        this.kits.get(warType).put(kit.getName(), kit);
    }

    public void removeKit(WarTypes warType, String kitName) {
        this.kits.get(warType).remove(kitName);
    }

    public void updateKit(WarTypes warType, String kitName, Kit kit) {
        this.kits.get(warType).put(kitName, kit);
    }

    public Collection<Kit> getKitsForType(WarTypes warType) {
        return this.kits.get(warType).values();
    }

    public Optional<Kit> getKitForName(WarTypes warType, String name) {
        if(!this.kits.containsKey(warType)) {
            this.kits.put(warType, new HashMap<>());
        }
        return Optional.ofNullable(this.kits.get(warType).get(name));
    }

    @Override
    public Map<Kit, WarTypes> getAllKits() {
        Map<Kit, WarTypes> kits = new HashMap<>();
        for(Map.Entry<WarTypes, Map<String,Kit>> entry : this.kits.entrySet()) {
            for(Map.Entry<String,Kit> entry2 : entry.getValue().entrySet()) {
                kits.put(entry2.getValue(), entry.getKey());
            }
        }
        return kits;
    }

}
