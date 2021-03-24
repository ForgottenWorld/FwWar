package me.kaotich00.fwwar.objects.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import java.util.*;

public class ParticipantNation {

    private final Nation nation;
    private final Map<UUID, ParticipantTown> towns;
    private boolean isDefeated;

    public ParticipantNation(Nation nation) {
        this.nation = nation;
        this.towns = new HashMap<>();
        this.isDefeated = false;
    }

    public Nation getNation() {
        return nation;
    }

    public void addTown(Town town) {
        if(!this.towns.containsKey(town.getUuid()))
            this.towns.put(town.getUuid(), new ParticipantTown(town));
    }

    public void removeTown(UUID townUUID) {
        this.towns.remove(townUUID);
    }

    public ParticipantTown getTown(UUID townUUID) {
        return this.towns.get(townUUID);
    }

    public List<ParticipantTown> getTowns() {
        return new ArrayList<>(this.towns.values());
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
    }
}
