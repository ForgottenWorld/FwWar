package me.kaotich00.fwwar.api.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.entity.Player;

import java.util.*;

public interface War {

    String getDescription();

    void addNation(Nation nation);

    void removeNation(Nation nation);

    List<Nation> getParticipantsNations();

    Set<Town> getParticipantsTowns();

    WarTypes getWarType();

    void startWar();

    boolean supportKits();

    void setPlayerKit(Player player, Kit kit);

    Optional<Kit> getPlayerKit(Player player);

    boolean hasParticipantsLimit();

    int getMaxAllowedParticipants();

    void addPlayerToWar(Town town, UUID playerUUID);

    void removePlayerFromWar(Town town, UUID playerUUID);

    List<UUID> getParticipantsForTown(Town town);

    void setWarStatus(WarStatus status);

    WarStatus getWarStatus();

    void handlePlayerDeath(Player player);

    void addPlayerToDeathQueue(Player player);

    void removePlayerFromDeathQueue(Player player);

    boolean isPlayerInDeathQueue(Player player);

}
