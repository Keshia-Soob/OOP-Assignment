package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import model.OffCampusApplication;
import service.OffCampusService;
import util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OffCampusFrame extends BaseFrame {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> filterCombo;

    public OffCampusFrame() {
        super("Off-Campus Jobs", SidebarPanel.NavItem.OFF_CAMPUS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);

        // ---- Top: Title + Filter ----
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("Off-Campus Jobs");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));
        top.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        filterCombo = new JComboBox<>(new String[]{"All", "Applied", "Shortlisted", "Selected", "Rejected"});
        filterCombo.setPreferredSize(new Dimension(140, 28));

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        top.add(filterPanel, BorderLayout.EAST);

        // ---- Table ----
        model = new DefaultTableModel(
                new Object[]{"Company", "Job Title", "Date Applied", "Status", "Notes"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        // ---- Bottom: Add button ----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(6, 0, 0, 0));

        JButton addBtn = new JButton("Add Off-Campus Job");
        addBtn.setPreferredSize(new Dimension(210, 36));
        addBtn.setBackground(new Color(58, 102, 171));
        addBtn.setForeground(Color.WHITE);
        addBtn.setOpaque(true);
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addBtn.addActionListener(e -> {
            new OffCampusFormFrame().setVisible(true);
            dispose();
        });

        bottom.add(addBtn);

        // ---- Assemble ----
        root.add(top,    BorderLayout.NORTH);
        root.add(sp,     BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        // Wire filter then load
        filterCombo.addActionListener(e -> loadTable());
        loadTable();

        return root;
    }

    private void loadTable() {

        model.setRowCount(0);

        int userId = Session.getUserId();
        String selectedStatus = (String) filterCombo.getSelectedItem();

        List<OffCampusApplication> apps =
                OffCampusService.getApplications(userId, selectedStatus);

        if (apps.isEmpty()) {
            model.addRow(new Object[]{"No records found", "", "", "", ""});
            return;
        }

        for (OffCampusApplication app : apps) {

            String dateStr = (app.getDateApplied() != null)
                    ? app.getDateApplied().toString().substring(0, 10)
                    : "—";

            String notes = (app.getNotes() != null && !app.getNotes().isBlank())
                    ? app.getNotes()
                    : "—";

            model.addRow(new Object[]{
                    app.getCompany(),
                    app.getJobTitle(),
                    dateStr,
                    app.getStatus(),
                    notes
            });
        }
    }
}