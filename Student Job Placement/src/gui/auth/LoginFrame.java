package gui.auth;

import gui.student.StudentDashboardFrame;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField     emailField;
    private JPasswordField passwordField;
    private JLabel         messageLabel;

    public LoginFrame() {
        super("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 248, 252));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ====================== HEADER ======================

    private JComponent buildHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(58, 102, 171));
        header.setPreferredSize(new Dimension(1000, 60));

        JLabel title = new JLabel("Placement & Job Recruitment System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title);

        return header;
    }

    // ====================== CENTER ======================

    private JComponent buildCenter() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 248, 252));

        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(650, 380));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(220, 230, 240), 1, true),
                new EmptyBorder(20, 30, 20, 30)
        ));

        // Heading
        JLabel heading = new JLabel("Login");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setBounds(280, 10, 200, 30);
        card.add(heading);

        // Email / Student ID label + field
        JLabel emailLbl = new JLabel("Email / Student ID");
        emailLbl.setBounds(50, 60, 200, 20);
        emailLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(emailLbl);

        emailField = new JTextField();
        emailField.setBounds(50, 80, 550, 35);
        styleField(emailField);
        card.add(emailField);

        // Password label + field
        JLabel passLbl = new JLabel("Password");
        passLbl.setBounds(50, 125, 200, 20);
        passLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 145, 550, 35);
        styleField(passwordField);
        // Allow pressing Enter to log in
        passwordField.addActionListener(e -> handleLogin());
        card.add(passwordField);

        // Login button
        JButton loginBtn = new JButton("Log In");
        loginBtn.setBounds(50, 205, 550, 45);

        Color normalColor = new Color(58, 102, 171);
        Color hoverColor  = new Color(48, 90, 155);

        loginBtn.setBackground(normalColor);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { loginBtn.setBackground(hoverColor); }
            @Override public void mouseExited (MouseEvent e) { loginBtn.setBackground(normalColor); }
        });

        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);

        // Error message label
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(180, 0, 0));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setBounds(50, 255, 550, 20);
        card.add(messageLabel);

        // Register link
        JLabel lblRegister = new JLabel("<html>Don't have an account? <u>Register</u></html>");
        lblRegister.setForeground(new Color(58, 102, 171));
        lblRegister.setBounds(225, 290, 300, 25);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openRegister(); }
        });
        card.add(lblRegister);

        centerPanel.add(card);
        return centerPanel;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new javax.swing.border.LineBorder(new Color(200, 210, 225), 1, true));
    }

    // ====================== FOOTER ======================

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(8, 12, 8, 12));
        footer.setBackground(new Color(245, 248, 252));

        JLabel info = new JLabel("Student Job Recruitment System");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(new Color(90, 90, 90));
        footer.add(info, BorderLayout.WEST);

        return footer;
    }

    // ====================== LOGIN LOGIC ======================

    private void handleLogin() {
        String identifier = emailField.getText().trim();
        String password   = new String(passwordField.getPassword());

        // Basic empty-field guard (AuthService also checks, but this gives instant UI feedback)
        if (identifier.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter your email / student ID and password.");
            return;
        }

        // Disable button while checking to prevent double-clicks
        messageLabel.setText("Checking credentials...");
        messageLabel.setForeground(new Color(80, 80, 80));

        String error = AuthService.login(identifier, password);

        if (error == null) {
            // Success — Session now holds the logged-in user
            SwingUtilities.invokeLater(() -> {
                new StudentDashboardFrame().setVisible(true);
                dispose();
            });
        } else {
            messageLabel.setForeground(new Color(180, 0, 0));
            messageLabel.setText(error);
            passwordField.setText("");        // clear password on failure
            passwordField.requestFocus();
        }
    }

    // ====================== REGISTER NAV ======================

    private void openRegister() {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
        dispose();
    }
}