package me.kaotich00.fwwar.storage;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleArenaService;
import me.kaotich00.fwwar.services.SimpleKitService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.LocationType;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StorageManager {

    private static StorageManager instance;
    private final String saveArenasFile = "arenas.yml";
    private final String saveKitsFile = "kits.yml";
    private final Fwwar plugin;

    private StorageManager(Fwwar plugin) {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.plugin = plugin;
    }

    public static StorageManager getInstance(Fwwar plugin) {
        if(instance == null) {
            instance = new StorageManager(plugin);
        }
        return instance;
    }

    public void saveArenas() throws IOException {
        List<Arena> arenas = SimpleArenaService.getInstance().getArenas();

        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), saveArenasFile));
        data.set("arenas", null);
        for(Arena arena: arenas) {
            for(Map.Entry<LocationType,Location> entry: arena.getGameLocations().entrySet()) {
                LocationType locationType = entry.getKey();
                Location location = entry.getValue();

                data.set("arenas." + arena.getName() + "." + locationType.toString() + ".world", location.getWorld().getUID().toString());
                data.set("arenas." + arena.getName() + "." + locationType.toString() + ".x", location.getBlockX());
                data.set("arenas." + arena.getName() + "." + locationType.toString() + ".y", location.getBlockY());
                data.set("arenas." + arena.getName() + "." + locationType.toString() + ".z", location.getBlockZ());
            }
        }

        data.save(new File(plugin.getDataFolder(), saveArenasFile));
    }

    public void loadArenas() {
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), saveArenasFile));
        SimpleArenaService simpleArenaManager = SimpleArenaService.getInstance();

        if(data.getConfigurationSection("arenas") != null) {
            for (String key : data.getConfigurationSection("arenas").getKeys(false)) {
                Arena arena = simpleArenaManager.newArena(key);
                Bukkit.getConsoleSender().sendMessage("    >> Loaded Arena " + arena.getName());

                for (String key2 : data.getConfigurationSection("arenas." + key).getKeys(false)) {
                    LocationType locationType = LocationType.valueOf(key2);

                    UUID locationWorld = UUID.fromString(data.getString("arenas." + key + "." + key2 + ".world"));
                    int locationX = data.getInt("arenas." + key + "." + key2 + ".x");
                    int locationY = data.getInt("arenas." + key + "." + key2 + ".y");
                    int locationZ = data.getInt("arenas." + key + "." + key2 + ".z");

                    Location location = new Location(Bukkit.getWorld(locationWorld), locationX, locationY, locationZ);

                    arena.setLocation(locationType, location);
                }
            }
        }

    }

    public void saveKits() throws IOException {
        Map<Kit, WarTypes> kits = SimpleKitService.getInstance().getAllKits();

        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), saveKitsFile));
        for(Map.Entry<Kit,WarTypes> entry: kits.entrySet()) {
            Kit kit = entry.getKey();
            WarTypes warType = entry.getValue();

            data.set("kits." + kit.getName() + ".type", warType.name());
            data.set("kits." + kit.getName() + ".items", kit.getItemsList());
            data.set("kits." + kit.getName() + ".required", kit.isRequired());
            data.set("kits." + kit.getName() + ".quantity", kit.getQuantity());
        }

        data.save(new File(plugin.getDataFolder(), saveKitsFile));
    }

    public void loadKits() {
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), saveKitsFile));
        SimpleKitService simpleKitService = SimpleKitService.getInstance();

        if(data.getConfigurationSection("kits") != null) {
            for (String key : data.getConfigurationSection("kits").getKeys(false)) {
                Kit kit = new Kit(key);
                List<ItemStack> items = (List<ItemStack>) data.get("kits." + key + ".items");
                for(ItemStack item: items) {
                    kit.addItemToKit(item);
                }

                WarTypes warType = WarTypes.valueOf(data.get("kits." + key + ".type").toString());

                kit.setRequired(data.get("kits." + key + ".required").equals("true") ? true : false);
                kit.setQuantity((int) data.get("kits." + key + ".quantity"));

                simpleKitService.addKit(warType, kit);

                Bukkit.getConsoleSender().sendMessage("    >> Loaded Kit " + kit.getName());
            }
        }

    }

}
