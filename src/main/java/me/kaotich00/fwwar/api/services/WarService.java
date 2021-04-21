package me.kaotich00.fwwar.api.services;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface WarService {

    Optional<War> getWar();

    void setCurrentWar(War war);

    int getWarTaskId();

}
