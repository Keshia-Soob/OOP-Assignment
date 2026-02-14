package gui.auth;

import gui.student.ApplicationsFrame;   // temporary (until you have StudentDashboardFrame)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        super("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 360);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 102, 160));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        header.setPreferredSize(new Dimension(100, 56));

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(18, 26, 18, 26));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel emailLbl = new JLabel("Email / Username:");
        emailLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        center.add(emailLbl, gbc);

        gbc.gridx = 1;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(260, 30));
        center.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        center.add(passLbl, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(260, 30));
        center.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setOpaque(false);

        JButton loginBtn = new JButton("Log In");
        JButton registerBtn = new JButton("Register");

        loginBtn.setPreferredSize(new Dimension(120, 32));
        registerBtn.setPreferredSize(new Dimension(120, 32));

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> openRegister());

        btnRow.add(loginBtn);
        btnRow.add(registerBtn);

        center.add(btnRow, gbc);

        gbc.gridy++;
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(180, 0, 0));
        center.add(messageLabel, gbc);

        gbc.gridy++;
        JLabel hint = new JLabel("Don't have an account? Click Register.", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(new Color(90, 90, 90));
        center.add(hint, gbc);

        return center;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(8, 12, 8, 12));
        footer.setBackground(new Color(245, 245, 245));

        JLabel info = new JLabel("Student Job Recruitment System");
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(new Color(90, 90, 90));

        footer.add(info, BorderLayout.WEST);
        return footer;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("Please enter email and password.");
            return;
        }

        // TODO: Replace this with your AuthController/AuthService DB check
        boolean success = true; // demo

        if (success) {
            // open main system (full sidebar screens)
            SwingUtilities.invokeLater(() -> new ApplicationsFrame().setVisible(true));
            dispose();
        } else {
            messageLabel.setText("Invalid login details.");
        }
    }

    private void openRegister() {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
        dispose();
    }
}