package io.github.lukeeff.newssystem.managers;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
     *
     * @author lukeeff
     * @since 4/25/2020
     */
public class SQLManager {


    @Getter @Setter private NewsSystem plugin;
    @Getter @Setter private File databaseFolder;
    @Getter @Setter private Connection connection;

    //Database file name
    @Getter private final String DATABASENAME = "news";


    /**
     * Constructor for SQLManager. Assigns field variable
     * values and establishes a connection to the database.
     * @param instance the instance of the NewsSystem class.
     */
    public SQLManager(@NonNull NewsSystem instance) {
        setPlugin(instance);
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
        final String path = "\\" + getDATABASENAME() + ".db";
        return getDatabaseFolder().getPath().concat(path);
    }

    /**
     * Create the database directory. Does not overwrite an existing directory.
     */
    private void createDatabaseDirectory() {
        final String DATABASEDIRECTORYNAME = "database"; //Hard coded since no need to change the name of the folder
        setDatabaseFolder(new File(getPlugin().getDataFolder(), DATABASEDIRECTORYNAME));
        getDatabaseFolder().mkdirs();
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
            final String DRIVER = "jdbc:sqlite:";
            Class.forName("org.sqlite.JDBC"); //Required for initial connection.
            this.connection = DriverManager.getConnection(DRIVER + getDatabaseFilePath());
            createTable();
        } catch (SQLException | ClassNotFoundException syntaxException) {
            syntaxException.printStackTrace();
        }
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
        @NonNull final String TABLENAME = DatabaseUtil.getTABLENAME();
        @NonNull final String PRIMARYCOl = DatabaseUtil.getCOLPRIMARY() + " varChar PRIMARY KEY, \n";
        @NonNull final String SECONDCOL = DatabaseUtil.getCOLSECONDARY() + " varChar NOT NULL";
        final String ENDCOL = "\n);";
        String CREATETABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLENAME + "(\n" + PRIMARYCOl + SECONDCOL + ENDCOL;

        Statement statement = connection.createStatement(); //Can't use PreparedStatement here.
        statement.executeUpdate(CREATETABLE);
    }



}
