package service;

import dao.UserDAO;
import model.User;
import util.Session;

/**
 * Service layer for profile-related operations.
 * Keeps GUI code clean and business logic centralised.
 */
public class ProfileService {

    /**
     * Validates and saves updated profile info.
     * On success, refreshes the Session with a fresh copy from the DB.
     *
     * @return null on success, or an error message string.
     */
    public static String updateProfile(String fullName,
                                       String address, String contactNumber,
                                       String course, String faculty,
                                       String cgpaText, String level,
                                       String ageText) {

        // ---- Validation ----
        if (fullName == null || fullName.isBlank())
            return "Full name is required.";

        double cgpa;
        try {
            cgpa = Double.parseDouble(cgpaText);
        } catch (NumberFormatException e) {
            return "CGPA must be a number (e.g. 3.25).";
        }
        if (cgpa < 0 || cgpa > 10)
            return "CGPA must be between 0 and 10.";

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return "Age must be a whole number.";
        }
        if (age < 15 || age > 80)
            return "Please enter a valid age (15-80).";

        int userId = Session.getUserId();

        boolean ok = UserDAO.updateProfile(userId, fullName, address,
                contactNumber, course, faculty, cgpa, level, age);

        if (!ok) return "Update failed. The email may already be in use.";

        // Refresh session with updated data
        User refreshed = UserDAO.findById(userId);
        if (refreshed != null) Session.login(refreshed);

        return null; // success
    }

    /**
     * Validates and changes the password.
     *
     * @return null on success, or an error message string.
     */
    public static String changePassword(String currentPassword,
                                        String newPassword,
                                        String confirmPassword) {

        if (currentPassword == null || currentPassword.isBlank())
            return "Please enter your current password.";

        if (newPassword == null || newPassword.isBlank())
            return "Please enter a new password.";

        if (newPassword.length() < 6)
            return "New password must be at least 6 characters.";

        if (!newPassword.equals(confirmPassword))
            return "New password and confirmation do not match.";

        if (newPassword.equals(currentPassword))
            return "New password must be different from the current password.";

        return UserDAO.changePassword(Session.getUserId(), currentPassword, newPassword);
    }
}