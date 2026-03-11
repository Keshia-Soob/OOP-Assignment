package gui.student;

import gui.auth.LoginFrame;
import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import service.ApplicationService;
import service.OffCampusService;
import util.Session;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class StudentDashboardFrame extends BaseFrame {

    public StudentDashboardFrame() {
        super("Dashboard", SidebarPanel.NavItem.DASHBOARD);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ── Greeting ─────────────────────────────────────────────────────────
        String name = Session.getCurrentUser() != null
                ? Session.getCurrentUser().getFullName()
                : "Student";

        JLabel title = new JLabel("Welcome, " + name);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);

        // ── Stats cards ───────────────────────────────────────────────────────
        int userId      = Session.getUserId();
        int totalApps   = ApplicationService.countTotal(userId);

        // Shortlisted = on-campus Shortlisted + off-campus Shortlisted
        int shortlistedOnCampus  = ApplicationService.countByStatus(userId, "Shortlisted");
        int shortlistedOffCampus = OffCampusService.countByStatus(userId, "Shortlisted");
        int shortlisted          = shortlistedOnCampus + shortlistedOffCampus;

        // Selected = on-campus Selected + off-campus Selected (combined for one-offer rule)
        int selectedOnCampus  = ApplicationService.countByStatus(userId, "Selected");
        int selectedOffCampus = OffCampusService.countSelected(userId);
        int selected          = selectedOnCampus + selectedOffCampus;

        int offCampus   = OffCampusService.countByUser(userId);

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.add(buildStatCard("Applications",  String.valueOf(totalApps),   new Color(52, 102, 160)));
        statsRow.add(buildStatCard("Shortlisted",   String.valueOf(shortlisted), new Color(230, 160, 20)));
        statsRow.add(buildStatCard("Selected",      String.valueOf(selected),    new Color(34, 139, 34)));
        statsRow.add(buildStatCard("Off-Campus",    String.valueOf(offCampus),   new Color(130, 60, 180)));

        // ── Navigation cards ──────────────────────────────────────────────────
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 30, 20));
        cardsPanel.setOpaque(false);

        cardsPanel.add(createNavCard("Profile"));
        cardsPanel.add(createNavCard("Recruitments"));
        cardsPanel.add(createNavCard("Applications"));
        cardsPanel.add(createNavCard("Off-Campus Jobs"));
        cardsPanel.add(createNavCard("Policy Page"));
        cardsPanel.add(createNavCard("Log Out"));

        // ── Recent activity feed — merge on-campus + off-campus, newest first ──
        List<String> onCampusActivity  = ApplicationService.getRecentActivity(userId, 10);
        List<String> offCampusActivity = OffCampusService.getRecentActivity(userId, 10);

        List<String> allActivity = new java.util.ArrayList<>();
        allActivity.addAll(onCampusActivity);
        allActivity.addAll(offCampusActivity);

        // Sort descending by the date prefix "yyyy-MM-dd" at the start of each string
        allActivity.sort((a, b) -> b.substring(0, 10).compareTo(a.substring(0, 10)));

        // Keep only the 5 most recent
        List<String> activity = allActivity.size() > 5
                ? allActivity.subList(0, 5)
                : allActivity;
        JPanel activityCard = buildActivityCard(activity);

        // ── Centre section (nav cards + activity side by side) ────────────────
        JPanel centre = new JPanel(new BorderLayout(20, 0));
        centre.setOpaque(false);
        centre.add(cardsPanel,   BorderLayout.CENTER);
        centre.add(activityCard, BorderLayout.EAST);

        // ── Main body: stats on top, nav+activity below ───────────────────────
        JPanel mainBody = new JPanel(new BorderLayout(0, 20));
        mainBody.setOpaque(false);
        mainBody.add(statsRow, BorderLayout.NORTH);
        mainBody.add(centre,   BorderLayout.CENTER);

        root.add(top,      BorderLayout.NORTH);
        root.add(mainBody, BorderLayout.CENTER);

        return root;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Stat card
    // ─────────────────────────────────────────────────────────────────────────

    private JPanel buildStatCard(String label, String value, Color accent) {

        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 235), 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        // Coloured top stripe
        JPanel stripe = new JPanel();
        stripe.setBackground(accent);
        stripe.setPreferredSize(new Dimension(1, 5));

        JLabel numLabel = new JLabel(value, SwingConstants.CENTER);
        numLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        numLabel.setForeground(accent);

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblLabel.setForeground(new Color(90, 90, 90));

        card.add(stripe,   BorderLayout.NORTH);
        card.add(numLabel, BorderLayout.CENTER);
        card.add(lblLabel, BorderLayout.SOUTH);

        return card;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Recent activity card
    // ─────────────────────────────────────────────────────────────────────────

    private JPanel buildActivityCard(List<String> items) {

        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(300, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 235), 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel heading = new JLabel("Recent Activity");
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        heading.setForeground(new Color(40, 40, 40));
        card.add(heading, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        if (items.isEmpty()) {
            JLabel empty = new JLabel("No recent activity.");
            empty.setFont(new Font("SansSerif", Font.ITALIC, 12));
            empty.setForeground(new Color(150, 150, 150));
            empty.setAlignmentX(LEFT_ALIGNMENT);
            list.add(empty);
        } else {
            for (String item : items) {
                // On-campus format:  "yyyy-MM-dd  —  JobTitle @ Company  [Status]"
                // Off-campus format: "yyyy-MM-dd  —  JobTitle @ Company  [Status]  {Off-Campus}"
                boolean isOffCampus = item.contains("{Off-Campus}");
                String  clean       = item.replace("{Off-Campus}", "").trim();

                String[] parts  = clean.split("\\[");
                String mainText = parts[0].trim();
                String status   = parts.length > 1
                        ? parts[1].replace("]", "").trim()
                        : "";

                list.add(buildActivityRow(mainText, status, isOffCampus));
                list.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildActivityRow(String mainText, String status, boolean isOffCampus) {

        JPanel row = new JPanel(new BorderLayout(8, 2));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(235, 240, 250), 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));

        // Main text
        JLabel text = new JLabel("<html>" + mainText + "</html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 11));
        text.setForeground(new Color(60, 60, 60));
        row.add(text, BorderLayout.CENTER);

        // Right side: optional "Off-Campus" tag + status badge stacked vertically
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        if (isOffCampus) {
            JLabel offTag = new JLabel("Off-Campus");
            offTag.setFont(new Font("SansSerif", Font.BOLD, 9));
            offTag.setForeground(Color.WHITE);
            offTag.setOpaque(true);
            offTag.setBackground(new Color(130, 60, 180));
            offTag.setBorder(new EmptyBorder(1, 5, 1, 5));
            offTag.setAlignmentX(RIGHT_ALIGNMENT);
            rightPanel.add(offTag);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        JLabel badge = new JLabel(status);
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(statusColor(status));
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));
        badge.setAlignmentX(RIGHT_ALIGNMENT);
        rightPanel.add(badge);

        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    private Color statusColor(String status) {
        switch (status) {
            case "Shortlisted": return new Color(230, 160, 20);
            case "Selected":    return new Color(34, 139, 34);
            case "Rejected":    return new Color(190, 50, 50);
            default:            return new Color(52, 102, 160);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Navigation card
    // ─────────────────────────────────────────────────────────────────────────

    private JButton createNavCard(String text) {
        JButton card = new JButton(text);
        card.setFont(new Font("SansSerif", Font.BOLD, 15));
        card.setFocusPainted(false);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        card.setPreferredSize(new Dimension(160, 90));
        card.addActionListener(e -> handleNavigation(text));
        return card;
    }

    private void handleNavigation(String destination) {
        switch (destination) {
            case "Recruitments":
                new RecruitmentFrame().setVisible(true); dispose(); break;
            case "Applications":
                new ApplicationsFrame().setVisible(true); dispose(); break;
            case "Profile":
                new ProfileFrame().setVisible(true); dispose(); break;
            case "Off-Campus Jobs":
                new OffCampusFrame().setVisible(true); dispose(); break;
            case "Policy Page":
                new StudentPolicyFrame().setVisible(true); dispose(); break;
            case "Log Out":
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to log out?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.AuthService.logout();
                    new LoginFrame().setVisible(true);
                    dispose();
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Page not implemented.");
        }
    }
}