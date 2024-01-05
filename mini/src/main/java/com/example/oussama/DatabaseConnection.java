package com.example.oussama;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/miniprojet";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connection successful!");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to establish connection.");
            return null;
        }
    }
}
