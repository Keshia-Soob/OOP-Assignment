package gui.auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox<String> courseBox;
    private JComboBox<String> branchBox;
    private JComboBox<String> sectionBox;
    private JTextField cgpaField;
    private JPasswordField passwordField;

    private JLabel messageLabel;

    public RegisterFrame() {
        super("Student Registration");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 102, 160));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        header.setPreferredSize(new Dimension(100, 56));

        JLabel title = new JLabel("Student Registration", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JComponent buildForm() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Full Name
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        fullNameField = new JTextField();
        fullNameField.setPreferredSize(new Dimension(260, 30));
        form.add(fullNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(260, 30));
        form.add(emailField, gbc);

        // Course + Branch (two columns)
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Course:"), gbc);

        gbc.gridx = 1; gbc.gridy = y;
        courseBox = new JComboBox<>(new String[]{"B.Tech", "BSc IT", "Diploma"});
        courseBox.setPreferredSize(new Dimension(260, 30));
        form.add(courseBox, gbc);

        gbc.gridx = 2; gbc.gridy = y;
        form.add(new JLabel("Branch:"), gbc);

        gbc.gridx = 3; gbc.gridy = y++;
        branchBox = new JComboBox<>(new String[]{"Computer Science", "Software Eng", "Networking"});
        branchBox.setPreferredSize(new Dimension(260, 30));
        form.add(branchBox, gbc);

        // Section + CGPA
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Section:"), gbc);

        gbc.gridx = 1; gbc.gridy = y;
        sectionBox = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2"});
        sectionBox.setPreferredSize(new Dimension(260, 30));
        form.add(sectionBox, gbc);

        gbc.gridx = 2; gbc.gridy = y;
        form.add(new JLabel("CGPA:"), gbc);

        gbc.gridx = 3; gbc.gridy = y++;
        cgpaField = new JTextField();
        cgpaField.setPreferredSize(new Dimension(260, 30));
        form.add(cgpaField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = y++;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(260, 30));
        form.add(passwordField, gbc);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);

        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Back to Login");

        registerBtn.setPreferredSize(new Dimension(180, 34));
        loginBtn.setPreferredSize(new Dimension(140, 34));

        registerBtn.addActionListener(e -> handleRegister());
        loginBtn.addActionListener(e -> openLogin());

        btnRow.add(registerBtn);
        btnRow.add(loginBtn);

        // Message
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(180, 0, 0));

        root.add(form, BorderLayout.CENTER);
        root.add(btnRow, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(root, BorderLayout.CENTER);
        wrapper.add(messageLabel, BorderLayout.SOUTH);

        return wrapper;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(8, 12, 8, 12));
        footer.setBackground(new Color(245, 245, 245));

        JLabel info = new JLabel("Placement & Job Recruitment System");
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(new Color(90, 90, 90));

        footer.add(info, BorderLayout.WEST);
        return footer;
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String cgpaTxt = cgpaField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (fullName.isEmpty() || email.isEmpty() || cgpaTxt.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("Please fill in all required fields.");
            return;
        }

        double cgpa;
        try {
            cgpa = Double.parseDouble(cgpaTxt);
        } catch (NumberFormatException ex) {
            messageLabel.setText("CGPA must be a number.");
            return;
        }

        // TODO: Replace with DB INSERT via StudentController/StudentService/StudentDAO
        boolean saved = true; // demo

        if (saved) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            openLogin();
        } else {
            messageLabel.setText("Registration failed. Try again.");
        }
    }

    private void openLogin() {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        dispose();
    }
}