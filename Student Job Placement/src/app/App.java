package app;

import dao.DatabaseManager;
import gui.auth.LoginFrame;

public class App {

    public static void main(String[] args) {

        // ✅ Step 1: Initialize database (create tables if not exist)
        DatabaseManager.initializeDatabase();

        // ✅ Step 2: Launch GUI
        javax.swing.SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
    }
}

//Its only job is to: Launch the first window (LoginFrame)
