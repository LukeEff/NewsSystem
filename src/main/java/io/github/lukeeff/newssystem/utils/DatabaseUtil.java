package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Utility class for the database. Primary function
 * to get, set, check, add, and modify data from the database
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class DatabaseUtil {

    //Table name
    @Getter private static final String TABLENAME = "player_data";
    //Column names
    @Getter private static final String COLPRIMARY = "UUID";
    @Getter private static final String COLSECONDARY = "RECEIVENEWS";
    //Instances
    @Getter private final NewsSystem plugin;
    @Getter private Connection connection;

    /**
     * Constructor for DatabaseUtil
     * @param instance instance of NewsSystem
     */
    public DatabaseUtil(@NonNull NewsSystem instance) {
        plugin = instance;
        connection = plugin.getSqlManager().getConnection();
    }

    /**
     * Adds a brand new record to the database
     * <p>The record consists of a primary key
     * that is the value of the UUID of a target
     * player. The secondary column contains a boolean
     * value that determines if a player will see the
     * news feed</p>
     * @param PLAYERID the UUID of the player
     */
    public void addPlayerToDatabase(@NonNull final UUID PLAYERID) {
        final String INSERTDATA = "INSERT INTO " + TABLENAME + " ("
                + COLPRIMARY + "," + COLSECONDARY + ")\nVALUES "
                + "(?,?);";
        final String canGetNews = "true";
        try {
            PreparedStatement addPlayer = connection.prepareStatement(INSERTDATA);
            addPlayer.setString(1, PLAYERID.toString());
            addPlayer.setString(2, canGetNews);
            addPlayer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modify and update player data from the database
     * @param PLAYERID the uuid associated with the player
     * @param column the column with data being modified
     * @param newValue the new value for the column in respect to the player
     * @throws SQLException thrown with sql syntax error
     */
    private void setPlayerData(@NonNull final UUID PLAYERID, final @NonNull String column, final @Nullable String newValue) throws SQLException {
        final String UPDATE = "UPDATE " + getTABLENAME() + "\nSET "  + column + " = ?\nWHERE " + COLPRIMARY + " = ?;";
        PreparedStatement statement = connection.prepareStatement(UPDATE);
        statement.setString(1, newValue);
        statement.setString(2, PLAYERID.toString());
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
    private String getPlayerData(@NonNull final String whereColumnKey, @NonNull final String fromColumn, @NonNull final String whereColumn) throws SQLException {
        final String SELECTSYNTAX = "SELECT " + fromColumn + " FROM " + getTABLENAME() + " WHERE " + whereColumn + " = ?";
        final PreparedStatement statement = getConnection().prepareStatement(SELECTSYNTAX);
        statement.setString(1, whereColumnKey);
        final ResultSet name = statement.executeQuery();
        name.next();
        try {
            return name.getString(fromColumn);
        } catch (SQLException closed) {
            return null; //Not in database
        }
    }

    /**
     * Checks if a player can see a news feed
     * @param playerID the target player in the database
     * @return true when a player can see the news feed.
     */
    boolean canSeeNews(@NonNull final UUID playerID) {
        try {
            final String DATABASETRUE = "true";
            return getPlayerData(playerID.toString(), getCOLSECONDARY(), getCOLPRIMARY()).equalsIgnoreCase(DATABASETRUE);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            return false; //Should only happen if player is not in the database and gets checked anyways.
        }
    }

    /**
     * Sets the News column to true or false.
     * <p>When true, a player will see the news
     * feed. When false, the target player will
     * not see the news feed</p>
     * @param PLAYERID the target row
     * @param seeNews the value to override the current value
     */
    private void setSeeNews(@NonNull final UUID PLAYERID, @NonNull final Boolean seeNews) {
        try {
            setPlayerData(PLAYERID, getCOLSECONDARY(), seeNews.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enables/Disables the news message for a target member
     * in the database
     * @param playerID the target player UUID in the database
     * @return true if being set to true, false if being set to
     * false
     */
    public boolean toggleNews(@NonNull final UUID playerID) {
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
     * @param PLAYERID the uuid of a player as a String
     * @return true when the player is in the database
     */
    public boolean playerExists(@NonNull UUID PLAYERID) {
        try {
            return (getPlayerData(PLAYERID.toString(), getCOLPRIMARY(), getCOLPRIMARY()) != null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}
