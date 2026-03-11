package service;

import dao.ApplicationDAO;
import dao.OffCampusDAO;
import model.OffCampusApplication;

import java.util.List;

public class OffCampusService {

    /* ====================== ADD (with one-offer rule) ====================== */
    /**
     * Validates and inserts a new off-campus application.
     *
     * Enforces the one-offer rule: if the student is already Selected
     * in either on-campus applications OR off-campus applications,
     * the add is rejected.
     *
     * Returns null on success, or an error message string on failure.
     */
    public static String add(int userId, String company, String jobTitle,
                              String status, String notes) {

        if (company == null || company.isBlank())
            return "Company name is required.";

        if (jobTitle == null || jobTitle.isBlank())
            return "Job title is required.";

        // ── One-offer rule: block if already placed anywhere ──────────────────
        if (isAlreadySelected(userId)) {
            return "You have already been selected for a placement.\n" +
                   "The system allows only one active placement per student.";
        }

        OffCampusApplication app = new OffCampusApplication(
                userId, company.trim(), jobTitle.trim(), status,
                notes == null ? "" : notes.trim()
        );

        boolean ok = OffCampusDAO.insert(app);
        return ok ? null : "Failed to save. Please try again.";
    }

    /* ====================== DELETE ====================== */
    /**
     * Deletes the off-campus entry identified by offAppId.
     * The userId is passed to ensure ownership — students can only
     * delete their own records.
     *
     * Returns null on success, or an error message string on failure.
     */
    public static String delete(int offAppId, int userId) {
        boolean ok = OffCampusDAO.delete(offAppId, userId);
        return ok ? null : "Could not delete the entry. Please try again.";
    }

    /* ====================== GET (with optional status filter) ====================== */
    public static List<OffCampusApplication> getApplications(int userId, String statusFilter) {
        return OffCampusDAO.getByUserIdAndStatus(userId, statusFilter);
    }

    /* ====================== COUNT (for dashboard stat card) ====================== */
    public static int countByUser(int userId) {
        return OffCampusDAO.countByUser(userId);
    }

    /* ====================== COUNT SELECTED (for dashboard stat card) ====================== */
    /**
     * Returns the number of off-campus entries with status 'Selected' for this user.
     */
    public static int countSelected(int userId) {
        return dao.OffCampusDAO.countByUserAndStatus(userId, "Selected");
    }

    /* ====================== COUNT BY STATUS (for dashboard stat cards) ====================== */
    /**
     * Returns the number of off-campus entries matching the given status for this user.
     * e.g. countByStatus(userId, "Shortlisted")
     */
    public static int countByStatus(int userId, String status) {
        return dao.OffCampusDAO.countByUserAndStatus(userId, status);
    }

    /* ====================== RECENT ACTIVITY (for dashboard feed) ====================== */
    public static List<String> getRecentActivity(int userId, int limit) {
        return dao.OffCampusDAO.getRecentActivity(userId, limit);
    }

    /* ====================== ONE-OFFER RULE HELPER ====================== */
    /**
     * Returns true if this student is already Selected in EITHER
     * on-campus applications or off-campus applications.
     *
     * This is the central cross-table check for the one-offer rule.
     * Called by both OffCampusService.add() and ApplicationService.apply().
     */
    public static boolean isAlreadySelected(int userId) {
        return ApplicationDAO.isSelected(userId)   // on-campus Selected
            || OffCampusDAO.isSelected(userId);    // off-campus Selected
    }
}