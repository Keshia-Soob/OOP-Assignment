package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import model.Application;
import model.User;
import service.ApplicationService;
import service.ProfileService;
import util.Session;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Profile Page — displays user information, applied jobs,
 * and provides inline edit + password-change functionality.
 */
public class ProfileFrame extends BaseFrame {

    // ── Theme (reuses BaseFrame constants) ──────────────────────────────────
    private static final Color BG          = Color.WHITE;
    private static final Color ACCENT      = new Color(52, 102, 160);   // same as COLOR_PRIMARY
    private static final Color LIGHT_BG    = new Color(245, 248, 252);
    private static final Color BORDER_CLR  = new Color(210, 220, 235);
    private static final Color LABEL_CLR   = new Color(80, 90, 110);
    private static final Color GREEN_OK    = new Color(34, 139, 34);
    private static final Color RED_ERR     = new Color(180, 0, 0);

    // ── Profile-info edit fields ─────────────────────────────────────────────
    private JTextField  fFullName, fAddress, fContact, fCgpa, fAge;
    private JTextField  fStudentId, fEmail;   // read-only (disabled)
    private JComboBox<String> cbCourse, cbFaculty, cbLevel;
    private JLabel      profileFeedback;

    // ── Password-change fields ───────────────────────────────────────────────
    private JPasswordField fCurrentPw, fNewPw, fConfirmPw;
    private JLabel         pwFeedback;

    // ── Applications table ───────────────────────────────────────────────────
    private DefaultTableModel appsModel;

    public ProfileFrame() {
        super("Profile", SidebarPanel.NavItem.PROFILE);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  buildContent — called by BaseFrame constructor
    // ════════════════════════════════════════════════════════════════════════
    @Override
    protected JComponent buildContent() {

        // Master vertical scroll pane
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(6, 6, 6, 6));

        root.add(buildPageTitle());
        root.add(vGap(10));
        root.add(buildProfileCard());
        root.add(vGap(16));
        root.add(buildPasswordCard());
        root.add(vGap(16));
        root.add(buildApplicationsCard());
        root.add(vGap(10));

        JScrollPane scroll = new JScrollPane(root);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Page title
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildPageTitle() {
        JLabel lbl = new JLabel("My Profile");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SECTION 1 — Profile Info Card
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildProfileCard() {

        User u = Session.getCurrentUser();

        JPanel card = card("Profile Information");

        // ---- All fields (Student ID and Email are read-only) ----
        fStudentId = tfReadOnly(u != null ? u.getStudentId() : "");
        fEmail     = tfReadOnly(u != null ? u.getEmail()     : "");
        fFullName  = tf(u != null ? u.getFullName()          : "");
        fContact   = tf(u != null ? u.getContactNumber()     : "");
        fAddress   = tf(u != null ? u.getAddress()           : "");
        fCgpa      = tf(u != null ? String.valueOf(u.getCgpa()) : "");
        fAge       = tf(u != null ? String.valueOf(u.getAge())  : "");

        cbCourse = combo(new String[]{
                "Computer Science","Applied Computing","Cybersecurity",
                "Software Engineering","Business & Management","Law & Management"
        }, u != null ? u.getCourse() : null);

        cbFaculty = combo(new String[]{"FOICDT","FLM","FSSH","FoE","FoA"},
                u != null ? u.getFaculty() : null);

        cbLevel = combo(new String[]{"Level 1","Level 2","Level 3","Level 4","Masters"},
                u != null ? u.getLevel() : null);

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(LEFT_ALIGNMENT);

        addFieldPair(grid, "Student ID",         fStudentId);
        addFieldPair(grid, "Email",              fEmail);
        addFieldPair(grid, "Full Name *",        fFullName);
        addFieldPair(grid, "Contact Number",     fContact);
        addFieldPair(grid, "Address",            fAddress);
        addFieldPair(grid, "Course",             cbCourse);
        addFieldPair(grid, "Faculty",            cbFaculty);
        addFieldPair(grid, "CGPA *",             fCgpa);
        addFieldPair(grid, "Level",              cbLevel);
        addFieldPair(grid, "Age *",              fAge);

        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, grid.getPreferredSize().height + 20));
        card.add(grid);
        card.add(vGap(14));

        // ---- Feedback label ----
        profileFeedback = feedbackLabel();
        card.add(profileFeedback);
        card.add(vGap(8));

        // ---- Save button ----
        JButton saveBtn = primaryButton("Save Changes");
        saveBtn.addActionListener(e -> handleSaveProfile());
        JPanel btnRow = leftAligned(saveBtn);
        card.add(btnRow);

        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SECTION 2 — Change Password Card
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildPasswordCard() {

        JPanel card = card("Change Password");

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(LEFT_ALIGNMENT);

        fCurrentPw = pwField();
        fNewPw     = pwField();
        fConfirmPw = pwField();

        addFieldPair(grid, "Current Password",  fCurrentPw);
        addFieldPair(grid, "New Password",       fNewPw);
        addFieldPair(grid, "Confirm New Password", fConfirmPw);

        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, grid.getPreferredSize().height + 20));
        card.add(grid);
        card.add(vGap(10));

