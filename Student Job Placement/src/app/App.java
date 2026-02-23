package app;

import dao.DatabaseManager;
import gui.auth.LoginFrame;

import javax.swing.*;

public class App {

    private static void applyLookAndFeel() {
        try {
            // ✅ CrossPlatform (Metal) - the one you liked when running frames individually
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // ✅ Step 1: Initialize database (create tables if not exist)
        DatabaseManager.initializeDatabase();

        // ✅ Step 2: Launch GUI
        javax.swing.SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
    }
}