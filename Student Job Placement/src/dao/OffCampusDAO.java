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

        // "All" means no status filter
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
}
