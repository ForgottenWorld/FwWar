package me.kaotich00.fwwar.api.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface War {

    String getDescription();

    void addNation(Nation nation);

    void removeNation(Nation nation);

    List<Nation> getParticipants();

    void addKit(Kit kit);

    void removeKit(String kitName);

    void updateKit(String kitName, Kit kit);

    Collection<Kit> getKits();

    Optional<Kit> getKitForName(String name);

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

}
