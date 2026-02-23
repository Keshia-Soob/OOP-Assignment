package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentDashboardFrame extends BaseFrame {

    public StudentDashboardFrame() {
        super("Dashboard", SidebarPanel.NavItem.DASHBOARD);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---- Top: Title ----
        JLabel title = new JLabel("Student Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);

        // ---- Cards Grid ----
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        cardsPanel.add(createCard("Profile"));
        cardsPanel.add(createCard("Recruitments"));
        cardsPanel.add(createCard("Applications"));
        cardsPanel.add(createCard("Off-Campus Jobs"));
        cardsPanel.add(createCard("Policy Page"));
        cardsPanel.add(createCard("Log Out"));

        root.add(top, BorderLayout.NORTH);
        root.add(cardsPanel, BorderLayout.CENTER);

        return root;
    }

    private JButton createCard(String text) {

        JButton card = new JButton(text);
        card.setFont(new Font("SansSerif", Font.BOLD, 15));
        card.setFocusPainted(false);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        card.setPreferredSize(new Dimension(180, 110));

        card.addActionListener(e -> handleNavigation(text));

        return card;
    }

    private void handleNavigation(String destination) {

        switch (destination) {

            case "Recruitments":
                new RecruitmentFrame().setVisible(true);
                this.dispose();
                break;

            case "Applications":
                new ApplicationsFrame().setVisible(true);
                this.dispose();
                break;

            case "Profile":
                JOptionPane.showMessageDialog(this, "Profile page not implemented yet.");
                break;

            case "Off-Campus Jobs":
                JOptionPane.showMessageDialog(this, "Off-Campus Jobs page not implemented yet.");
                break;

            case "Policy Page":
                new StudentPolicyFrame().setVisible(true);
                this.dispose();
                break;

            case "Log Out":
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to log out?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Replace with LoginFrame if you have one
                    JOptionPane.showMessageDialog(this, "Logging out...");
                    this.dispose();
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "Page not implemented.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new StudentDashboardFrame().setVisible(true)
        );
    }
}
