package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Utility class for the database. Primary function
 * to get, set, check, add, and modify data from the database.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class DatabaseUtil {

    //Table name
    @Getter private static final String TABLE_NAME = "player_data";
    //Column names
    @Getter private static final String COL_PRIMARY = "UUID";
    @Getter private static final String COL_SECONDARY = "RECEIVENEWS";
    //Instances
    @Getter private final NewsSystem plugin;
    @Getter @Setter private Connection connection;

    /**
     * Constructor for DatabaseUtil.
     * @param instance instance of NewsSystem.
     */
    public DatabaseUtil(@NonNull final NewsSystem instance) {
        this.plugin = instance;
        setConnection(getPlugin().getSqlManager().getConnection());
    }

    /**
     * Adds a brand new record to the database.
     *
     * The record consists of a primary key
     * that is the value of the UUID of a target
     * player. The secondary column contains a boolean
     * value that determines if a player will see the
     * news feed.
     *
     * @param playerID the UUID of the player.
     */
    public void addPlayerToDatabase(@NonNull final UUID playerID) {
        final String insertData = "INSERT INTO " + getTABLE_NAME() + " ("
                + getCOL_PRIMARY() + "," + getCOL_SECONDARY() + ")\nVALUES "
                + "(?,?);";
        final String canGetNews = "true";
        try {
            PreparedStatement addPlayer = getConnection().prepareStatement(insertData);
            addPlayer.setString(1, playerID.toString());
            addPlayer.setString(2, canGetNews);
            addPlayer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modify and update player data from the database.
     *
     * @param playerID the uuid associated with the player.
     * @param column the column with data being modified.
     * @param newValue the new value for the column in respect to the player.
     * @throws SQLException thrown with sql syntax error.
     */
    private void setPlayerData(@NonNull final UUID playerID, final @NonNull String column, final @Nullable String newValue) throws SQLException {
        final String update = "UPDATE " + getTABLE_NAME() + "\nSET "  + column + " = ?\nWHERE " + getCOL_PRIMARY() + " = ?;";
        final PreparedStatement statement = getConnection().prepareStatement(update);
        statement.setString(1, newValue);
        statement.setString(2, playerID.toString());
        statement.executeUpdate();
    }

    /**
     * Get data from the database from a specified column and specified key.
     *
     * @param whereColumnKey the key for some condition.
     * @param fromColumn the column(s) that will be returned.
     * @param whereColumn the column condition for key.
     * @return the object found in the database. Returns null if no object found.
     * @throws SQLException thrown in respect to improper syntax.
     */
    private String getPlayerData(@NonNull final String whereColumnKey, @NonNull final String fromColumn, @NonNull final String whereColumn) throws SQLException {
        final String selectSyntax = "SELECT " + fromColumn + " FROM " + getTABLE_NAME() + " WHERE " + whereColumn + " = ?";
        final PreparedStatement statement = getConnection().prepareStatement(selectSyntax);
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
     * Checks if a player can see a news feed.
     *
     * @param playerID the target player in the database.
     * @return true when a player can see the news feed.
     */
    boolean canSeeNews(@NonNull final UUID playerID) {
        try {
            final String databaseTrue = "true";
            return getPlayerData(playerID.toString(), getCOL_SECONDARY(), getCOL_PRIMARY()).equalsIgnoreCase(databaseTrue);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            return false; //Will only happen if player is not in the database and gets checked anyways.
        }
    }

    /**
     * Sets the News column to true or false.
     *
     * When true, a player will see the news
     * feed.
     * When false, the target player will
     * not see the news feed.
     *
     * @param playerID the target row.
     * @param seeNews the value to override the current value.
     */
    private void setSeeNews(@NonNull final UUID playerID, @NonNull final Boolean seeNews) {
        try {
            setPlayerData(playerID, getCOL_SECONDARY(), seeNews.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enables/Disables the news message for a target member
     * in the database.
     *
     * @param playerID the target player UUID in the database.
     * @return true if being set to true, false if being set to
     * false.
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
     * Checks for a UUID primary key inside of the database.
     *
     * @param playerID the uuid of a player as a String.
     * @return true when the player is in the database.
     */
    public boolean playerExists(@NonNull UUID playerID) {
        try {
            return (getPlayerData(playerID.toString(), getCOL_PRIMARY(), getCOL_PRIMARY()) != null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}
