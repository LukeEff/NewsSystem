package io.github.lukeeff.newssystem.managers;


import io.github.lukeeff.newssystem.NewsSystem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * The config manager class will handle all tasks
 * involving creating the config file, folder, and
 * assigning a reference to the config so it can
 * be manipulated.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class ConfigManager {

    @Getter private final NewsSystem plugin;
    @Getter @Setter private FileConfiguration config;
    @Getter @Setter private File configFile;

    /**
     * Constructor for ConfigManager.
     *
     * The constructor for the ConfigManager class will
     * initialize a FileConfiguration object that can be modified
     * in the root NewsSystem folder
     *
     * @param instance the NewsSystem instance
     */
    public ConfigManager(@NonNull final NewsSystem instance) {
        this.plugin = instance;
        createRootDirectory();
        createConfigFile();
        loadConfig();
    }

    /**
     * Creates root plugin folder directory
     */
    private void createRootDirectory() {
        final File folder = getPlugin().getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * Initializes the configuration value
     * When a config file does not exist, a
     * brand new config file with the compiled
     * config values is written
     */
    private void loadConfig() {
        setConfig(new YamlConfiguration());
        try {
            getConfig().load(getConfigFile());
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration file. Required to update news values inputted by a user.
     */
    public void saveConfig() {
        try {
            getConfig().save(createConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the config file from the plugin folder
     *
     * This method looks for an existing config
     * file inside of its root folder and return it.
     * When it is not found, it creates a new config.yml
     * file and returns the information inside of the
     * default config instead
     *
     * @return A reference to the config file in the dataFolder
     */
    private File createConfigFile() {
        setConfigFile(new File(getPlugin().getDataFolder(), "config.yml"));
        if(!getConfigFile().exists()) {
            getPlugin().saveDefaultConfig();
        }
        return configFile;
    }


}
