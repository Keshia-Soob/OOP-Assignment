package gui.base;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {

    public enum NavItem {
        DASHBOARD,
        RECRUITMENTS,
        APPLICATIONS,
        PROFILE,
        OFF_CAMPUS,
        POLICY,
        LOGOUT
    }

    public interface NavigationListener {
        void onNavigate(NavItem item);
    }

    private NavigationListener navigationListener;
    private NavItem activeItem;

    public SidebarPanel(NavItem activeItem) {
        this.activeItem = activeItem;

        setLayout(new GridLayout(8, 1, 5, 10));
        setBackground(BaseFrame.COLOR_SIDEBAR_BG);
        setPreferredSize(new Dimension(200, 0));

        add(createButton("Dashboard", NavItem.DASHBOARD));
        add(createButton("Recruitments", NavItem.RECRUITMENTS));
        add(createButton("Applications", NavItem.APPLICATIONS));
        add(createButton("Profile", NavItem.PROFILE));
        add(createButton("Off-Campus Jobs", NavItem.OFF_CAMPUS));
        add(createButton("Policy", NavItem.POLICY));
        add(createButton("Log Out", NavItem.LOGOUT));
    }

    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }

    private JButton createButton(String text, NavItem item) {

        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));

        if (item == activeItem) {
            button.setBackground(BaseFrame.COLOR_SIDEBAR_ACTIVE);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
        }

        button.addActionListener(e -> {
            if (navigationListener != null) {
                navigationListener.onNavigate(item);
            }
        });

        return button;
    }
}
