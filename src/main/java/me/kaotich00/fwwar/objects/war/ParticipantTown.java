package me.kaotich00.fwwar.objects.war;

import com.palmergames.bukkit.towny.object.Town;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ParticipantTown {

    private final Town town;
    private final Set<UUID> players;
    private boolean isDefeated;

    public ParticipantTown(Town town) {
        this.town = town;
        this.players = new HashSet<>();
        this.isDefeated = false;
    }

    public Town getTown() {
        return this.town;
    }

    public void addPlayer(UUID playerUUID) {
        this.players.add(playerUUID);
    }

    public void removePlayer(UUID playerUUID) {
        this.players.remove(playerUUID);
    }

    public Set<UUID> getPlayers() {
        return this.players;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
    }
}
