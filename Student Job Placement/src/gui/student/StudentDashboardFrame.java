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
        JPanel cardsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        cardsPanel.add(createCard("Profile"));
        cardsPanel.add(createCard("Recruitments"));
        cardsPanel.add(createCard("Applications"));
        cardsPanel.add(createCard("Off-Campus Jobs"));
        cardsPanel.add(createCard("Policy Page"));
        cardsPanel.add(createCard("Logo Page"));
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

        card.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        text + " page not implemented yet.")
        );

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new StudentDashboardFrame().setVisible(true)
        );
    }
}
