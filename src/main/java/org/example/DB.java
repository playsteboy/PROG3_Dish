package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:postgresql://localhost:5432/video_game_store";
    private static final String USER = "postgres";
    private static final String PASSWORD = "anjamikaiah";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("connection failed : " + e.getMessage());
            return null;
        }
    }
}
