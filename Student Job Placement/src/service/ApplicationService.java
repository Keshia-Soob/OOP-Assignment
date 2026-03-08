package service;

import dao.ApplicationDAO;
import model.Application;

import java.util.List;

public class ApplicationService {

    /**
     * Submit an application for the given user + job.
     * @return true on success, false if already applied or DB error.
     */
    public static boolean apply(int userId, int jobId) {
        return ApplicationDAO.insertApplication(userId, jobId);
    }

    /**
     * Retrieve all applications belonging to the given user.
     */
    public static List<Application> getApplications(int userId) {
        return ApplicationDAO.getApplicationsByUser(userId);
    }
}