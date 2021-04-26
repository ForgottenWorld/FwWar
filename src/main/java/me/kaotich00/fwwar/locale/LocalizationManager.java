package me.kaotich00.fwwar.locale;

import me.kaotich00.fwwar.Fwwar;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocalizationManager {

    private static LocalizationManager instance;
    private final Fwwar plugin;
    private final String defaultLanguageFile = "language_it_IT.yml";

    private final Map<String, String> strings;

    private LocalizationManager(Fwwar plugin) {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.strings = new HashMap<>();
        this.plugin = plugin;
    }

    public static LocalizationManager getInstance(Fwwar plugin) {
        if(instance == null) {
            instance = new LocalizationManager(plugin);
        }
        return instance;
    }

    public void initDefaultLocalization() {
        plugin.saveResource(defaultLanguageFile, true);
    }

    public void loadLanguageFile() {
        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        initDefaultLocalization();

        String localizationFile = defaultConfig.getString("localization.language_file");

        if(localizationFile == null) {
            localizationFile = defaultLanguageFile;
        }

        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), localizationFile));
        for (String key : Objects.requireNonNull(data.getConfigurationSection("strings")).getKeys(false)) {
            this.strings.put(key, data.getString("strings." + key));
        }
    }

    public void reload() {
        this.strings.clear();
        loadLanguageFile();
    }

    public String localize(String key) {
        return this.strings.containsKey(key) ? this.strings.get(key) : ChatColor.RED + "No translation present for " + key;
    }

}
