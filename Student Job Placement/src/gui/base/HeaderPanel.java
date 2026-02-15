package gui.base;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Reusable top header used by every frame */
public class HeaderPanel extends JPanel {

    private JLabel titleLabel;

    public HeaderPanel(String title) {
        setLayout(new BorderLayout());
        setBackground(BaseFrame.COLOR_PRIMARY);
        setBorder(new EmptyBorder(10, 14, 10, 14));
        setPreferredSize(new Dimension(100, 56));

        // Left: small icon + "Dashboard" label (like your screenshot)
        JLabel left = new JLabel("  Dashboard");
        left.setForeground(Color.WHITE);
        left.setFont(new Font("SansSerif", Font.BOLD, 13));
        left.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        add(left, BorderLayout.WEST);

        // Center: System Title
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(titleLabel, BorderLayout.CENTER);

        // Right spacing (optional placeholder)
        JLabel right = new JLabel(" ");
        right.setPreferredSize(new Dimension(80, 1));
        add(right, BorderLayout.EAST);
    }

    public void setTitle(String newTitle) {
        titleLabel.setText(newTitle);
    }
}
