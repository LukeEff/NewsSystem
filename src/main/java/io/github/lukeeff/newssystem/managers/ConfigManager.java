package io.github.lukeeff.newssystem.managers;


import io.github.lukeeff.newssystem.NewsSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private NewsSystem plugin;
    private FileConfiguration config;

    /**
     * Constructor for ConfigManager.
     * <p>The constructor for the ConfigManager class will
     * initialize a FileConfiguration object that can be modified
     * in the root NewsSystem folder</p>
     * @param instance the NewsSystem instance
     */
    public ConfigManager(NewsSystem instance) {
        plugin = instance;
        createRootDirectory();
        setConfig(getConfigFile());
    }

    /**
     * Get the instance of the config reference
     * @return the configuration object
     */
    public FileConfiguration getConfig() {
        return config;
    }


    /**
     * Creates root plugin folder directory
     */
    private void createRootDirectory() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }

    /**
     * Initializes the configuration value
     * When a config file does not exist, a
     * brand new config file with the compiled
     * config values is written
     * @param configFile the configuration file
     *                   that will be set as a
     *                   global variable for getting
     *                   and setting values from
     */
    private void setConfig(File configFile) {
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the config file from the plugin folder
     * <p>This method loks for an existing config
     * file inside of its root folder and return it.
     * When it is not found, it creates a new config.yml
     * file and returns the information inside of the
     * default config instead</p>
     * @return A reference to the config file in the dataFolder
     */
    private File getConfigFile() {
        final String NEWCONFIG = ChatColor.GREEN + "Config not found! Creating new config file.";
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(NEWCONFIG);
            plugin.saveDefaultConfig();
        }
        return configFile;
    }


}
