package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ApplicationsFrame extends BaseFrame {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> filterCombo;
    private JTextField searchField;

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

        filterCombo = new JComboBox<>(new String[] {"All", "Applied", "Shortlisted", "Selected", "Rejected"});
        filterCombo.setPreferredSize(new Dimension(140, 28));

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);

        top.add(filterPanel, BorderLayout.EAST);

        // ---- Table panel ----
        model = new DefaultTableModel(
                new Object[]{"Company", "Job Title", "Date Applied", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // Sample rows (replace with DB results later)
        model.addRow(new Object[]{"Infosys", "Data Scientist", "May 12, 2024", "Account"});
        model.addRow(new Object[]{"Amazon", "UI Designer", "May 21, 2024", "Offence"}); // sample text

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        // ---- Bottom row: Search + Add Off-campus Job ----
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(4, 0, 0, 0));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(260, 32));
        searchField.setToolTipText("Search by company or job title...");

        JButton addOffCampusBtn = new JButton("Add Off-Campus Job");
        addOffCampusBtn.setPreferredSize(new Dimension(190, 34));
        addOffCampusBtn.setFocusPainted(false);

        // action (demo)
        addOffCampusBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Open Off-Campus Form (not implemented yet).")
        );

        bottom.add(searchField, BorderLayout.CENTER);
        bottom.add(addOffCampusBtn, BorderLayout.EAST);

        // ---- Add all sections ----
        root.add(top, BorderLayout.NORTH);
        root.add(sp, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        // Filter/search hooks (demo-level)
        wireLocalActions();

        return root;
    }

    private void wireLocalActions() {
        filterCombo.addActionListener(e -> {
            // Later: reload from DB with WHERE status = ?
            // For now: show what was selected
            // (Don't block the UI with DB queries; use DAO/Service later)
        });

        searchField.addActionListener(e -> {
            // Later: query DB (LIKE company/jobTitle)
        });
    }
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new ApplicationsFrame().setVisible(true));
    }

}
