package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.utils.LocationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SimpleArenaService {

    private static SimpleArenaService instance;

    public enum CreationStep {
        FIRST_NATION_SPAWN_POINT,
        FIRST_NATION_BATTLE_POINT,
        SECOND_NATION_SPAWN_POINT,
        SECOND_NATION_BATTLE_POINT
    }

    private List<Arena> arenas;
    private Map<UUID, Arena> playerCreationArena;
    private Map<UUID, CreationStep> playerCreationStep;

    private SimpleArenaService() {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.arenas = new ArrayList<>();
        this.playerCreationArena = new HashMap<>();
        this.playerCreationStep = new HashMap<>();
    }

    public static SimpleArenaService getInstance() {
        if(instance == null) {
            instance = new SimpleArenaService();
        }
        return instance;
    }

    public List<Arena> getArenas() {
        return this.arenas;
    }

    public Optional<Arena> getArenaByName(String name) {
        return this.arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Arena newArena(String name) {
        Arena newArena = new Arena(name);
        this.arenas.add(newArena);

        return newArena;
    }

    public void arenaCreationHandler(Player sender, Location location) {
        if(!playerCreationArena.containsKey(sender.getUniqueId())) {
            return;
        }

        Arena arena = this.playerCreationArena.get(sender.getUniqueId());

        CreationStep step = playerCreationStep.get(sender.getUniqueId());
        switch(step) {
            case FIRST_NATION_SPAWN_POINT:
                arena.setLocation(LocationType.FIRST_NATION_SPAWN_POINT, location);

                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "First nation spawn point");
                break;

            case FIRST_NATION_BATTLE_POINT:
                arena.setLocation(LocationType.FIRST_NATION_BATTLE_POINT, location);

                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "First nation battle point");
                break;

            case SECOND_NATION_SPAWN_POINT:
                arena.setLocation(LocationType.SECOND_NATION_SPAWN_POINT, location);

                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "Second nation spawn point");
                break;

            case SECOND_NATION_BATTLE_POINT:
                arena.setLocation(LocationType.SECOND_NATION_BATTLE_POINT, location);

                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "Second nation battle point");
                break;
        }

        this.playerCreationArena.remove(sender.getUniqueId());
        this.playerCreationStep.remove(sender.getUniqueId());
    }

    public void setPlayerArenaSetupStep(Player player, Arena arena, CreationStep creationStep) {
        this.playerCreationArena.put(player.getUniqueId(), arena);
        this.playerCreationStep.put(player.getUniqueId(), creationStep);
    }

    public boolean isPlayerInCreationMode(Player player) {
        return this.playerCreationArena.containsKey(player.getUniqueId());
    }

}
