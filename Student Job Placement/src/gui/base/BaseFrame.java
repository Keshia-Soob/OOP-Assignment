package gui.base;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * BaseFrame = your "base.html" in Swing.
 * All windows extend this to automatically get the same header + sidebar.
 */
public abstract class BaseFrame extends JFrame {

    // Theme (keep consistent across the whole project)
    public static final Color COLOR_PRIMARY = new Color(52, 102, 160);     // header blue
    public static final Color COLOR_SIDEBAR_BG = new Color(235, 242, 250); // light blue/gray
    public static final Color COLOR_SIDEBAR_ACTIVE = new Color(66, 133, 244);
    public static final Color COLOR_TEXT_DARK = new Color(30, 30, 30);

    protected HeaderPanel header;
    protected SidebarPanel sidebar;

    protected JPanel contentWrapper; // area where each frame injects its unique content

    public BaseFrame(String windowTitle, SidebarPanel.NavItem activeItem) {
        super(windowTitle);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
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

        // Inject frame-specific UI
        contentWrapper.add(buildContent(), BorderLayout.CENTER);

        // Hook up navigation actions (you can replace these with real navigation later)
        wireDefaultNavigation();
    }

    /** Each screen provides its own center content */
    protected abstract JComponent buildContent();

    /** Navigation wiring: Replace with your app routing later */
    private void wireDefaultNavigation() {
        sidebar.setNavigationListener(item -> {
            // This is demo routing only. Replace with real screens.
            JOptionPane.showMessageDialog(
                    this,
                    "Navigate to: " + item,
                    "Navigation",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}
