package gui.student;

import dao.JobOfferDAO;
import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Job;
import model.User;
import service.ProfileService;
import util.Session;

/**
 * Profile Page — displays user information, applied jobs, and provides inline
 * edit + password-change functionality.
 */
public class ProfileFrame extends BaseFrame {

    // ── Theme ────────────────────────────────────────────────────────────────
    private static final Color BG = Color.WHITE;
    private static final Color ACCENT = new Color(52, 102, 160);
    private static final Color BORDER_CLR = new Color(210, 220, 235);
    private static final Color LABEL_CLR = new Color(80, 90, 110);
    private static final Color GREEN_OK = new Color(34, 139, 34);
    private static final Color RED_ERR = new Color(180, 0, 0);

    // ── Profile-info edit fields ─────────────────────────────────────────────
    private JTextField fFullName, fAddress, fContact, fCgpa, fAge;
    private JTextField fStudentId, fEmail;
    private JComboBox<String> cbCourse, cbFaculty, cbLevel;
    private JLabel profileFeedback;

    // ── Password-change fields ───────────────────────────────────────────────
    private JPasswordField fCurrentPw, fNewPw, fConfirmPw;
    private JLabel pwFeedback;

    // ── Offers table ─────────────────────────────────────────────────────────
    private JTable offersTable;
    private DefaultTableModel offersTableModel;

    public ProfileFrame() {
        super("Profile", SidebarPanel.NavItem.PROFILE);
    }

