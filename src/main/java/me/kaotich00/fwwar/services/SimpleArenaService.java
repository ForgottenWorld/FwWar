package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.utils.LocationType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

    private final List<Arena> arenas;
    private final Map<UUID, Arena> playerCreationArena;
    private final Map<UUID, CreationStep> playerCreationStep;

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
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "First nation spawn point");
                break;

            case FIRST_NATION_BATTLE_POINT:
                arena.setLocation(LocationType.FIRST_NATION_BATTLE_POINT, location);
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "First nation battle point");
                break;

            case SECOND_NATION_SPAWN_POINT:
                arena.setLocation(LocationType.SECOND_NATION_SPAWN_POINT, location);
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "Second nation spawn point");
                break;

            case SECOND_NATION_BATTLE_POINT:
                arena.setLocation(LocationType.SECOND_NATION_BATTLE_POINT, location);
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                Message.ARENA_CREATION_STEP_COMPLETED.send(sender, "Second nation battle point");
                break;
        }

        if(arena.getGameLocations().size() == 4) {
            Message.ARENA_CREATION_COMPLETED.send(sender);
            location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP,1,1);
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

    public void spawnEffectAtBlock(Location loc) {
        loc.add(0.5,0.5,0.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 5);
        loc.getWorld().spawnParticle(Particle.REDSTONE,loc,10,dustOptions);
    }

    public void deleteArena(Arena arena) {
        this.arenas.remove(arena);
    }

    public Arena getRandomArena() {
        Random random = new Random();

        List<Arena> arenas = getArenas();
        int size = arenas.size();

        return arenas.get(size > 1 ? random.nextInt(size - 1) : 0);
    }

}
