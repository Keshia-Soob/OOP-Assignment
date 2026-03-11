package dao;

import gui.auth.DB;
import model.OffCampusApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffCampusDAO {

    /* ====================== INSERT ====================== */
    public static boolean insert(OffCampusApplication app) {

        String sql = """
            INSERT INTO off_campus_applications
                (user_id, company, job_title, status, notes)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt   (1, app.getUserId());
            ps.setString(2, app.getCompany());
            ps.setString(3, app.getJobTitle());
            ps.setString(4, app.getStatus());
            ps.setString(5, app.getNotes());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ====================== DELETE ====================== */
    /**
     * Deletes an off-campus entry belonging to the given user.
     * The userId check ensures a student can only delete their own records.
     * Returns true if a row was actually deleted.
     */
    public static boolean delete(int offAppId, int userId) {
        String sql = """
            DELETE FROM off_campus_applications
            WHERE  off_app_id = ? AND user_id = ?
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, offAppId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ====================== IS SELECTED (one-offer rule) ====================== */
    /**
     * Returns true if this student already has a 'Selected' off-campus entry.
     * Used to enforce the one-offer rule across both on-campus and off-campus jobs.
     */
    public static boolean isSelected(int userId) {
        String sql = """
            SELECT COUNT(*) FROM off_campus_applications
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

    /* ====================== GET ALL BY USER ====================== */
    public static List<OffCampusApplication> getByUserId(int userId) {

        List<OffCampusApplication> list = new ArrayList<>();

        String sql = """
            SELECT off_app_id, user_id, company, job_title,
                   date_applied, status, notes
            FROM   off_campus_applications
            WHERE  user_id = ?
            ORDER  BY date_applied DESC
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new OffCampusApplication(
                        rs.getInt      ("off_app_id"),
                        rs.getInt      ("user_id"),
                        rs.getString   ("company"),
                        rs.getString   ("job_title"),
                        rs.getTimestamp("date_applied"),
                        rs.getString   ("status"),
                        rs.getString   ("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /* ====================== GET ALL BY USER + STATUS FILTER ====================== */
    public static List<OffCampusApplication> getByUserIdAndStatus(int userId, String status) {

        if (status == null || status.equals("All")) {
            return getByUserId(userId);
        }

        List<OffCampusApplication> list = new ArrayList<>();

        String sql = """
            SELECT off_app_id, user_id, company, job_title,
                   date_applied, status, notes
            FROM   off_campus_applications
            WHERE  user_id = ? AND status = ?
            ORDER  BY date_applied DESC
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt   (1, userId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new OffCampusApplication(
                        rs.getInt      ("off_app_id"),
                        rs.getInt      ("user_id"),
                        rs.getString   ("company"),
                        rs.getString   ("job_title"),
                        rs.getTimestamp("date_applied"),
                        rs.getString   ("status"),
                        rs.getString   ("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /* ====================== RECENT ACTIVITY (for dashboard) ====================== */
    /**
     * Returns the most recent N off-campus entries for this user,
     * formatted identically to ApplicationDAO.getRecentActivity() so the
     * dashboard can merge both lists and sort them together.
     *
     * Format: "yyyy-MM-dd  —  <jobTitle> @ <company>  [<status>]  {Off-Campus}"
     * The "{Off-Campus}" tag lets the dashboard label these rows distinctly.
     */
    public static List<String> getRecentActivity(int userId, int limit) {
        List<String> items = new ArrayList<>();

        String sql = """
            SELECT company, job_title, status, date_applied
            FROM   off_campus_applications
            WHERE  user_id = ?
            ORDER  BY date_applied DESC
            LIMIT  ?
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String date = rs.getTimestamp("date_applied")
                               .toString().substring(0, 10);
                items.add(String.format("%s  —  %s @ %s  [%s]  {Off-Campus}",
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

    /* ====================== COUNT BY USER + STATUS ====================== */
    public static int countByUserAndStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM off_campus_applications " +
                     "WHERE user_id = ? AND status = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt   (1, userId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* ====================== COUNT BY USER (for dashboard) ====================== */
    public static int countByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM off_campus_applications WHERE user_id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}