package me.kaotich00.fwwar.war.bolt;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class BoltWar implements War {

    protected WarStatus warStatus;
    protected Map<UUID, Integer> killCount;

    @Override
    public int getMaxAllowedParticipants() {
        return 2;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return true;
    }

    @Override
    public void setWarStatus(WarStatus warStatus) {
        this.warStatus = warStatus;
    }

    @Override
    public WarStatus getWarStatus() {
        return warStatus;
    }

    @Override
    public void incrementPlayerKillCount(Player player, int incrementBy) {
        if(!this.killCount.containsKey(player.getUniqueId())) {
            this.killCount.put(player.getUniqueId(), 0);
        }
        this.killCount.put(player.getUniqueId(), this.killCount.get(player.getUniqueId()) + incrementBy);
    }

    @Override
    public int getPlayerKillCount(Player player){
        return this.killCount.get(player.getUniqueId());
    }

    @Override
    public LinkedHashMap<UUID, Integer> getKillCountsLeaderboard(){
        LinkedHashMap<UUID, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : killCount.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, Comparator.comparingInt(o -> (int) o).reversed());
        for (Integer kills : list) {
            for (Map.Entry<UUID, Integer> entry : killCount.entrySet()) {
                if (entry.getValue().equals(kills)) {
                    sortedMap.put(entry.getKey(), kills);
                }
            }
        }
        return sortedMap;
    }

}
