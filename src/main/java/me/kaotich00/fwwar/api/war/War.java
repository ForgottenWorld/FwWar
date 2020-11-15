package me.kaotich00.fwwar.api.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.entity.Player;

import java.util.*;

public interface War {

    /**
     * Get a comprehensive description of the War
     * @return a String containing the description
     */
    String getDescription();

    /**
     * Push a participant Nation to the War.
     * @param nation the Nation to be added
     */
    void addNation(Nation nation);

    /**
     * Remove a given Nation from the War.
     * @param nation the Nation to be removed
     */
    void removeNation(Nation nation);

    /**
     * Remove a given Town from the War.
     * @param town the Town to be removed
     */
    void removeTown(Town town);

    /**
     * Get a list of participants Nations to the war.
     * @return a List of Nation
     */
    List<Nation> getParticipantsNations();

    /**
     * Get a list of participants Towns to the war.
     * @return a List of Town
     */
    Set<Town> getParticipantsTowns();

    /**
     * Retrieve the war type of the current War.
     * @return the WarTypes enum
     */
    WarTypes getWarType();

    /**
     * Start the current War.
     */
    void startWar();

    /**
     * Stop the current War.
     */
    void stopWar();

    /**
     * Check wether the current war supports Kits.
     * @return true if the war support kits, false otherwise
     */
    boolean supportKits();

    /**
     * Check wheter or not the war has a participants limit
     * @return the participant limit
     */
    boolean hasParticipantsLimit();

    /**
     * Get the maximum amount of participants allowed for the war.
     * It is best advised to use it in combination with hasParticipantsLimit()
     * @return the number of max allowed participants
     */
    int getMaxAllowedParticipants();

    /**
     * Add a player to the war.
     * @param town the Town of the player
     * @param playerUUID the UUID of the player to be added
     */
    void addPlayerToWar(Town town, UUID playerUUID);

    /**
     * Remove a player from the war.
     * @param town the Town of the player
     * @param playerUUID the UUID of the player to be removed
     */
    void removePlayerFromWar(Town town, UUID playerUUID);

    /**
     * Get a list of UUID of participants player for the given Town
     * @param town the Town to search for player
     * @return a List of UUIDs
     */
    List<UUID> getParticipantsForTown(Town town);

    /**
     * Set the current status for the war
     * @param status the WarStatus
     */
    void setWarStatus(WarStatus status);

    /**
     * Get the current status of the war
     * @return the WarStatus
     */
    WarStatus getWarStatus();

    /**
     * This method handles the death of a player during the war.
     * It does not return anything, it just calculate the consequences of the death
     * for the given Player. A Nation or a Town might be defeated if it is the last
     * standing player for example.
     * @param player the Player
     */
    void handlePlayerDeath(Player player);

    /**
     * This method add a Player to the Death Queue.
     * The Death Queue is a Buffer used in the PlayerRespawnEvent.
     * Given the fact that you cannot teleport a player or remove a scoreboard
     * on the PlayerDeathEvent, the player is added to a temporary Queue that will
     * be used on his Respawn.
     * @param player the Player to be added
     */
    void addPlayerToDeathQueue(Player player);

    /**
     * Remove a player from the Death Queue.
     * @param player the Player to be removed
     */
    void removePlayerFromDeathQueue(Player player);

    /**
     * Check wether a player is in a Death Queue
     * @param player the Player to check
     * @return true if the Player is found, false otherwise
     */
    boolean isPlayerInDeathQueue(Player player);

    /**
     * Increment the player kill count for the scoreboard leaderboard by the given amount.
     * @param player the Player
     * @param incrementBy the amount to increment by
     */
    void incrementPlayerKillCount(Player player, int incrementBy);

    /**
     * Return the amount of kills a Player made during the war
     * @param player the Player to search for
     * @return the amount of kills
     */
    int getPlayerKillCount(Player player);

    /**
     * This method returns the leaderboard in descending order for the current war.
     * @return an ordered Map of player UUID and score
     */
    LinkedHashMap<UUID, Integer> getKillCountsLeaderboard();

    /**
     * Return a full list of UUIDs of the participants players
     * @return a List of player UUIDs
     */
    List<UUID> getParticipantPlayers();

}
