package me.kaotich00.fwwar.war;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.objects.war.DeathQueue;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.objects.war.KillCounter;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.objects.war.ParticipantTown;
import me.kaotich00.fwwar.utils.WarStatus;

import java.util.*;

public abstract class AbstractWar implements War {

    /**
     * The status for the war, it can assume the following values:
     *  - CREATED: The war has been created and it's in setup mode
     *  - CONFIRMED: All participants have been confirmed
     *  - STARTED: The war has started and is in progress
     *  - ENDED: The war has ended
     */
    protected WarStatus warStatus;
    /* The list of all participant nations */
    protected Map<UUID, ParticipantNation> participants;
    /* Queue handler for PlayerDeathEvent */
    protected DeathQueue deathQueue;
    /* Kill counter handler for scoreboard Leaderboard */
    protected KillCounter killCounter;
    /* The Arena in which the war will be disputed */
    protected Arena arena;

    public AbstractWar() {
        this.participants = new HashMap<>();
        this.deathQueue = new DeathQueue();
        this.killCounter = new KillCounter();
    }

    @Override
    public WarStatus getWarStatus() {
        return warStatus;
    }

    @Override
    public void setWarStatus(WarStatus warStatus) {
        this.warStatus = warStatus;
    }

    @Override
    public void addParticipant(Nation nation) {
        if(this.participants.containsKey(nation.getUuid()))
            return;

        this.participants.put(nation.getUuid(), new ParticipantNation(nation));
    }

    @Override
    public void removeParticipant(Nation nation) {
        this.participants.remove(nation.getUuid());
    }

    @Override
    public ParticipantNation getParticipant(UUID nationUUID) {
        return this.participants.get(nationUUID);
    }

    @Override
    public boolean hasNation(Nation nation) {
        return this.participants.containsKey(nation.getUuid());
    }

    @Override
    public boolean hasTown(Town town) {
        UUID nationUUID;
        try {
            if(!hasNation(town.getNation())) return false;
            nationUUID = town.getNation().getUuid();
        } catch (NotRegisteredException e) {
            return false;
        }

        ParticipantTown participantTown = this.getParticipant(nationUUID).getTown(town.getUuid());
        return participantTown != null;
    }

    @Override
    public boolean hasResident(Resident resident) {
        UUID townUUID;
        UUID nationUUID;
        try {
            if(!hasNation(resident.getTown().getNation())) return false;
            if(!hasTown(resident.getTown())) return false;

            townUUID = resident.getTown().getUuid();
            nationUUID = resident.getTown().getNation().getUuid();
        } catch (NotRegisteredException e) {
            return false;
        }

        return this.getParticipant(nationUUID).getTown(townUUID).getPlayers().contains(resident.getPlayer().getUniqueId());
    }

    @Override
    public List<ParticipantNation> getParticipants() {
        return new ArrayList<>(this.participants.values());
    }

    @Override
    public Optional<Arena> getArena() {
        return Optional.ofNullable(arena);
    }

    @Override
    public void setArena(Arena arena) {
        this.arena = arena;
    }

    @Override
    public DeathQueue getDeathQueue() {
        return this.deathQueue;
    }

    @Override
    public KillCounter getKillCounter() {
        return this.killCounter;
    }
}
