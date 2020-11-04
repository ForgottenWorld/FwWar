package me.kaotich00.fwwar.war.bolt;

import com.palmergames.bukkit.towny.object.Nation;
import me.kaotich00.fwwar.objects.kit.Kit;

import java.util.ArrayList;
import java.util.List;

public class FactionWar extends BoltWar {

    List<Nation> nations;
    List<Kit> kits;

    public FactionWar() {
        this.nations = new ArrayList<>();
        this.kits = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "The faction kit war consists in two nations facing each other in a merciless combat! Pick a class and kill every opponent.";
    }

    @Override
    public void addNation(Nation nation) {
        this.nations.add(nation);
    }

    @Override
    public void removeNation(Nation nation) {
        this.nations.remove(nation);
    }

    @Override
    public List<Nation> getParticipants() {
        return null;
    }
}
