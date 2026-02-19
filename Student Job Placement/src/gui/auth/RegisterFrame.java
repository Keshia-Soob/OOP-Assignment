package gui.auth;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class RegisterFrame extends JFrame {

    private JTextField txtFullName, txtCgpa;
    private JComboBox<String> comboCourse, comboBranch, comboSection;
    private JPasswordField txtPassword;
    private JButton btnRegister;

    public RegisterFrame() {

        setTitle("Placement & Job Recruitment System");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 248, 252));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
    }

    // ✅ MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new RegisterFrame().setVisible(true);
        });
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

    // ================= CENTER AREA =================
    private JComponent buildCenter() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 248, 252));

        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(650, 430));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 230, 240), 1, true),
                new EmptyBorder(20, 30, 20, 30)
        ));

        // ===== Title =====
        JLabel lblHeading = new JLabel("Student Registration");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeading.setBounds(200, 10, 300, 30);
        card.add(lblHeading);

        // ===== Full Name =====
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setBounds(50, 60, 100, 20);
        card.add(lblFullName);

        txtFullName = new JTextField();
        txtFullName.setBounds(50, 80, 550, 35);
        styleField(txtFullName);
        card.add(txtFullName);

        // ===== Course =====
        JLabel lblCourse = new JLabel("Course");
        lblCourse.setBounds(50, 125, 100, 20);
        card.add(lblCourse);

        comboCourse = new JComboBox<>(new String[]{
                "Computer Science",
                "Business",
                "Engineering"
        });
        comboCourse.setBounds(50, 145, 260, 35);
        styleField(comboCourse);
        card.add(comboCourse);

        // ===== Branch =====
        JLabel lblBranch = new JLabel("Branch");
        lblBranch.setBounds(340, 125, 100, 20);
        card.add(lblBranch);

        comboBranch = new JComboBox<>(new String[]{
                "Computer Science",
                "Business",
                "Engineering"
        });
        comboBranch.setBounds(340, 145, 260, 35);
        styleField(comboBranch);
        card.add(comboBranch);

        // ===== Section =====
        JLabel lblSection = new JLabel("Section");
        lblSection.setBounds(50, 190, 100, 20);
        card.add(lblSection);

        comboSection = new JComboBox<>(new String[]{"A", "B", "C"});
        comboSection.setBounds(50, 210, 260, 35);
        styleField(comboSection);
        card.add(comboSection);

        // ===== CGPA =====
        JLabel lblCgpa = new JLabel("CGPA");
        lblCgpa.setBounds(340, 190, 100, 20);
        card.add(lblCgpa);

        txtCgpa = new JTextField();
        txtCgpa.setBounds(340, 210, 260, 35);
        styleField(txtCgpa);
        card.add(txtCgpa);

        // ===== Password =====
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(50, 255, 100, 20);
        card.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 275, 550, 35);
        styleField(txtPassword);
        card.add(txtPassword);

        // ===== Register Button =====
        btnRegister = new JButton("Register");
        btnRegister.setBounds(50, 330, 550, 45);

        Color normalColor = new Color(58, 102, 171);
        Color hoverColor  = new Color(48, 90, 155);

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

        // ===== Login Link (UPDATED) =====
        JLabel lblLogin = new JLabel("<html>Already have an account? <u>Login here</u></html>");
        lblLogin.setForeground(new Color(58, 102, 171));
        lblLogin.setBounds(210, 385, 300, 25);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                // Open LoginFrame
                SwingUtilities.invokeLater(() -> {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ignored) {}
                    new LoginFrame().setVisible(true);
                });

                dispose(); // close RegisterFrame
            }
        });

        card.add(lblLogin);

        centerPanel.add(card);
        return centerPanel;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new LineBorder(new Color(200, 210, 225), 1, true));
    }

    private void registerStudent() {
        String name = txtFullName.getText();
        String cgpa = txtCgpa.getText();
        String password = new String(txtPassword.getPassword());

        if (name.isEmpty() || cgpa.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        JOptionPane.showMessageDialog(this, "Registration Successful!");
    }
}
