package io.github.lukeeff.newssytem;

import io.github.lukeeff.newssytem.managers.SQLManager;
import io.github.lukeeff.newssytem.utils.DatabaseUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class NewsSystem extends JavaPlugin {

    public SQLManager sqlManager; //Manager. Purpose to create and establish an initial connection to a database
    public DatabaseUtil databaseUtil; //How I plan to store/retrieve news data

    @Override
    public void onEnable() {
        sqlManager = new SQLManager(this); //Initialize. Instance reference stored in main class
        databaseUtil = new DatabaseUtil(this); //Initialize. Instance reference stored in main class
    }

    @Override
    public void onDisable() {

    }

}
