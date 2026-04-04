package dao;

import gui.auth.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.ApplicationHistory;

public class ApplicationDAO {

    /**
     * Insert a new application row for the given user + job.
     * Returns true on success.
     * Returns false if the user already applied (UNIQUE constraint) or on DB error.
     */
    public static boolean insertApplication(int userId, int jobId) {
        String sql = "INSERT INTO applications (user_id, job_id, status) VALUES (?, ?, 'Applied')";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.executeUpdate();

            // Log the initial status in history
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                insertHistory(keys.getInt(1), "Applied");
            }

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
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

    /**
     * Withdraw (delete) an application only if its current status is 'Applied'.
     * Returns true on success, false if not found / already progressed / DB error.
     */
    public static boolean withdrawApplication(int applicationId, int userId) {
        String sql = """
            DELETE FROM applications
            WHERE  application_id = ?
              AND  user_id        = ?
              AND  status         = 'Applied'
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return the full status history for a given application, oldest first.
     */
    public static List<ApplicationHistory> getHistory(int applicationId) {
        List<ApplicationHistory> list = new ArrayList<>();

        String sql = """
            SELECT history_id, application_id, status, changed_at
            FROM   application_history
            WHERE  application_id = ?
            ORDER  BY changed_at ASC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ApplicationHistory(
                        rs.getInt      ("history_id"),
                        rs.getInt      ("application_id"),
                        rs.getString   ("status"),
                        rs.getTimestamp("changed_at")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Insert a history record. Called internally whenever status changes.
     */
    public static void insertHistory(int applicationId, String status) {
        String sql = "INSERT INTO application_history (application_id, status) VALUES (?, ?)";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt   (1, applicationId);
            ps.setString(2, status);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the status of a single application row.
     * Returns true if a row was actually changed.
     */
    public static boolean updateStatus(int applicationId, String newStatus) {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt   (2, applicationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── One-offer enforcement ─────────────────────────────────────────────────

    /**
     * Returns true if this user already holds a 'Selected' application.
     * Used to block new applications once a student has been placed.
     */
    public static boolean isSelected(int userId) {
        String sql = """
            SELECT COUNT(*) FROM applications
            WHERE  user_id = ? AND status = 'Selected'
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * When a student is Selected for one job, close every other open application
     * they have (status Applied or Shortlisted) so other students get a fair chance.
     * Also logs each closure in application_history.
     *
     * @param userId            the student being placed
     * @param keepApplicationId the application that just became Selected (left untouched)
     */
    public static void closeAllOtherApplications(int userId, int keepApplicationId) {

        // 1. Find all other open applications for this student
        String fetchSql = """
            SELECT application_id FROM applications
            WHERE  user_id = ?
              AND  application_id != ?
              AND  status IN ('Applied', 'Shortlisted')
            """;

        List<Integer> toClose = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(fetchSql)) {

            ps.setInt(1, userId);
            ps.setInt(2, keepApplicationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) toClose.add(rs.getInt("application_id"));

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (toClose.isEmpty()) return;

        // 2. Update them all to 'Closed'
        String updateSql = "UPDATE applications SET status = 'Closed' WHERE application_id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(updateSql)) {

            for (int appId : toClose) {
                ps.setInt(1, appId);
                ps.executeUpdate();
                insertHistory(appId, "Closed");   // audit trail
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── Dashboard stats ──────────────────────────────────────────────────────

    /** Total applications submitted by this user. */
    public static int countByUser(int userId) {
        return countByUserAndStatus(userId, null);
    }

    /** Applications for this user with a specific status (null = any). */
    public static int countByUserAndStatus(int userId, String status) {
        String sql = (status == null)
                ? "SELECT COUNT(*) FROM applications WHERE user_id = ?"
                : "SELECT COUNT(*) FROM applications WHERE user_id = ? AND status = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            if (status != null) ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Recent activity: last N status changes across all of this user's
     * applications, newest first.
     */
    public static List<String> getRecentActivity(int userId, int limit) {
        List<String> items = new ArrayList<>();

        String sql = """
            SELECT j.company, j.job_title, h.status, h.changed_at
            FROM   application_history h
            JOIN   applications a ON h.application_id = a.application_id
            JOIN   jobs         j ON a.job_id          = j.job_id
            WHERE  a.user_id = ?
            ORDER  BY h.changed_at DESC
            LIMIT  ?
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String date = rs.getTimestamp("changed_at")
                               .toString().substring(0, 10);
                items.add(String.format("%s  —  %s @ %s  [%s]",
                        date,
                        rs.getString("job_title"),
                        rs.getString("company"),
                        rs.getString("status")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

        /**
     * Find application_id for a given user and job.
     */
    public static int findApplicationId(int userId, int jobId) {
        String sql = """
            SELECT application_id
            FROM applications
            WHERE user_id = ? AND job_id = ?
            LIMIT 1
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("application_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Mark one application as Selected and log history.
     */
    public static boolean markAsSelected(int applicationId) {
        if (applicationId <= 0) return false;

        boolean updated = updateStatus(applicationId, "Selected");
        if (updated) {
            insertHistory(applicationId, "Selected");
        }
        return updated;
    }
}