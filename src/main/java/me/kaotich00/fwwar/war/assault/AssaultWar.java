package me.kaotich00.fwwar.war.assault;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.war.AbstractWar;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.*;

public abstract class AssaultWar extends AbstractWar {

    protected HashMap<Nation, List<Town>> townsForNation;

    @Override
    public void addNation(Nation nation) {
        super.addNation(nation);

        this.townsForNation.put(nation, new ArrayList<>(nation.getTowns()));
    }

    @Override
    public void removeNation(Nation nation) {
        super.removeNation(nation);

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

}
