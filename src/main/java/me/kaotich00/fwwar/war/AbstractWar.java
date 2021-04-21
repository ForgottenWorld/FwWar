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
    /*
        This Map is updated with current
        participating town and nations, which means
        that when a town or nation are defeated, they
        will be removed from the map
     */
    protected Map<UUID, ParticipantNation> participants;
    /*
        Those are container used to simplify
        search operations, making them O(1)
    */
    protected Set<UUID> nationsContainer;
    protected Set<UUID> townsContainer;
    protected Set<UUID> playersContainer;

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

        this.nationsContainer = new HashSet<>();
        this.townsContainer = new HashSet<>();
        this.playersContainer = new HashSet<>();
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
    public void addNation(Nation nation) {
        if(this.participants.containsKey(nation.getUuid()))
            return;

        this.participants.put(nation.getUuid(), new ParticipantNation(nation));
        this.nationsContainer.add(nation.getUuid());
    }

    @Override
    public void removeNation(Nation nation) {
        this.participants.remove(nation.getUuid());
        this.nationsContainer.remove(nation.getUuid());
    }

    @Override
    public ParticipantNation getNation(UUID nationUUID) {
        return this.participants.get(nationUUID);
    }

    @Override
    public boolean hasNation(Nation nation) {
        return this.nationsContainer.contains(nation.getUuid());
    }

    @Override
    public boolean hasTown(Town town) {
        return this.townsContainer.contains(town.getUuid());
    }

    @Override
    public boolean hasResident(Resident resident) {
        return this.playersContainer.contains(resident.getUUID());
    }

    @Override
    public List<ParticipantNation> getNations() {
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
