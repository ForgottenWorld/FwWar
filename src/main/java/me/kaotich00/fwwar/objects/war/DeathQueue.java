package me.kaotich00.fwwar.objects.war;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathQueue {

    private final List<UUID> players;

    public DeathQueue() {
        this.players = new ArrayList<>();
    }

    /**
     * This method add a Player to the Death Queue.
     * The Death Queue is a Buffer used in the PlayerRespawnEvent.
     * Given the fact that you cannot teleport a player or remove a scoreboard
     * on the PlayerDeathEvent, the player is added to a temporary Queue that will
     * be used on his Respawn.
     * @param player the Player to be added
     */
    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
    }

    /**
     * Remove a player from the Death Queue.
     * @param player the Player to be removed
     */
    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
    }

    /**
     * Check whether a player is in a Death Queue
     * @param player the Player to check
     * @return true if the Player is found, false otherwise
     */
    public boolean hasPlayer(Player player) {
        return this.players.contains(player.getUniqueId());
    }

}
