package dao;

import gui.auth.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Job;

public class JobOfferDAO {

    /**
     * Returns all PENDING job offers for a given user.
     */
    public static List<Job> getPendingOffersByUser(int userId) {
        List<Job> offers = new ArrayList<>();

        String sql = """
            SELECT j.job_id, j.company, j.job_title, j.location, j.min_cgpa, j.salary,
                   j.description, j.key_responsibilities, j.duration,
                   j.start_date, j.contact_email, j.contact_number
            FROM job_offers jo
            JOIN jobs j ON jo.job_id = j.job_id
            WHERE jo.user_id = ?
              AND jo.status = 'Pending'
            ORDER BY jo.offer_id DESC
        """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Job job = new Job(
                        rs.getInt("job_id"),
                        rs.getString("company"),
                        rs.getString("job_title"),
                        rs.getString("location"),
                        rs.getDouble("min_cgpa"),
                        rs.getDouble("salary"),
                        rs.getString("description"),
                        rs.getString("key_responsibilities"),
                        rs.getString("duration"),
                        rs.getDate("start_date"),
                        rs.getString("contact_email"),
                        rs.getString("contact_number")
                );
                offers.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offers;
    }

    /**
     * Returns true if the user already accepted an offer.
     */
    public static boolean hasAcceptedOffer(int userId) {
        String sql = """
            SELECT COUNT(*) 
            FROM job_offers
            WHERE user_id = ?
              AND status = 'Accepted'
        """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Mark a specific offer as accepted.
     */
    public static boolean acceptOffer(int userId, int jobId) {
        String sql = """
        UPDATE job_offers
        SET status = 'Accepted',
            responded_at = CURRENT_TIMESTAMP
        WHERE user_id = ?
          AND job_id = ?
          AND status = 'Pending'
    """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mark a specific offer as rejected.
     */
    public static boolean rejectOffer(int userId, int jobId) {
        String sql = """
        UPDATE job_offers
        SET status = 'Rejected',
            responded_at = CURRENT_TIMESTAMP
        WHERE user_id = ?
          AND job_id = ?
          AND status = 'Pending'
    """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reject all other pending offers for the same user after one offer is
     * accepted.
     */
    public static void rejectAllOtherOffers(int userId, int acceptedJobId) {
        String sql = """
            UPDATE job_offers
            SET status = 'Rejected'
            WHERE user_id = ?
              AND job_id <> ?
              AND status = 'Pending'
        """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, acceptedJobId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPendingOffers(int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM job_offers
        WHERE user_id = ?
          AND status = 'Pending'
    """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void seedOffersForUser(int userId, int limit) {

        // IMPORTANT: do nothing if user already has offers
        if (hasAcceptedOffer(userId) || hasPendingOffers(userId)) {
            return;
        }

        String sql = """
        INSERT INTO job_offers (user_id, job_id, status)
        SELECT ?, j.job_id, 'Pending'
        FROM jobs j
        WHERE j.job_id NOT IN (
            SELECT job_id FROM job_offers WHERE user_id = ?
        )
        ORDER BY RAND()
        LIMIT ?
    """;

        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, limit);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getRecentOfferActivity(int userId, int limit) {
    List<String> activity = new ArrayList<>();

    String sql = """
        SELECT j.job_title, j.company, jo.status, jo.responded_at
        FROM job_offers jo
        JOIN jobs j ON jo.job_id = j.job_id
        WHERE jo.user_id = ?
          AND jo.status IN ('Accepted', 'Rejected')
          AND jo.responded_at IS NOT NULL
        ORDER BY jo.responded_at DESC
        LIMIT ?
    """;

    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ps.setInt(2, limit);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String date = rs.getTimestamp("responded_at").toLocalDateTime().toLocalDate().toString();
            String jobTitle = rs.getString("job_title");
            String company = rs.getString("company");
            String status = rs.getString("status");

            activity.add(date + "  —  " + jobTitle + " @ " + company + "  [" + status + "]  {Job Offer}");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return activity;
}
}