    @Override
    protected JComponent buildContent() {

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
        root.add(buildOffersCard());
        root.add(vGap(10));

        JScrollPane scroll = new JScrollPane(root);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ════════════════════════════════════════════════════════════════════════
    // Page title
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildPageTitle() {
        JLabel lbl = new JLabel("My Profile");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SECTION 1 — Profile Info Card
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildProfileCard() {

        User u = Session.getCurrentUser();

        JPanel card = card("Profile Information");

        fStudentId = tfReadOnly(u != null ? u.getStudentId() : "");
        fEmail = tfReadOnly(u != null ? u.getEmail() : "");
        fFullName = tf(u != null ? u.getFullName() : "");
        fContact = tf(u != null ? u.getContactNumber() : "");
        fAddress = tf(u != null ? u.getAddress() : "");
        fCgpa = tf(u != null ? String.valueOf(u.getCgpa()) : "");
        fAge = tf(u != null ? String.valueOf(u.getAge()) : "");

        cbCourse = combo(new String[]{
            "Computer Science", "Applied Computing", "Cybersecurity",
            "Software Engineering", "Business & Management", "Law & Management"
        }, u != null ? u.getCourse() : null);

        cbFaculty = combo(new String[]{"FOICDT", "FLM", "FSSH", "FoE", "FoA"},
                u != null ? u.getFaculty() : null);

        cbLevel = combo(new String[]{"Level 1", "Level 2", "Level 3", "Level 4", "Masters"},
                u != null ? u.getLevel() : null);

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(LEFT_ALIGNMENT);

        addFieldPair(grid, "Student ID", fStudentId);
        addFieldPair(grid, "Email", fEmail);
        addFieldPair(grid, "Full Name *", fFullName);
        addFieldPair(grid, "Contact Number", fContact);
        addFieldPair(grid, "Address", fAddress);
        addFieldPair(grid, "Course", cbCourse);
        addFieldPair(grid, "Faculty", cbFaculty);
        addFieldPair(grid, "CGPA *", fCgpa);
        addFieldPair(grid, "Level", cbLevel);
        addFieldPair(grid, "Age *", fAge);

        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, grid.getPreferredSize().height + 20));
        card.add(grid);
        card.add(vGap(14));

        profileFeedback = feedbackLabel();
        card.add(profileFeedback);
        card.add(vGap(8));

        JButton saveBtn = primaryButton("Save Changes");
        saveBtn.addActionListener(e -> handleSaveProfile());
        card.add(leftAligned(saveBtn));

        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SECTION 2 — Change Password Card
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildPasswordCard() {

        JPanel card = card("Change Password");

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(LEFT_ALIGNMENT);

        fCurrentPw = pwField();
        fNewPw = pwField();
        fConfirmPw = pwField();

        addFieldPair(grid, "Current Password", fCurrentPw);
        addFieldPair(grid, "New Password", fNewPw);
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
    // SECTION 3 — Job Offers
    // ════════════════════════════════════════════════════════════════════════
    private JComponent buildOffersCard() {

        JPanel card = card("Job Offers from Companies");

        int userId = Session.getUserId();

        // THIS LINE MAKES EVERYTHING AUTOMATIC
        JobOfferDAO.seedOffersForUser(userId, 3);

        List<Job> offers = JobOfferDAO.getPendingOffersByUser(userId);

        if (userId <= 0) {
            JLabel msg = new JLabel("No user session found.");
            msg.setForeground(RED_ERR);
            msg.setAlignmentX(LEFT_ALIGNMENT);
            card.add(msg);
            return card;
        }

        if (JobOfferDAO.hasAcceptedOffer(userId)) {
            JLabel msg = new JLabel("You have already accepted a job offer. You cannot accept another one.");
            msg.setFont(new Font("SansSerif", Font.BOLD, 13));
            msg.setForeground(GREEN_OK);
            msg.setAlignmentX(LEFT_ALIGNMENT);
            card.add(msg);
            return card;
        }

        if (offers.isEmpty()) {
            JLabel empty = new JLabel("No pending job offers available.");
            empty.setFont(new Font("SansSerif", Font.ITALIC, 13));
            empty.setForeground(LABEL_CLR);
            empty.setAlignmentX(LEFT_ALIGNMENT);
            card.add(empty);
            return card;
        }

        offersTableModel = new DefaultTableModel(
                new Object[]{"Job ID", "Company", "Role", "Location", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        for (Job job : offers) {
            offersTableModel.addRow(new Object[]{
                job.getJobId(),
                job.getCompany(),
                job.getJobTitle(),
                job.getLocation(),
                "Actions"
            });
        }

        offersTable = new JTable(offersTableModel);
        offersTable.setRowHeight(36);
        offersTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        offersTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        // Hide job ID column
        offersTable.getColumnModel().getColumn(0).setMinWidth(0);
        offersTable.getColumnModel().getColumn(0).setMaxWidth(0);
        offersTable.getColumnModel().getColumn(0).setWidth(0);

        offersTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        offersTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        offersTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        offersTable.getColumnModel().getColumn(4).setPreferredWidth(260);

        offersTable.getColumnModel().getColumn(1).setCellRenderer(centreRenderer());
        offersTable.getColumnModel().getColumn(2).setCellRenderer(centreRenderer());
        offersTable.getColumnModel().getColumn(3).setCellRenderer(centreRenderer());

        offersTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        offersTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), offersTable));

        JScrollPane sp = new JScrollPane(offersTable);
        sp.setBorder(new LineBorder(BORDER_CLR, 1, true));
        sp.setPreferredSize(new Dimension(700, 180));

        card.add(sp);

        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    // Action handlers
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
            showFeedback(profileFeedback, "✔ Profile updated successfully.", true);
        } else {
            showFeedback(profileFeedback, "✘ " + error, false);
        }
    }

    private void handleChangePassword() {
        String error = ProfileService.changePassword(
                new String(fCurrentPw.getPassword()),
                new String(fNewPw.getPassword()),
                new String(fConfirmPw.getPassword())
        );

        if (error == null) {
            showFeedback(pwFeedback, "✔ Password updated successfully.", true);
            fCurrentPw.setText("");
            fNewPw.setText("");
            fConfirmPw.setText("");
        } else {
            showFeedback(pwFeedback, "✘ " + error, false);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Helpers for offers
    // ════════════════════════════════════════════════════════════════════════
    private Job getJobFromRow(int row) {
        int jobId = Integer.parseInt(offersTableModel.getValueAt(row, 0).toString());

        List<Job> offers = JobOfferDAO.getPendingOffersByUser(Session.getUserId());
        for (Job j : offers) {
            if (j.getJobId() == jobId) {
                return j;
            }
        }

        return null;
    }

    private void reloadProfileFrame() {
        dispose();
        SwingUtilities.invokeLater(() -> new ProfileFrame().setVisible(true));
    }

    // ════════════════════════════════════════════════════════════════════════
    // UI helper factory methods
    // ════════════════════════════════════════════════════════════════════════
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
        if (selected != null) {
            cb.setSelectedItem(selected);
        }
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

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT);
            }
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

