package gui.base;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BaseFrame extends JFrame {

    // Theme
    public static final Color COLOR_PRIMARY = new Color(52, 102, 160);
    public static final Color COLOR_SIDEBAR_BG = new Color(235, 242, 250);
    public static final Color COLOR_SIDEBAR_ACTIVE = new Color(66, 133, 244);
    public static final Color COLOR_TEXT_DARK = new Color(30, 30, 30);

    protected HeaderPanel header;
    protected SidebarPanel sidebar;
    protected JPanel contentWrapper;

    public BaseFrame(String windowTitle, SidebarPanel.NavItem activeItem) {
        super(windowTitle);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        header = new HeaderPanel("Placement & Job Recruitment System");
        add(header, BorderLayout.NORTH);

        // Sidebar
        sidebar = new SidebarPanel(activeItem);
        add(sidebar, BorderLayout.WEST);

        // Content wrapper
        contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(18, 18, 18, 18));
        contentWrapper.setBackground(Color.WHITE);
        add(contentWrapper, BorderLayout.CENTER);

        contentWrapper.add(buildContent(), BorderLayout.CENTER);

        wireNavigation();
    }

    protected abstract JComponent buildContent();

    private void wireNavigation() {

        sidebar.setNavigationListener(item -> {

            JFrame nextFrame = null;

            switch (item) {

                case DASHBOARD:
                    nextFrame = new gui.student.StudentDashboardFrame();
                    break;

                case RECRUITMENTS:
                    nextFrame = new gui.student.RecruitmentFrame();
                    break;

                case APPLICATIONS:
                    nextFrame = new gui.student.ApplicationsFrame();
                    break;

                case OFF_CAMPUS:
                    nextFrame = new gui.student.OffCampusFrame();
                    break;

                case PROFILE:
                    JOptionPane.showMessageDialog(this, "Profile not implemented yet.");
                    return;

                case POLICY:
                    nextFrame = new gui.student.StudentPolicyFrame();
                    return;

                case LOGOUT:
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to log out?",
                            "Confirm Logout",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        // ✅ Go back to LoginFrame
                        new gui.auth.LoginFrame().setVisible(true);
                        this.dispose();
                    }
                    return;
            }

            if (nextFrame != null) {
                nextFrame.setVisible(true);
                this.dispose();
            }
        });
    }
}