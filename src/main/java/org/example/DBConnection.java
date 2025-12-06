package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String url = "jdbc:postgresql://localhost:5432/product_management_db";
    private static String user = "product_manager_user";
    private static String password = "123456";

    public static Connection getDBConnection() throws SQLException {
            return DriverManager.getConnection(url, user, password);
    }
}
