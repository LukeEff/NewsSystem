package io.github.lukeeff.newssystem.managers;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

    /**
     * This is the SQLManager class I've built.
     * I'm not stoked with how it turned out, but
     * manipulating SQL syntax in an organized fashion
     * is challenging and I did not implement a library
     * to do the hard work for me.
     */

public class SQLManager {


    private NewsSystem plugin;
    private File databaseFolder;
    private final String DATABASENAME = "news"; //TODO configurable when config added
    private Connection connection;

    public SQLManager(NewsSystem instance) {
        this.plugin = instance;
        createDatabaseDirectory();
        establishDatabaseConnection();
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
     * Establishes a connection to the local SQLite database.
     * <p>The method will only need to be called once and
     * will create a new database file if one does not exist.
     * SQLException will be thrown when driver is not found
     * or syntax is invalid. ClassNotFound thrown when driver
     * class is not found. SQLite is a dependency in gradle.</p>
     */
    private void establishDatabaseConnection() {
        try {
            final String databaseFilePath = getDatabaseFilePath();
            final String driver = "jdbc:sqlite:";
            plugin.getLogger().info(driver + databaseFilePath);
            Class.forName("org.sqlite.JDBC"); //Required for initial connection.
            this.connection = DriverManager.getConnection(driver + databaseFilePath);
            createTable();
        } catch (SQLException | ClassNotFoundException syntaxException) {
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

    /**
     * Creates a new table in the database
     * <p>For the purposes of this task, we
     * only will need one table. In real life
     * application, a library would be used to avoid
     * code duplication or I'd build one myself</p>
     * @throws SQLException thrown when syntax is invalid
     */
    private void createTable() throws SQLException {
        final String TABLENAME = DatabaseUtil.getTablename();
        final String PRIMARYCOl = DatabaseUtil.getColprimary() + " varChar PRIMARY KEY, \n";
        final String SECONDCOL = DatabaseUtil.getColsecondary() + " varChar NOT NULL";
        final String ENDCOL = "\n);";

        String CREATETABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLENAME + "(\n" + PRIMARYCOl + SECONDCOL + ENDCOL;

        Statement statement = connection.createStatement(); //Can't use PreparedStatement here.
        statement.executeUpdate(CREATETABLE);
    }



}
