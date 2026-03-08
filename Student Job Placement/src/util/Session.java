package util;

import model.User;

/**
 * Holds the currently logged-in user for the lifetime of the session.
 * Set once on successful login; cleared on logout.
 */
public class Session {

    private static User currentUser = null;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}