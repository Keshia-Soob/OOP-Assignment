package service;

import dao.ApplicationDAO;
import model.Application;
import model.ApplicationHistory;

import java.util.List;

public class ApplicationService {

    // ── Result codes returned by apply() ─────────────────────────────────────
    public static final int APPLY_SUCCESS        = 0;
    public static final int APPLY_ALREADY_APPLIED = 1;
    public static final int APPLY_ALREADY_SELECTED = 2;   // one-offer rule
    public static final int APPLY_ERROR          = 3;

    /**
     * Attempt to submit an application.
     *
     * Enforces the one-offer rule: a student who is already Selected
     * cannot apply to any further job.
     *
     * @return one of the APPLY_* constants above
     */
    public static int apply(int userId, int jobId) {

        // ── One-offer rule: check BOTH on-campus and off-campus tables ───────
        if (service.OffCampusService.isAlreadySelected(userId)) {
            return APPLY_ALREADY_SELECTED;
        }

        boolean ok = ApplicationDAO.insertApplication(userId, jobId);

        if (ok)  return APPLY_SUCCESS;

        // insertApplication returns false for both duplicate-key and other errors.
        // Distinguish by checking whether a row already exists.
        boolean duplicate = ApplicationDAO.getApplicationsByUser(userId)
                .stream()
                .anyMatch(a -> a.getJobId() == jobId);

        return duplicate ? APPLY_ALREADY_APPLIED : APPLY_ERROR;
    }

    /**
     * Called by the Admin/Company layer when a student is marked as Selected.
     *
     * 1. Updates the application status to 'Selected' in the DB.
     * 2. Logs the change in application_history.
     * 3. Closes all other open applications for this student (one-offer rule).
     *
     * @param applicationId  the application being selected
     * @param userId         the student being placed
     * @return true on success
     */
    public static boolean recordSelection(int applicationId, int userId) {
        boolean ok = ApplicationDAO.updateStatus(applicationId, "Selected");
        if (ok) {
            ApplicationDAO.insertHistory(applicationId, "Selected");
            ApplicationDAO.closeAllOtherApplications(userId, applicationId);
        }
        return ok;
    }

    /**
     * Retrieve all applications belonging to the given user.
     */
    public static List<Application> getApplications(int userId) {
        return ApplicationDAO.getApplicationsByUser(userId);
    }

    /**
     * Withdraw an application — only allowed when status is still 'Applied'.
     * @return true on success, false if already progressed or not found.
     */
    public static boolean withdraw(int applicationId, int userId) {
        return ApplicationDAO.withdrawApplication(applicationId, userId);
    }

    /**
     * Return the full status timeline for a single application.
     */
    public static List<ApplicationHistory> getHistory(int applicationId) {
        return ApplicationDAO.getHistory(applicationId);
    }

    // ── Dashboard helpers ─────────────────────────────────────────────────────

    public static int countTotal(int userId) {
        return ApplicationDAO.countByUser(userId);
    }

    public static int countByStatus(int userId, String status) {
        return ApplicationDAO.countByUserAndStatus(userId, status);
    }

    public static List<String> getRecentActivity(int userId, int limit) {
        return ApplicationDAO.getRecentActivity(userId, limit);
    }
}