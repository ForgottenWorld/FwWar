package me.kaotich00.fwwar.war.bolt;

import me.kaotich00.fwwar.api.war.War;

public abstract class BoltWar implements War {

    @Override
    public int getMaxAllowedParticipants() {
        return 2;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return true;
    }
}
