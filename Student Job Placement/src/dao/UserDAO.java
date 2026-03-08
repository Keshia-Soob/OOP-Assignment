package dao;

import gui.auth.DB;
import gui.auth.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

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

    /**
     * Fetches a fresh copy of the user from the DB by user_id.
     * Used after a profile update to refresh the Session object.
     */
    public static User findById(int userId) {

        String sql = """
            SELECT user_id, full_name, email, address, student_id,
                   contact_number, course, faculty, cgpa, level, age,
                   password_hash, role
            FROM   users
            WHERE  user_id = ?
            LIMIT  1
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the editable profile fields for the given user.
     * Returns true on success, false on duplicate email/student_id or DB error.
     */
    public static boolean updateProfile(int userId, String fullName,
                                        String address, String contactNumber,
                                        String course, String faculty,
                                        double cgpa, String level, int age) {

        String sql = """
            UPDATE users
            SET    full_name = ?, address = ?,
                   contact_number = ?, course = ?, faculty = ?,
                   cgpa = ?, level = ?, age = ?
            WHERE  user_id = ?
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, address);
            ps.setString(3, contactNumber);
            ps.setString(4, course);
            ps.setString(5, faculty);
            ps.setDouble(6, cgpa);
            ps.setString(7, level);
            ps.setInt(8, age);
            ps.setInt(9, userId);

            ps.executeUpdate();
            return true;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // duplicate email
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies the current password then updates it to the new hash.
     * Returns null on success, or an error message string on failure.
     */
    public static String changePassword(int userId, String currentPlain, String newPlain) {

        // 1) Verify current password
        String checkSql = "SELECT password_hash FROM users WHERE user_id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, userId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) return "User not found.";

            String storedHash = rs.getString("password_hash");
            String currentHash = PasswordUtil.sha256(currentPlain);

            if (!storedHash.equals(currentHash)) {
                return "Current password is incorrect.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while verifying password.";
        }

        // 2) Update to new hash
        String newHash = PasswordUtil.sha256(newPlain);
        String updateSql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(updateSql)) {

            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return null; // success

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while updating password.";
        }
    }

    // ---- private helper ----

    private static User mapRow(ResultSet rs) throws SQLException {
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
}
