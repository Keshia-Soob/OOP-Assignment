package dao;

import gui.auth.DB;
import model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    /**
     * Insert a new application row for the given user + job.
     * Returns true on success.
     * Returns false if the user already applied (UNIQUE constraint) or on DB error.
     */
    public static boolean insertApplication(int userId, int jobId) {
        String sql = "INSERT INTO applications (user_id, job_id, status) VALUES (?, ?, 'Applied')";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate key – user already applied to this job
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return all applications for the given user,
     * joined with jobs to get company + job title.
     */
    public static List<Application> getApplicationsByUser(int userId) {
        List<Application> list = new ArrayList<>();

        String sql = """
            SELECT a.application_id, a.user_id, a.job_id,
                   j.company, j.job_title,
                   a.date_applied, a.status
            FROM   applications a
            JOIN   jobs j ON a.job_id = j.job_id
            WHERE  a.user_id = ?
            ORDER  BY a.date_applied DESC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application app = new Application();
                app.setApplicationId(rs.getInt("application_id"));
                app.setUserId       (rs.getInt("user_id"));
                app.setJobId        (rs.getInt("job_id"));
                app.setCompany      (rs.getString("company"));
                app.setJobTitle     (rs.getString("job_title"));
                app.setDateApplied  (rs.getTimestamp("date_applied"));
                app.setStatus       (rs.getString("status"));
                list.add(app);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}