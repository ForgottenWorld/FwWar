package me.kaotich00.fwwar.objects.war;

import org.bukkit.entity.Player;

import java.util.*;

public class KillCounter {

    protected final Map<UUID, Integer> killCount;

    public KillCounter() {
        this.killCount = new HashMap<>();
    }

    /**
     * Increment the player kill count for the scoreboard leaderboard by the given amount.
     * @param player the Player
     * @param incrementBy the amount to increment by
     */
    public void incrementPlayerScore(Player player, int incrementBy) {
        if(!this.killCount.containsKey(player.getUniqueId())) {
            this.killCount.put(player.getUniqueId(), 0);
        }
        this.killCount.put(player.getUniqueId(), this.killCount.get(player.getUniqueId()) + incrementBy);
    }

    /**
     * Return the amount of kills a Player made during the war
     * @param player the Player to search for
     * @return the amount of kills
     */
    public int getPlayerScore(Player player){
        return this.killCount.get(player.getUniqueId());
    }

    /**
     * This method returns the leaderboard in descending order for the current war.
     * @return an ordered Map of player UUID and score
     */
    public LinkedHashMap<UUID, Integer> getLeaderboard(){
        LinkedHashMap<UUID, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : killCount.entrySet()) {
            list.add(entry.getValue());
        }
        list.sort(Comparator.comparingInt(o -> (int) o).reversed());
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
