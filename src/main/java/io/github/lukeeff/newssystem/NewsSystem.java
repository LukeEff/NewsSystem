package io.github.lukeeff.newssystem;

import io.github.lukeeff.newssystem.commands.*;
import io.github.lukeeff.newssystem.listeners.PlayerJoin;
import io.github.lukeeff.newssystem.managers.ConfigManager;
import io.github.lukeeff.newssystem.managers.SQLManager;
import io.github.lukeeff.newssystem.utils.BroadcastUtil;
import io.github.lukeeff.newssystem.utils.ConfigUtil;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the project
 *
 * This project demonstrates the current aptitude of my
 * programming skills at this current moment of time
 * for the trial developer application.
 *
 * This project simply sends a player an action bar message
 * packet at timed intervals in an organized order, saves their
 * preference in a database, messages, in a config file, and a
 * few other things.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class NewsSystem extends JavaPlugin {

    @Getter private SQLManager sqlManager;
    @Getter private DatabaseUtil databaseUtil;
    @Getter private ConfigManager configManager;
    @Getter private BroadcastUtil broadcastUtil;
    @Getter private ConfigUtil configUtil;
    @Getter private News newsSubCommands;

    /**
     * Called when server is started.
     */
    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.configUtil = new ConfigUtil(this, getConfigManager().getConfig());
        this.sqlManager = new SQLManager(this);
        this.databaseUtil = new DatabaseUtil(this);
        this.broadcastUtil = new BroadcastUtil(this);
        this.newsSubCommands = new News(this);

        getBroadcastUtil().beginBroadcast();
        setExecutors();
        registerSubCommands();
        registerEvents();
    }

    /**
     * Called on reload or server close.
     */
    @Override
    public void onDisable() {
    }

    /**
     * Registers primary commands.
     */
    private void setExecutors() {
        getCommand("news").setExecutor(newsSubCommands);
        getCommand("togglenews").setExecutor(new ToggleNews());
    }

    /**
     * Registers sub commands for the news command.
     */
    private void registerSubCommands() {
        newsSubCommands.registerNewsSubCommands("remove", new RemoveNews());
        newsSubCommands.registerNewsSubCommands("add", new AddNews());
    }

    /**
     * Registers events that will be listened to.
     */
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    }

}
