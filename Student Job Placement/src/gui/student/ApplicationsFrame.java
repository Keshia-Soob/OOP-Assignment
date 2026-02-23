package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ApplicationsFrame extends BaseFrame {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> filterCombo;

    public ApplicationsFrame() {
        super("Applications", SidebarPanel.NavItem.APPLICATIONS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);

        // ---- Top row: Title + Filter ----
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

        filterCombo = new JComboBox<>(new String[] {
                "All", "Applied", "Shortlisted", "Selected", "Rejected"
        });
        filterCombo.setPreferredSize(new Dimension(140, 28));

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);

        top.add(filterPanel, BorderLayout.EAST);

        // ---- Table panel ----
        model = new DefaultTableModel(
                new Object[]{"Company", "Job Title", "Date Applied", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        // ---- Add sections ----
        root.add(top, BorderLayout.NORTH);
        root.add(sp, BorderLayout.CENTER);

        wireLocalActions();

        return root;
    }

    private void wireLocalActions() {
        filterCombo.addActionListener(e -> {
            // Later: reload from DB with WHERE status = ?
        });
    }

    
}