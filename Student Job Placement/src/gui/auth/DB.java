package gui.auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL =
            "jdbc:mysql://localhost:3306/placement_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";        // or your MySQL user
    private static final String PASS = "mydatabase2026$";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
