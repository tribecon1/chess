package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    private static final String sqlUserTable =
            """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(30) NOT NULL PRIMARY KEY,
              `password` varchar(72) NOT NULL,
              `email` varchar(45) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """;


    private static final String sqlAuthTable =
            """
            CREATE TABLE IF NOT EXISTS auth (
              `username` varchar(30) NOT NULL,
              `authToken` varchar(45) NOT NULL PRIMARY KEY
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """;


    private static final String sqlGameTable =
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameID` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
              `whiteUsername` varchar(30),
              `blackUsername` varchar(30),
              `gameName` varchar(30) NOT NULL UNIQUE,
              `game` text NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """;


    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        createTables();
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void createTables() throws DataAccessException{
        try (Connection conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlUserTable)){
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(sqlAuthTable)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(sqlGameTable)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void clearTable(String givenTable) throws DataAccessException {
        Connection conn = null;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "DROP TABLE " + String.format(givenTable);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }


    public static int getRowCount(String givenTable) throws DataAccessException {
        int numberOfEntries = 0;
        Connection conn = null;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "SELECT COUNT(*) FROM ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, givenTable);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    numberOfEntries = resultSet.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
        return numberOfEntries;
    }

}