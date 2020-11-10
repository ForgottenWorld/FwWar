package me.kaotich00.fwwar.objects.arena;

import me.kaotich00.fwwar.utils.LocationType;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Arena {

    private String name;

    private Map<LocationType, Location> gameLocations;

    public Arena(String name) {
        this.name = name;

        this.gameLocations = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public void setLocation(LocationType locationType, Location location) {
        this.gameLocations.put(locationType, location);
    }

    public Location getLocation(LocationType locationType) {
        return this.gameLocations.get(locationType);
    }

    public Map<LocationType, Location> getGameLocations() {
        return this.gameLocations;
    }

}
