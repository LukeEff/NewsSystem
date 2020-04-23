package io.github.lukeeff.newssytem.utils;

import io.github.lukeeff.newssytem.NewsSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    //Name for SQL table
    private static final String TABLENAME = "news";

    //Column names
    private static final String COLUMNPRIMARY = "ID";
    private static final String COLUMNSECONDARY = "NEWS";

    //Syntax
    private static final String VARCHAR = "varChar";

    //Statements
    private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS " + TABLENAME + "(\n";


    private NewsSystem plugin; //TODO convert to local variable if not needed global later on
    private Connection connection; //To avoid calling

    /**
     * Constructor for DatabaseUtil
     * @param instance instance of NewsSystem
     */
    public DatabaseUtil(NewsSystem instance) {
        plugin = instance;
        connection = plugin.sqlManager.getConnection();
    }

    /**
     * Creates a new table in the database
     * <p>For the purposes of this task, we
     * only will need one table</p>
     * @throws SQLException thrown when syntax is invalid
     */
    private void createTable() throws SQLException {
        Statement statement = connection.createStatement(); //Can't use PreparedStatement here.
        statement.executeUpdate(CREATETABLE);
    }




}
