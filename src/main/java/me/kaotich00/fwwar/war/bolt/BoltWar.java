package me.kaotich00.fwwar.war.bolt;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.utils.WarStatus;

public abstract class BoltWar implements War {

    protected WarStatus warStatus;

    @Override
    public int getMaxAllowedParticipants() {
        return 2;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return true;
    }

    @Override
    public void setWarStatus(WarStatus warStatus) {
        this.warStatus = warStatus;
    }

    @Override
    public WarStatus getWarStatus() {
        return warStatus;
    }

}
