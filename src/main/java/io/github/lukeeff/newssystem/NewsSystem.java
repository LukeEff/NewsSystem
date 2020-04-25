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


public class NewsSystem extends JavaPlugin {

    @Getter private SQLManager sqlManager;
    @Getter private DatabaseUtil databaseUtil;
    @Getter private ConfigManager configManager;
    @Getter private BroadcastUtil broadcastUtil;
    @Getter private ConfigUtil configUtil;
    @Getter private News newsSubCommands;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configUtil = new ConfigUtil(this, getConfigManager().getConfig());
        sqlManager = new SQLManager(this); //Initialize. Instance reference stored in main class
        databaseUtil = new DatabaseUtil(this); //Initialize. Instance reference stored in main class
        broadcastUtil = new BroadcastUtil(this);
        newsSubCommands = new News(this);

        broadcastUtil.setScheduleNewsSwitch(true); //Replace with some configurable value
        broadcastUtil.beginBroadcast();
        getCommand("news").setExecutor(newsSubCommands);
        getCommand("togglenews").setExecutor(new ToggleNews(this));
        registerSubCommands();

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    }

    @Override
    public void onDisable() {
        broadcastUtil.setScheduleNewsSwitch(false);
    }

    /**
     * Registers sub commands for the news command
     */
    private void registerSubCommands() {
        newsSubCommands.registerNewsSubCommands("remove", new RemoveNews());
        newsSubCommands.registerNewsSubCommands("add", new AddNews());
    }

}
