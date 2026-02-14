package gui.base;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

/** Reusable sidebar with navigation buttons */
public class SidebarPanel extends JPanel {

    public enum NavItem {
        DASHBOARD, PROFILE, RECRUITMENTS, APPLICATIONS, OFFCAMPUS, POLICY
    }

    private Consumer<NavItem> navigationListener;
    private final Map<NavItem, JButton> buttons = new EnumMap<>(NavItem.class);

    public SidebarPanel(NavItem activeItem) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BaseFrame.COLOR_SIDEBAR_BG);
        setPreferredSize(new Dimension(200, 1));
        setBorder(new EmptyBorder(12, 10, 12, 10));

        addNavButton("Dashboard", NavItem.DASHBOARD, UIManager.getIcon("FileView.directoryIcon"));
        add(Box.createVerticalStrut(6));
        addNavButton("Profile", NavItem.PROFILE, UIManager.getIcon("FileView.fileIcon"));
        add(Box.createVerticalStrut(6));
        addNavButton("Recruitments", NavItem.RECRUITMENTS, UIManager.getIcon("OptionPane.questionIcon"));
        add(Box.createVerticalStrut(6));
        addNavButton("Applications", NavItem.APPLICATIONS, UIManager.getIcon("OptionPane.informationIcon"));
        add(Box.createVerticalStrut(6));
        addNavButton("Off-Campus Jobs", NavItem.OFFCAMPUS, UIManager.getIcon("FileChooser.newFolderIcon"));
        add(Box.createVerticalStrut(6));
        addNavButton("Policy", NavItem.POLICY, UIManager.getIcon("FileView.hardDriveIcon"));

        add(Box.createVerticalGlue());

        setActive(activeItem);
    }

    private void addNavButton(String text, NavItem item, Icon icon) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIcon(icon);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBackground(BaseFrame.COLOR_SIDEBAR_BG);
        btn.setForeground(BaseFrame.COLOR_TEXT_DARK);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));

        btn.setBorder(new EmptyBorder(10, 12, 10, 12));

        btn.addActionListener(e -> {
            setActive(item);
            if (navigationListener != null) navigationListener.accept(item);
        });

        buttons.put(item, btn);
        add(btn);
    }

    /** Highlight active button */
    public void setActive(NavItem activeItem) {
        for (Map.Entry<NavItem, JButton> entry : buttons.entrySet()) {
            JButton b = entry.getValue();
            if (entry.getKey() == activeItem) {
                b.setBackground(BaseFrame.COLOR_SIDEBAR_ACTIVE);
                b.setForeground(Color.WHITE);
            } else {
                b.setBackground(BaseFrame.COLOR_SIDEBAR_BG);
                b.setForeground(BaseFrame.COLOR_TEXT_DARK);
            }
        }
    }

    public void setNavigationListener(Consumer<NavItem> listener) {
        this.navigationListener = listener;
    }
}
