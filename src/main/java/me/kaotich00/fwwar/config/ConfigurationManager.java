package me.kaotich00.fwwar.config;

import me.kaotich00.fwwar.Fwwar;

public class ConfigurationManager {

    private static ConfigurationManager configInstance;
    private Fwwar plugin;

    private ConfigurationManager(Fwwar plugin) {
        if (configInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.plugin = plugin;
    }

    public static ConfigurationManager getInstance() {
        if(configInstance == null) {
            configInstance = new ConfigurationManager(Fwwar.getPlugin(Fwwar.class));
        }
        return configInstance;
    }

    public void reloadDefaultConfig() {
        plugin.reloadDefaultConfig();
    }

}
