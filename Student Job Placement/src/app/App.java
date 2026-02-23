package app;

import dao.DatabaseManager;
import gui.auth.LoginFrame;

import javax.swing.*;

public class App {

    private static void applyLookAndFeel() {
        try {
            // ✅ This is the "default Swing look" you likely liked when running frames individually
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            // Optional: nicer fonts on some systems (safe to keep)
            // UIManager.put("swing.boldMetal", Boolean.FALSE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // 1) Apply L&F ONCE here (GLOBAL)
        applyLookAndFeel();

        // 2) Initialize database
        DatabaseManager.initializeDatabase();

        // 3) Launch GUI
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}