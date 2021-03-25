package me.kaotich00.fwwar.war.assault;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.war.AbstractWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AssaultWar extends AbstractWar {

    protected HashMap<Nation, List<Town>> townsForNation;

    @Override
    public void addParticipant(Nation nation) {
        super.addParticipant(nation);

        this.townsForNation.put(nation, new ArrayList<>(nation.getTowns()));
    }

    @Override
    public void removeParticipant(Nation nation) {
        super.removeParticipant(nation);

        this.townsForNation.remove(nation);
    }

    public void setTownDefeated(Nation nation, Town town) {
        this.townsForNation.get(nation).remove(town);
    }

    public List<Town> getTownsForNation(Nation nation) {
        return this.townsForNation.get(nation);
    }

    @Override
    public int getMaxAllowedParticipants() {
        return 0;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return false;
    }

    @Override
    public boolean supportKits() {
        return false;
    }

}