        JLabel hint = new JLabel("Password must be at least 6 characters.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(LABEL_CLR);
        hint.setAlignmentX(LEFT_ALIGNMENT);
        card.add(hint);
        card.add(vGap(10));

        pwFeedback = feedbackLabel();
        card.add(pwFeedback);
        card.add(vGap(8));

        JButton changeBtn = primaryButton("Update Password");
        changeBtn.addActionListener(e -> handleChangePassword());
        card.add(leftAligned(changeBtn));

        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SECTION 3 — Applied Jobs Card
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildApplicationsCard() {

        JPanel card = card("Jobs Applied For");

        appsModel = new DefaultTableModel(
                new Object[]{"#", "Company", "Job Title", "Date Applied", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(appsModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 235, 245));
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(LIGHT_BG);
        table.getTableHeader().setForeground(new Color(40, 40, 40));
        table.setSelectionBackground(new Color(210, 225, 245));

        // ---- Status column colour renderer ----
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                setHorizontalAlignment(CENTER);
                String status = value != null ? value.toString() : "";
                switch (status) {
                    case "Selected"    -> { setForeground(new Color(0, 128, 0)); setFont(getFont().deriveFont(Font.BOLD)); }
                    case "Shortlisted" -> { setForeground(new Color(200, 120, 0)); setFont(getFont().deriveFont(Font.BOLD)); }
                    case "Rejected"    -> { setForeground(RED_ERR); setFont(getFont().deriveFont(Font.BOLD)); }
                    default            -> { setForeground(ACCENT); setFont(getFont().deriveFont(Font.PLAIN)); }
                }
                return this;
            }
        });

        // Centre "#" column
        table.getColumnModel().getColumn(0).setCellRenderer(centreRenderer());
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(3).setCellRenderer(centreRenderer());

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(0, 200));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        sp.setBorder(new LineBorder(BORDER_CLR, 1, true));
        sp.setAlignmentX(LEFT_ALIGNMENT);

        card.add(sp);

        loadApplications();
        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Action handlers
    // ════════════════════════════════════════════════════════════════════════

    private void handleSaveProfile() {
        String error = ProfileService.updateProfile(
                fFullName.getText().trim(),
                fAddress.getText().trim(),
                fContact.getText().trim(),
                (String) cbCourse.getSelectedItem(),
                (String) cbFaculty.getSelectedItem(),
                fCgpa.getText().trim(),
                (String) cbLevel.getSelectedItem(),
                fAge.getText().trim()
        );

        if (error == null) {
            showFeedback(profileFeedback, "✔  Profile updated successfully.", true);
        } else {
            showFeedback(profileFeedback, "✘  " + error, false);
        }
    }

    private void handleChangePassword() {
        String error = ProfileService.changePassword(
                new String(fCurrentPw.getPassword()),
                new String(fNewPw.getPassword()),
                new String(fConfirmPw.getPassword())
        );

        if (error == null) {
            showFeedback(pwFeedback, "✔  Password updated successfully.", true);
            fCurrentPw.setText("");
            fNewPw.setText("");
            fConfirmPw.setText("");
        } else {
            showFeedback(pwFeedback, "✘  " + error, false);
        }
    }

    private void loadApplications() {
        appsModel.setRowCount(0);
        List<Application> apps = ApplicationService.getApplications(Session.getUserId());
        int i = 1;
        for (Application app : apps) {
            String date = (app.getDateApplied() != null)
                    ? app.getDateApplied().toString().substring(0, 10) : "-";
            appsModel.addRow(new Object[]{i++, app.getCompany(), app.getJobTitle(), date, app.getStatus()});
        }
        if (apps.isEmpty()) {
            appsModel.addRow(new Object[]{"", "No applications yet.", "", "", ""});
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI helper factory methods
    // ════════════════════════════════════════════════════════════════════════

    /** Styled white card panel with a titled section header */
    private JPanel card(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(16, 20, 16, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(ACCENT);
        heading.setBorder(new EmptyBorder(0, 0, 4, 0));
        heading.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(heading);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_CLR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(sep);
        panel.add(vGap(12));

        return panel;
    }

    private void addFieldPair(JPanel grid, String labelText, JComponent field) {
        JPanel cell = new JPanel(new BorderLayout(0, 4));
        cell.setOpaque(false);
        cell.add(fieldLabel(labelText), BorderLayout.NORTH);
        cell.add(field, BorderLayout.CENTER);
        grid.add(cell);
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(LABEL_CLR);
        return l;
    }

    private JTextField tf(String val) {
        JTextField f = new JTextField(val);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(4, 7, 4, 7)
        ));
        return f;
    }

    /** A text field that looks like tf() but cannot be edited. */
    private JTextField tfReadOnly(String val) {
        JTextField f = tf(val);
        f.setEditable(false);
        f.setBackground(new Color(240, 243, 248));
        f.setForeground(new Color(90, 100, 115));
        return f;
    }

    private JPasswordField pwField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(4, 7, 4, 7)
        ));
        return f;
    }

    private JComboBox<String> combo(String[] items, String selected) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        cb.setBorder(new LineBorder(BORDER_CLR, 1, true));
        if (selected != null) cb.setSelectedItem(selected);
        return cb;
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 36));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color hover = new Color(38, 82, 135);
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(ACCENT); }
        });
        return btn;
    }

    private JLabel feedbackLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private void showFeedback(JLabel lbl, String msg, boolean success) {
        lbl.setText(msg);
        lbl.setForeground(success ? GREEN_OK : RED_ERR);
    }

    private JPanel leftAligned(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height + 4));
        p.add(comp);
        return p;
    }

    private Component vGap(int h) {
        return Box.createRigidArea(new Dimension(0, h));
    }

    private DefaultTableCellRenderer centreRenderer() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        return r;
    }
}