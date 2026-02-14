package app;
import gui.auth.LoginFrame;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
    }
}

//Its only job is to: Launch the first window (LoginFrame)
