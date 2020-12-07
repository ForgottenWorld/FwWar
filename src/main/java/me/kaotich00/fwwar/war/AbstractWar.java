package me.kaotich00.fwwar.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class AbstractWar implements War {

    protected WarStatus warStatus;
    protected Map<UUID, Integer> killCount;
    protected List<Nation> nations;
    protected Map<Town, List<UUID>> players;
    protected List<UUID> deathQueue;
    protected Arena arena;

    @Override
    public void setWarStatus(WarStatus warStatus) {
        this.warStatus = warStatus;
    }

    @Override
    public void addNation(Nation nation) {
        this.nations.add(nation);
    }

    @Override
    public void removeNation(Nation nation) {
        this.nations.remove(nation);
    }

    @Override
    public void removeTown(Town town) {
        this.players.remove(town);
    }

    @Override
    public List<Nation> getParticipantsNations() {
        return nations;
    }

    @Override
    public Set<Town> getParticipantsTowns() {
        return this.players.keySet();
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

    @Override
    public void addPlayerToWar(Town town, UUID playerUUID) {
        if(!this.players.containsKey(town)) {
            this.players.put(town, new ArrayList<>());
        }
        this.players.get(town).add(playerUUID);
    }

    @Override
    public void removePlayerFromWar(Town town, UUID playerUUID) {
        if(!this.players.containsKey(town)) {
            this.players.put(town, new ArrayList<>());
        }
        this.players.get(town).remove(playerUUID);

        if(this.players.get(town).size() == 0) {
            this.players.remove(town);
        }
    }

    @Override
    public List<UUID> getParticipantsForTown(Town town) {
        return this.players.get(town);
    }

    @Override
    public void addPlayerToDeathQueue(Player player) {
        this.deathQueue.add(player.getUniqueId());
    }

    @Override
    public void removePlayerFromDeathQueue(Player player) {
        this.deathQueue.remove(player.getUniqueId());
    }

    @Override
    public boolean isPlayerInDeathQueue(Player player) {
        return this.deathQueue.contains(player.getUniqueId());
    }

    @Override
    public List<UUID> getParticipantPlayers() {
        List<UUID> playerList = new ArrayList<>();

        for(Map.Entry<Town, List<UUID>> entry : this.players.entrySet()) {
            List<UUID> currentList = entry.getValue();
            playerList.addAll(currentList);
        }

        return playerList;
    }

    @Override
    public void setArena(Arena arena) {
        this.arena = arena;
    }

    @Override
    public Optional<Arena> getArena() {
        return Optional.ofNullable(arena);
    }
}
