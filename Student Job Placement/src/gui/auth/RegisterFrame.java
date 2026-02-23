package gui.auth;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;

public class RegisterFrame extends JFrame {

    private JTextField txtFullName, txtEmail, txtAddress, txtStudentId, txtContact, txtCgpa, txtAge;
    private JComboBox<String> comboCourse, comboFaculty, comboLevel;
    private JPasswordField txtPassword;
    private JButton btnRegister;

    public RegisterFrame() {

        setTitle("Placement & Job Recruitment System");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 248, 252));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
    }

    // ================= HEADER =================
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

    // ================= CENTER AREA (SIMPLE LAYOUT) =================
    private JComponent buildCenter() {

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 248, 252));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(760, 540));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 230, 240), 1, true),
                new EmptyBorder(20, 30, 20, 30)
        ));

        JLabel heading = new JLabel("Student Registration");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(heading);
        card.add(Box.createVerticalStrut(18));

        // ---- Row 1: Full Name | Email ----
        txtFullName = new JTextField();
        txtEmail = new JTextField();
        card.add(twoFieldRow("Full Name *", txtFullName, "Email *", txtEmail));

        // ---- Row 2: Student ID | Contact ----
        txtStudentId = new JTextField();
        txtContact = new JTextField();
        card.add(twoFieldRow("Student ID *", txtStudentId, "Contact Number", txtContact));

        // ---- Row 3: Address (wide) ----
        txtAddress = new JTextField();
        card.add(singleFieldRow("Address", txtAddress));

        // ---- Row 4: Course | Faculty ----
        comboCourse = new JComboBox<>(new String[]{
            "Computer Science", "Applied Computing", "Cybersecurity", "Software Engineering", "Business & Management", "Law & Management"
        });
        comboFaculty = new JComboBox<>(new String[]{
            "FOICDT", "FLM", "FSSH", "FoE", "FoA"
        });
        card.add(twoFieldRow("Course", comboCourse, "Faculty", comboFaculty));

        // ---- Row 5: CGPA | Level ----
        txtCgpa = new JTextField();
        comboLevel = new JComboBox<>(new String[]{
            "Level 1", "Level 2", "Level 3", "Level 4", "Masters"
        });
        card.add(twoFieldRow("CGPA *", txtCgpa, "Level", comboLevel));

        // ---- Row 6: Age | Password ----
        txtAge = new JTextField();
        txtPassword = new JPasswordField();
        card.add(twoFieldRow("Age ", txtAge, "Password *", txtPassword));

        card.add(Box.createVerticalStrut(18));

        // ---- Register Button ----
        btnRegister = new JButton("Register");

        Color normalColor = new Color(58, 102, 171);
        Color hoverColor = new Color(48, 90, 155);

        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnRegister.setBackground(normalColor);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setOpaque(true);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(normalColor);
            }
        });

        btnRegister.addActionListener(e -> registerStudent());
        card.add(btnRegister);

        card.add(Box.createVerticalStrut(12));

        // ---- Login Link ----
        JLabel lblLogin = new JLabel("<html>Already have an account? <u>Login here</u></html>");
        lblLogin.setForeground(new Color(58, 102, 171));
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        loginPanel.setOpaque(false);
        loginPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // doesn't matter, panel is full width
        loginPanel.add(lblLogin);

        card.add(Box.createVerticalStrut(12));
        card.add(loginPanel);

        centerPanel.add(card);
        return centerPanel;
    }

    private JPanel twoFieldRow(String label1, JComponent field1, String label2, JComponent field2) {

        JPanel row = new JPanel(new GridLayout(2, 2, 15, 5));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setOpaque(false);

        JLabel l1 = new JLabel(label1);
        JLabel l2 = new JLabel(label2);
        styleLabel(l1);
        styleLabel(l2);

        row.add(l1);
        row.add(l2);
        row.add(styleField(field1));
        row.add(styleField(field2));

        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        return row;
    }

    private JPanel singleFieldRow(String label, JComponent field) {

        JPanel row = new JPanel(new GridLayout(2, 1, 5, 5));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setOpaque(false);

        JLabel l = new JLabel(label);
        styleLabel(l);

        row.add(l);
        row.add(styleField(field));

        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return row;
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(40, 40, 40));
    }

    private JComponent styleField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new LineBorder(new Color(200, 210, 225), 1, true));
        if (field instanceof JComboBox) {
            ((JComboBox<?>) field).setBackground(Color.WHITE);
        }
        return field;
    }

    // ================= DB Insert =================
    private void registerStudent() {

        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();
        String studentId = txtStudentId.getText().trim();
        String contact = txtContact.getText().trim();

        String course = (String) comboCourse.getSelectedItem();
        String faculty = (String) comboFaculty.getSelectedItem();

        String cgpaText = txtCgpa.getText().trim();
        String level = (String) comboLevel.getSelectedItem();

        String ageText = txtAge.getText().trim();
        String password = new String(txtPassword.getPassword());

        // ---- Validation ----
        if (fullName.isEmpty() || email.isEmpty() || studentId.isEmpty()
                || cgpaText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required (*) fields!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return;
        }

        double cgpa;
        try {
            cgpa = Double.parseDouble(cgpaText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "CGPA must be a number (e.g. 3.25)");
            return;
        }

        if (cgpa < 0 || cgpa > 10) {
            JOptionPane.showMessageDialog(this, "CGPA must be between 0 and 10.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a whole number (e.g. 20)");
            return;
        }

        if (age < 15 || age > 80) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.");
            return;
        }

        String passwordHash = PasswordUtil.sha256(password);

        // ---- Insert ----
        String sql = """
            INSERT INTO users
            (full_name, email, address, student_id, contact_number,
             course, faculty, cgpa, level, age, password_hash, role)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'STUDENT')
        """;

        try (java.sql.Connection conn = DB.getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, address);
            ps.setString(4, studentId);
            ps.setString(5, contact);
            ps.setString(6, course);
            ps.setString(7, faculty);
            ps.setDouble(8, cgpa);
            ps.setString(9, level);
            ps.setInt(10, age);
            ps.setString(11, passwordHash);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful ✅");
            new LoginFrame().setVisible(true);
            dispose();

        } catch (java.sql.SQLException e) {

            if (e.getErrorCode() == 1062) {  // MySQL duplicate entry error

                JOptionPane.showMessageDialog(this,
                        "This email or Student ID is already registered.\nPlease use a different one.",
                        "Duplicate Entry",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database error: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
