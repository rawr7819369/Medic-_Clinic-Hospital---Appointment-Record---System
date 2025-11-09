package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection utility class for managing database connections.
 * Implements singleton pattern to ensure single database connection instance.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mediconnect_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static Connection connection = null;
    
    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseConnection() {
    }
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(DB_URL + "?useSSL=false&serverTimezone=UTC", DB_USERNAME, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
            // Try to continue without database
            System.err.println("Continuing with in-memory storage only...");
            return null;
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            // Try to continue without database
            System.err.println("Continuing with in-memory storage only...");
            return null;
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
