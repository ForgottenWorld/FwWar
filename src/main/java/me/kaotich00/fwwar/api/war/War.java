package me.kaotich00.fwwar.api.war;

import com.palmergames.bukkit.towny.object.Nation;

import java.util.List;

public interface War {

    String getDescription();

    void addNation(Nation nation);

    void removeNation(Nation nation);

    List<Nation> getParticipants();

}