    // ════════════════════════════════════════════════════════════════════════
    // JTable Button Renderer
    // ════════════════════════════════════════════════════════════════════════
    class ButtonRenderer extends JPanel implements TableCellRenderer {

        JButton view = new JButton("View");
        JButton accept = new JButton("Accept");
        JButton reject = new JButton("Reject");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            add(view);
            add(accept);
            add(reject);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // JTable Button Editor
    // ════════════════════════════════════════════════════════════════════════
    class ButtonEditor extends DefaultCellEditor {

        private final JPanel panel = new JPanel();
        private final JButton view = new JButton("View");
        private final JButton accept = new JButton("Accept");
        private final JButton reject = new JButton("Reject");
        private final JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;

            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(view);
            panel.add(accept);
            panel.add(reject);

            view.addActionListener(e -> {
                fireEditingStopped();
                viewOffer();
            });

            accept.addActionListener(e -> {
                fireEditingStopped();
                acceptOffer();
            });

            reject.addActionListener(e -> {
                fireEditingStopped();
                rejectOffer();
            });
        }

        private void viewOffer() {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }

            Job job = getJobFromRow(row);
            if (job == null) {
                JOptionPane.showMessageDialog(ProfileFrame.this,
                        "Unable to load job details.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String details
                    = "Company: " + job.getCompany() + "\n"
                    + "Role: " + job.getJobTitle() + "\n"
                    + "Location: " + job.getLocation() + "\n"
                    + "Minimum CGPA: " + job.getMinCgpa() + "\n"
                    + "Salary: " + job.getSalary() + "\n"
                    + "Duration: " + job.getDuration() + "\n"
                    + "Start Date: " + job.getStartDate() + "\n"
                    + "Contact Email: " + job.getContactEmail() + "\n"
                    + "Contact Number: " + job.getContactNumber() + "\n\n"
                    + "Description:\n" + job.getDescription() + "\n\n"
                    + "Key Responsibilities:\n" + job.getKeyResponsibilities();

            JTextArea area = new JTextArea(details, 18, 40);
            area.setWrapStyleWord(true);
            area.setLineWrap(true);
            area.setEditable(false);
            area.setCaretPosition(0);

            JOptionPane.showMessageDialog(
                    ProfileFrame.this,
                    new JScrollPane(area),
                    "Job Offer Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        private void acceptOffer() {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }

            int userId = Session.getUserId();
            int jobId = Integer.parseInt(table.getValueAt(row, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(
                    ProfileFrame.this,
                    "Accepting this offer will prevent you from proceeding with other job applications.\nDo you want to continue?",
                    "Confirm Acceptance",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean offerAccepted = JobOfferDAO.acceptOffer(userId, jobId);

            if (!offerAccepted) {
                JOptionPane.showMessageDialog(ProfileFrame.this,
                        "Unable to accept this offer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JobOfferDAO.rejectAllOtherOffers(userId, jobId);

            JOptionPane.showMessageDialog(ProfileFrame.this,
                    "Offer accepted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            reloadProfileFrame();
        }

        private void rejectOffer() {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }

            int userId = Session.getUserId();
            int jobId = Integer.parseInt(table.getValueAt(row, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(
                    ProfileFrame.this,
                    "Are you sure you want to reject this offer?",
                    "Confirm Rejection",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean ok = JobOfferDAO.rejectOffer(userId, jobId);

            if (ok) {
                ((DefaultTableModel) table.getModel()).removeRow(row);

                if (table.getRowCount() == 0) {
                    reloadProfileFrame();
                } else {
                    JOptionPane.showMessageDialog(ProfileFrame.this,
                            "Offer rejected successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ProfileFrame.this,
                        "Unable to reject this offer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
}
