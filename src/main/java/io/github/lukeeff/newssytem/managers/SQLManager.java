package io.github.lukeeff.newssytem.managers;

import io.github.lukeeff.newssytem.NewsSystem;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {


    private NewsSystem plugin;
    private File databaseFolder;
    private final String DATABASENAME = "news"; //TODO configurable when config added
    private Connection connection;

    public SQLManager(NewsSystem instance) {
        this.plugin = instance;
    }

    /**
     * Gets the path of the database file
     * <p>This is similar to getting the
     * connection of a mySQL database with
     * the key difference being a file path
     * instead of a URL</p>
     * @return the path of the database file
     */
    private String getDatabaseFilePath() {
        final String path = "\\" + DATABASENAME + ".db";
        return databaseFolder.getPath().concat(path);
    }

    /**
     * Create the database directory. Does not overwrite an existing directory.
     */
    public void createDatabaseDirectory() {
        databaseFolder = new File(plugin.getDataFolder(), "database");
        databaseFolder.mkdirs();
    }

    /**
     * Gets the plugins root data folder
     * @return NewsSystem's root folder
     */
    private File getDatabaseFolder() {
        return plugin.getDataFolder();
    }

    /**
     * Establishes a connection to the local SQLite database.
     * <p>The method will only need to be called once and
     * will create a new database file if one does not exist.</p>
     */
    private void establishDatabaseConnection() {
        try {
            final String databaseFilePath = getDatabaseFilePath();
            final String driver = "jdbc:sqlite:";
            this.connection = DriverManager.getConnection(driver + databaseFilePath);
        } catch (SQLException syntaxException) {
            syntaxException.printStackTrace();
        }
    }

    /**
     * Gets the established connection to the SQLite database.
     * @return a reference to the SQLite database Connection object
     */
    public Connection getConnection() {
        return this.connection;
    }

}
