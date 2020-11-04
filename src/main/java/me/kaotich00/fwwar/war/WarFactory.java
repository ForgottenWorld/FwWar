package me.kaotich00.fwwar.war;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.bolt.FactionWar;

public class WarFactory {

    private final Fwwar plugin;

    public WarFactory(Fwwar plugin) {
        this.plugin = plugin;
    }

    public static War getWarForType(WarTypes warType) {
        switch (warType) {
            case ASSAULT_WAR:
                break;
            case BOLT_WAR_FACTION:
                return new FactionWar();
            case BOLT_WAR_RANDOM:
                break;
        }
        return null;
    }

}
