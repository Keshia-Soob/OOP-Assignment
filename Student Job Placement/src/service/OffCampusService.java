package service;

import dao.OffCampusDAO;
import model.OffCampusApplication;

import java.util.List;

public class OffCampusService {

    /* ====================== ADD ====================== */
    /**
     * Validates and inserts a new off-campus application.
     * Returns null on success, or an error message string on failure.
     */
    public static String add(int userId, String company, String jobTitle,
                              String status, String notes) {

        if (company == null || company.isBlank())
            return "Company name is required.";

        if (jobTitle == null || jobTitle.isBlank())
            return "Job title is required.";

        OffCampusApplication app = new OffCampusApplication(
                userId, company.trim(), jobTitle.trim(), status, notes.trim()
        );

        boolean ok = OffCampusDAO.insert(app);
        return ok ? null : "Failed to save. Please try again.";
    }

    /* ====================== GET (with optional status filter) ====================== */
    public static List<OffCampusApplication> getApplications(int userId, String statusFilter) {
        return OffCampusDAO.getByUserIdAndStatus(userId, statusFilter);
    }
}