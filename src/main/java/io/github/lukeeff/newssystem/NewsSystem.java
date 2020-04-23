package io.github.lukeeff.newssystem;

import io.github.lukeeff.newssystem.managers.ConfigManager;
import io.github.lukeeff.newssystem.managers.SQLManager;
import io.github.lukeeff.newssystem.utils.BroadcastUtil;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class NewsSystem extends JavaPlugin {

    public SQLManager sqlManager; //Manager. Purpose to create and establish an initial connection to a database
    public DatabaseUtil databaseUtil; //Store playerdata
    public ConfigManager configManager; //How I plan to store/retrieve news data

    @Override
    public void onEnable() {
        sqlManager = new SQLManager(this); //Initialize. Instance reference stored in main class
        databaseUtil = new DatabaseUtil(this); //Initialize. Instance reference stored in main class
        configManager = new ConfigManager(this);
        BroadcastUtil.beginBroadCast(this);

    }

    @Override
    public void onDisable() {

    }

}
