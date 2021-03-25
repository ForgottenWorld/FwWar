package me.kaotich00.fwwar.api.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.objects.war.DeathQueue;
import me.kaotich00.fwwar.objects.war.KillCounter;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.entity.Player;

import java.util.*;

public interface War {

    /**
     * Get the current status of the war
     * @return the WarStatus
     */
    WarStatus getWarStatus();

    /**
     * Set the current status for the war
     * @param status the WarStatus
     */
    void setWarStatus(WarStatus status);

    /**
     * Retrieve the war type of the current War.
     * @return the WarTypes enum
     */
    WarTypes getWarType();

    /**
     * Get a comprehensive description of the War
     * @return a String containing the description
     */
    String getDescription();

    /**
     * Push a participant Nation to the War.
     * @param nation the Nation to be added
     */
    void addParticipant(Nation nation);

    /**
     * Remove a given Nation from the War.
     * @param nation the Nation to be removed
     */
    void removeParticipant(Nation nation);

    /**
     * Get a single ParticipantNation entry.
     * @return ParticipantNation the participant nation
     */
    ParticipantNation getParticipant(UUID nationUUID);

    /**
     * Get a list of participants Nations to the war.
     * @return a List of Nation
     */
    List<ParticipantNation> getParticipants();

    /**
     * Check whether or not the war has enough participants
     * @return true if the war has enough participants, false otherwise
     */
    boolean hasEnoughParticipants();

    /**
     * Check if the given nation is in the war
     * @return true if present, false otherwise
     */
    boolean hasNation(Nation nation);

    /**
     * Check if the given town is in the war
     * @return true if present, false otherwise
     */
    boolean hasTown(Town town);

    /**
     * Check if the given player is in the war
     * @return true if present, false otherwise
     */
    boolean hasResident(Resident resident);

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
     * This method handles the death of a player during the war.
     * It does not return anything, it just calculate the consequences of the death
     * for the given Player. A Nation or a Town might be defeated if it is the last
     * standing player for example.
     * @param player the Player
     */
    void handlePlayerDeath(Player player);

    /**
     * Set the arena for the current war
     * @param arena the Arena
     */
    void setArena(Arena arena);

    /**
     * Return the Arena where the current war will be played
     * @return an Optional of Arena
     */
    Optional<Arena> getArena();

    KillCounter getKillCounter();

    DeathQueue getDeathQueue();

}
