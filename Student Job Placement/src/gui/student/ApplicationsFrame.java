package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import model.Application;
import model.ApplicationHistory;
import service.ApplicationService;
import util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationsFrame extends BaseFrame {

    private JTable            table;
    private DefaultTableModel model;
    private JComboBox<String> filterCombo;

    // Keep loaded list so buttons can reference selected row
    private List<Application> currentList;

    public ApplicationsFrame() {
        super("Applications", SidebarPanel.NavItem.APPLICATIONS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);

        // ── Top row: Title + Filter ───────────────────────────────────────────
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("Applications");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));
        top.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        filterCombo = new JComboBox<>(
                new String[]{"All", "Applied", "Shortlisted", "Selected", "Rejected"});
        filterCombo.setPreferredSize(new Dimension(140, 28));

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        top.add(filterPanel, BorderLayout.EAST);

        // ── Table ─────────────────────────────────────────────────────────────
        model = new DefaultTableModel(
                new Object[]{"Company", "Job Title", "Date Applied", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        // ── Bottom Buttons ────────────────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        bottom.setOpaque(false);

        JButton timelineBtn = buildButton("View Timeline", new Color(52, 102, 160));
        JButton withdrawBtn = buildButton("Withdraw Application", new Color(190, 50, 50));

        timelineBtn.addActionListener(e -> onViewTimeline());
        withdrawBtn.addActionListener(e -> onWithdraw());

        bottom.add(timelineBtn);
        bottom.add(withdrawBtn);

        // ── Assemble ──────────────────────────────────────────────────────────
        root.add(top,    BorderLayout.NORTH);
        root.add(sp,     BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        loadApplications("All");
        filterCombo.addActionListener(
                e -> loadApplications((String) filterCombo.getSelectedItem()));

        return root;
    }

    // ── Load / filter ─────────────────────────────────────────────────────────

    private void loadApplications(String statusFilter) {
        model.setRowCount(0);

        currentList = ApplicationService.getApplications(Session.getUserId());

        for (Application app : currentList) {
            if (!"All".equals(statusFilter) &&
                !app.getStatus().equalsIgnoreCase(statusFilter)) continue;

            String dateStr = (app.getDateApplied() != null)
                    ? app.getDateApplied().toString().substring(0, 10)
                    : "-";

            model.addRow(new Object[]{
                    app.getCompany(),
                    app.getJobTitle(),
                    dateStr,
                    app.getStatus()
            });
        }
    }

    // ── Withdraw ──────────────────────────────────────────────────────────────

    private void onWithdraw() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an application to withdraw.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Application app = getMatchingApp(viewRow);
        if (app == null) return;

        if (!"Applied".equalsIgnoreCase(app.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "Only applications with status \"Applied\" can be withdrawn.\n" +
                    "This application is currently: " + app.getStatus(),
                    "Cannot Withdraw", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Withdraw your application to " + app.getCompany() +
                " for \"" + app.getJobTitle() + "\"?\n\nThis cannot be undone.",
                "Confirm Withdrawal", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = ApplicationService.withdraw(app.getApplicationId(),
                                                  Session.getUserId());
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Application withdrawn successfully.",
                    "Withdrawn", JOptionPane.INFORMATION_MESSAGE);
            loadApplications((String) filterCombo.getSelectedItem());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Could not withdraw the application. It may have already been updated.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Timeline dialog ───────────────────────────────────────────────────────

    private void onViewTimeline() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an application to view its timeline.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Application app = getMatchingApp(viewRow);
        if (app == null) return;

        List<ApplicationHistory> history =
                ApplicationService.getHistory(app.getApplicationId());

        showTimelineDialog(app, history);
    }

    private void showTimelineDialog(Application app,
                                    List<ApplicationHistory> history) {

        JDialog dialog = new JDialog(this,
                app.getCompany() + " — " + app.getJobTitle(), true);
        dialog.setSize(480, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(new EmptyBorder(16, 20, 16, 20));

        // Title
        JLabel heading = new JLabel("Application Timeline");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        heading.setForeground(new Color(40, 40, 40));
        dialog.add(heading, BorderLayout.NORTH);

        // Timeline panel (vertical list of steps)
        JPanel timelinePanel = new JPanel();
        timelinePanel.setLayout(new BoxLayout(timelinePanel, BoxLayout.Y_AXIS));
        timelinePanel.setBackground(Color.WHITE);
        timelinePanel.setBorder(new EmptyBorder(8, 0, 8, 0));

        String[] allSteps = {"Applied", "Shortlisted", "Selected"};

        // Determine how far the application has progressed
        int reachedIndex = -1;
        for (ApplicationHistory h : history) {
            for (int i = 0; i < allSteps.length; i++) {
                if (allSteps[i].equalsIgnoreCase(h.getStatus())) {
                    if (i > reachedIndex) reachedIndex = i;
                }
            }
        }

        // Handle Rejected as a terminal state
        boolean rejected = history.stream()
                .anyMatch(h -> "Rejected".equalsIgnoreCase(h.getStatus()));

        for (int i = 0; i < allSteps.length; i++) {
            String step = allSteps[i];

            // Find timestamp for this step if it exists
            String timestamp = "";
            for (ApplicationHistory h : history) {
                if (step.equalsIgnoreCase(h.getStatus())) {
                    timestamp = h.getChangedAt().toString().substring(0, 16);
                    break;
                }
            }

            boolean done    = i <= reachedIndex && !rejected;
            boolean current = i == reachedIndex && !rejected;
            boolean pending = i > reachedIndex  || rejected;

            timelinePanel.add(buildTimelineStep(step, timestamp, done, current, pending));

            // Connector line between steps (except after last)
            if (i < allSteps.length - 1) {
                timelinePanel.add(buildConnector(done && i < reachedIndex));
            }
        }

        // Show Rejected node if applicable
        if (rejected) {
            timelinePanel.add(buildConnector(false));
            String ts = "";
            for (ApplicationHistory h : history) {
                if ("Rejected".equalsIgnoreCase(h.getStatus())) {
                    ts = h.getChangedAt().toString().substring(0, 16);
                    break;
                }
            }
            timelinePanel.add(buildTimelineStep("Rejected", ts, false, true, false));
        }

        JScrollPane scroll = new JScrollPane(timelinePanel);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        dialog.add(scroll, BorderLayout.CENTER);

        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        closeBtn.setFocusPainted(false);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.add(closeBtn);
        dialog.add(btnRow, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ── Timeline step builder ─────────────────────────────────────────────────

    private JPanel buildTimelineStep(String label, String timestamp,
                                     boolean done, boolean current,
                                     boolean pending) {

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);

        // Circle indicator
        Color circleColor;
        String symbol;
        if ("Rejected".equals(label)) {
            circleColor = new Color(190, 50, 50);
            symbol = "✕";
        } else if (done || current) {
            circleColor = new Color(34, 139, 34);
            symbol = "✓";
        } else {
            circleColor = new Color(180, 180, 180);
            symbol = "○";
        }

        JLabel circle = new JLabel(symbol) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(circleColor);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String txt = getText();
                int x = (getWidth()  - fm.stringWidth(txt)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(txt, x, y);
                g2.dispose();
            }
        };
        circle.setPreferredSize(new Dimension(28, 28));
        circle.setHorizontalAlignment(SwingConstants.CENTER);

        // Step label
        JLabel stepLabel = new JLabel(label);
        stepLabel.setFont(new Font("SansSerif",
                current ? Font.BOLD : Font.PLAIN, 14));
        stepLabel.setForeground(pending && !"Rejected".equals(label)
                ? new Color(160, 160, 160)
                : new Color(30, 30, 30));

        // Timestamp sub-label
        JLabel tsLabel = new JLabel(timestamp.isEmpty() ? "Pending" : timestamp);
        tsLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        tsLabel.setForeground(new Color(120, 120, 120));

        JPanel textCol = new JPanel();
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.setOpaque(false);
        textCol.add(stepLabel);
        textCol.add(tsLabel);

        row.add(circle);
        row.add(textCol);

        return row;
    }

    private JPanel buildConnector(boolean filled) {
        JPanel line = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(filled ? new Color(34, 139, 34) : new Color(200, 200, 200));
                g.fillRect(getWidth() / 2 - 1, 0, 2, getHeight());
            }
        };
        line.setOpaque(false);
        line.setPreferredSize(new Dimension(28, 18));
        line.setMaximumSize (new Dimension(28, 18));

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        wrap.setOpaque(false);
        wrap.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(line);
        return wrap;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Map the selected table row back to the Application object in currentList,
     * accounting for the active status filter.
     */
    private Application getMatchingApp(int viewRow) {
        String filter = (String) filterCombo.getSelectedItem();
        int count = 0;
        for (Application app : currentList) {
            if (!"All".equals(filter) &&
                !app.getStatus().equalsIgnoreCase(filter)) continue;
            if (count == viewRow) return app;
            count++;
        }
        return null;
    }

    private JButton buildButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 34));
        return btn;
    }
}