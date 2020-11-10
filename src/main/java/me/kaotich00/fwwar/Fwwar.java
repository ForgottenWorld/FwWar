package me.kaotich00.fwwar;

import me.kaotich00.fwwar.commands.WarCommandManager;
import me.kaotich00.fwwar.listener.ArenaCreationListener;
import me.kaotich00.fwwar.listener.PlayerListener;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.services.SimpleStorageService;
import me.kaotich00.fwwar.storage.StorageManager;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Fwwar extends JavaPlugin {

    public static FileConfiguration defaultConfig;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Loading configuration...");
        loadConfiguration();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Registering commands...");
        registerCommands();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Registering serializable objects...");
        ConfigurationSerialization.registerClass(CorePlot.class);

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Registering listeners...");
        registerListeners();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Loading core plots...");
        SimpleStorageService.getInstance(this).loadCorePlots();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Loading Arenas...");
        StorageManager.getInstance(this).loadArenas();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Loading Kits...");
        StorageManager.getInstance(this).loadKits();
    }

    @Override
    public void onDisable() {
        try {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Saving core plots...");
            SimpleStorageService.getInstance(this).saveCorePlots();

            Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Saving arenas...");
            StorageManager.getInstance(this).saveArenas();

            Bukkit.getConsoleSender().sendMessage(MessageUtils.getPluginPrefix() + ChatColor.RESET + " Saving Kits...");
            StorageManager.getInstance(this).saveKits();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        defaultConfig = getConfig();
    }

    public static FileConfiguration getDefaultConfig() {
        return defaultConfig;
    }

    public void reloadDefaultConfig() {
        reloadConfig();
        defaultConfig = getConfig();
    }

    public void registerCommands() {
        getCommand("war").setExecutor(new WarCommandManager(this));
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new ArenaCreationListener(), this);
    }

}
