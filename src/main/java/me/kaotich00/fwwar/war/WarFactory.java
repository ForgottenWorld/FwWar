package me.kaotich00.fwwar.war;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.assault.ClassicWar;
import me.kaotich00.fwwar.war.bolt.FactionWar;
import me.kaotich00.fwwar.war.bolt.RandomFactionWar;

public class WarFactory {

    private final Fwwar plugin;

    public WarFactory(Fwwar plugin) {
        this.plugin = plugin;
    }

    public static War getWarForType(WarTypes warType) {
        switch (warType) {
            case ASSAULT_WAR_CLASSIC:
                return new ClassicWar();
            case BOLT_WAR_FACTION:
                return new FactionWar();
            case BOLT_WAR_RANDOM:
                return new RandomFactionWar();
        }
        return null;
    }

}
