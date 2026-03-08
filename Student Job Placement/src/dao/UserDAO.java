package dao;

import gui.auth.DB;
import gui.auth.PasswordUtil;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Looks up a user by email OR student_id and checks the password hash.
     * Returns the User object on success, or null if not found / wrong password.
     */
    public static User findByCredentials(String emailOrStudentId, String plainPassword) {

        String passwordHash = PasswordUtil.sha256(plainPassword);

        String sql = """
            SELECT user_id, full_name, email, address, student_id,
                   contact_number, course, faculty, cgpa, level, age,
                   password_hash, role
            FROM   users
            WHERE  (email = ? OR student_id = ?)
              AND  password_hash = ?
            LIMIT  1
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emailOrStudentId);
            ps.setString(2, emailOrStudentId);
            ps.setString(3, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("student_id"),
                        rs.getString("contact_number"),
                        rs.getString("course"),
                        rs.getString("faculty"),
                        rs.getDouble("cgpa"),
                        rs.getString("level"),
                        rs.getInt("age"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // not found or wrong password
    }
}
