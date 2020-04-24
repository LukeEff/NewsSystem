package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DatabaseUtil {

    //Name for SQL table
    //private static final String TABLENAME = "news";
    private static final String TABLENAME = "player_data";

    //Column names
    private static final String COLPRIMARY = "UUID";
    private static final String COLSECONDARY = "RECEIVENEWS";

    //Syntax
    private static final String VARCHAR = "varChar";

    //Statements



    private NewsSystem plugin; //TODO convert to local variable if not needed global later on
    private static Connection connection; //To avoid calling

    /**
     * Constructor for DatabaseUtil
     * @param instance instance of NewsSystem
     */
    public DatabaseUtil(NewsSystem instance) {
        plugin = instance;
        connection = plugin.sqlManager.getConnection();
    }

    /**
     * Adds a brand new record to the database
     * <p>The record consists of a primary key
     * that is the value of the UUID of a target
     * player. The secondary column contains a boolean
     * value that determines if a player will see the
     * news feed</p>
     * @param UUID
     */
    public static void addPlayerToDatabase(final String UUID) {
        final String INSERTDATA = "INSERT INTO " + TABLENAME + " ("
                + COLPRIMARY + "," + COLSECONDARY + ")\nVALUES "
                + "(?,?);";
        String canGetNews = "true"; //1 means can get news

        try {
            PreparedStatement addPlayer = connection.prepareStatement(INSERTDATA);
            addPlayer.setString(1, UUID);
            addPlayer.setString(2, canGetNews);
            addPlayer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modify and update player data from the database
     * @param uuid the uuid associated with the player
     * @param column the column with data being modified
     * @param newValue the new value for the column in respect to the player
     * @throws SQLException thrown with sql syntax error
     */
    private static void setPlayerData(String uuid, String column, String newValue) throws SQLException {
        final String UPDATE = "UPDATE " + getTablename() + "\nSET "  + column + " = ?\nWHERE " + COLPRIMARY + " = ?;";
        PreparedStatement statement = connection.prepareStatement(UPDATE);
        statement.setString(1, newValue);
        statement.setString(2, uuid);
        statement.executeUpdate();
    }

    //TODO Return a list of the cases in which it's true
    /**
     * Get data from the database from a specified column and specified key
     * @param whereColumnKey the key for some condition
     * @param fromColumn the column(s) that will be returned
     * @param whereColumn the column condition for key
     * @return the object found in the database. Returns null if no object found
     * @throws SQLException thrown in respect to improper syntax
     */
    private static String getPlayerData(String whereColumnKey, String fromColumn, String whereColumn) throws SQLException {
        final String SELECTSYNTAX = "SELECT " + fromColumn + " FROM " + getTablename() + " WHERE " + whereColumn + " = ?";

        PreparedStatement statement = getConnection().prepareStatement(SELECTSYNTAX);
        statement.setString(1, whereColumnKey);
        ResultSet name = statement.executeQuery();
        name.next();
        try {
            return name.getString(fromColumn);
        } catch (SQLException closed) {
            return null;
        }

    }

    /**
     * Checks if a player can see a news feed
     * @param playerID the target player in the database
     * @return true when a player can see the news feed
     */
    public static boolean canSeeNews(String playerID) {
        try {
            return getPlayerData(playerID, getColsecondary(), getColprimary()).equalsIgnoreCase("true");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the News column to true or false.
     * <p>When true, a player will see the news
     * feed. When false, the target player will
     * not see the news feed</p>
     * @param playerID the target row
     * @param seeNews the value to override the current value
     */
    public static void setSeeNews(String playerID, Boolean seeNews) {
        try {
            setPlayerData(playerID, getColsecondary(), seeNews.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the name of the database table
     * @return the name of the database table
     */
    public static String getTablename() {
        return TABLENAME;
    }

    /**
     * Gets the name of the primary column
     * @return the name of the primary column
     */
    public static String getColprimary() {
        return COLPRIMARY;
    }

    /**
     * Gets the name of the secondary column
     * @return the name of the secondary column
     */
    public static String getColsecondary() {
        return COLSECONDARY;
    }

    /**
     * Gets the connection of the SQLite database
     * @return the connection reference to the database
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Enables/Disables the news message for a target member
     * in the database
     * @param playerID the target player UUID in the database
     */
    public static boolean toggleNews(String playerID) {
        if(canSeeNews(playerID)) {
            setSeeNews(playerID, false);
            return false;
        } else {
            setSeeNews(playerID, true);
            return true;
        }
    }

    /**
     * Checks for a UUID primary key inside of the database
     * @param playerID the uuid of a player as a String
     * @return true when the player is in the database
     */
    public static boolean playerExists(String playerID) {
        try {
            return (getPlayerData(playerID, getColprimary(), getColprimary()) != null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}
