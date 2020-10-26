package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class SimpleStorageService {

    private static SimpleStorageService instance;
    private String saveCorePlotsFile = "coreplots.yml";
    private FileConfiguration corePlotConfig;
    private Fwwar plugin;

    private SimpleStorageService(Fwwar plugin) {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.plugin = plugin;
    }

    public static SimpleStorageService getInstance(Fwwar plugin) {
        if(instance == null) {
            instance = new SimpleStorageService(plugin);
        }
        return instance;
    }

    public void saveCorePlots() throws IOException {
        Map<UUID, CorePlot> corePlots = SimplePlotService.getInstance().getCorePlots();

        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), saveCorePlotsFile));
        data.createSection("coreplots", corePlots);

        data.save(new File(plugin.getDataFolder(), saveCorePlotsFile));
    }

    public void loadCorePlots() {
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), saveCorePlotsFile));
        SimplePlotService simplePlotService = SimplePlotService.getInstance();

        if(data != null && data.getConfigurationSection("coreplots") != null) {
            for (String key : data.getConfigurationSection("coreplots").getKeys(false)) {
                simplePlotService.setCorePlotOfTown(UUID.fromString(key), (CorePlot) data.get("coreplots." + key));
            }
        }

    }

}
