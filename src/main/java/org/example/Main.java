package org.example;
import java.sql.Connection;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try (Connection conn = DB.getConnection()) {
            if (conn != null) {
                System.out.println("Connection successful !");
            } else {
                System.out.println("Connection failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}