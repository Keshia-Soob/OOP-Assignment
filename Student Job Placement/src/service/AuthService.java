package service;

import dao.UserDAO;
import model.User;
import util.Session;

public class AuthService {

    /**
     * Attempts to log in with the given credentials.
     * On success: stores the user in Session and returns null.
     * On failure: returns an error message string.
     */
    public static String login(String emailOrStudentId, String plainPassword) {

        if (emailOrStudentId == null || emailOrStudentId.isBlank())
            return "Please enter your email or student ID.";

        if (plainPassword == null || plainPassword.isBlank())
            return "Please enter your password.";

        User user = UserDAO.findByCredentials(emailOrStudentId.trim(), plainPassword);

        if (user == null)
            return "Invalid email / student ID or password.";

        // Store in session for the rest of the app to use
        Session.login(user);
        return null; // null = success
    }

    /** Clears the session. Call on logout. */
    public static void logout() {
        Session.logout();
    }
}