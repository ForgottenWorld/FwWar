package me.kaotich00.fwwar.api.services;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface WarService {

    Optional<War> getCurrentWar();

    void setCurrentWar(War war);

    int getWarTaskId();

    void addKit(WarTypes warType, Kit kit);

    void removeKit(WarTypes warType, String kitName);

    void updateKit(WarTypes warType, String kitName, Kit kit);

    Collection<Kit> getKitsForType(WarTypes warType);

    Optional<Kit> getKitForName(WarTypes warType, String name);

    Map<Kit, WarTypes> getAllKits();

}
