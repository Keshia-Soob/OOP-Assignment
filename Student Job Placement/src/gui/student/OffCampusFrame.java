package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import model.OffCampusApplication;
import service.OffCampusService;
import util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OffCampusFrame extends BaseFrame {

    private JTable            table;
    private DefaultTableModel model;
    private JComboBox<String> filterCombo;

    // Keeps the loaded list so buttons can reference the selected row
    private List<OffCampusApplication> currentList;

    public OffCampusFrame() {
        super("Off-Campus Jobs", SidebarPanel.NavItem.OFF_CAMPUS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);

        // ── Top: title + filter ───────────────────────────────────────────────
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

        filterCombo = new JComboBox<>(
                new String[]{"All", "Applied", "Shortlisted", "Selected", "Rejected"});
        filterCombo.setPreferredSize(new Dimension(140, 28));

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        top.add(filterPanel, BorderLayout.EAST);

        // ── One-offer banner ──────────────────────────────────────────────────
        JPanel topStack = new JPanel(new BorderLayout(0, 6));
        topStack.setOpaque(false);
        topStack.add(top, BorderLayout.NORTH);

        if (OffCampusService.isAlreadySelected(Session.getUserId())) {
            JPanel banner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            banner.setBackground(new Color(255, 243, 205));
            banner.setBorder(new LineBorder(new Color(230, 180, 30), 1, true));

            JLabel icon = new JLabel("⚠");
            icon.setFont(new Font("SansSerif", Font.BOLD, 14));
            icon.setForeground(new Color(160, 100, 0));

            JLabel msg = new JLabel(
                    "You have already been selected for a placement. " +
                    "You cannot add further applications.");
            msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
            msg.setForeground(new Color(100, 60, 0));

            banner.add(icon);
            banner.add(msg);
            topStack.add(banner, BorderLayout.SOUTH);
        }

        // ── Table ─────────────────────────────────────────────────────────────
        model = new DefaultTableModel(
                new Object[]{"Company", "Job Title", "Date Applied", "Status", "Notes"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        // ── Bottom buttons ────────────────────────────────────────────────────
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Left side: Delete button
        JButton deleteBtn = buildButton("Delete Entry", new Color(190, 50, 50));
        deleteBtn.addActionListener(e -> onDelete());

        JPanel leftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftBtns.setOpaque(false);
        leftBtns.add(deleteBtn);

        // Right side: Add button (disabled when student is already selected)
        boolean blocked = OffCampusService.isAlreadySelected(Session.getUserId());

        JButton addBtn = buildButton("Add Off-Campus Job", new Color(58, 102, 171));
        addBtn.setEnabled(!blocked);
        if (blocked) {
            addBtn.setBackground(new Color(160, 160, 160));
            addBtn.setToolTipText(
                    "You cannot add a new application — you are already placed.");
        }
        addBtn.addActionListener(e -> {
            new OffCampusFormFrame().setVisible(true);
            dispose();
        });

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightBtns.setOpaque(false);
        rightBtns.add(addBtn);

        bottom.add(leftBtns,  BorderLayout.WEST);
        bottom.add(rightBtns, BorderLayout.EAST);

        // ── Assemble ──────────────────────────────────────────────────────────
        root.add(topStack, BorderLayout.NORTH);
        root.add(sp,       BorderLayout.CENTER);
        root.add(bottom,   BorderLayout.SOUTH);

        filterCombo.addActionListener(e -> loadTable());
        loadTable();

        return root;
    }

    // ── Load table ────────────────────────────────────────────────────────────

    private void loadTable() {
        model.setRowCount(0);

        int userId          = Session.getUserId();
        String selectedStatus = (String) filterCombo.getSelectedItem();

        currentList = OffCampusService.getApplications(userId, selectedStatus);

        if (currentList.isEmpty()) {
            model.addRow(new Object[]{"No records found", "", "", "", ""});
            return;
        }

        for (OffCampusApplication app : currentList) {
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

    // ── Delete handler ────────────────────────────────────────────────────────

    private void onDelete() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an entry to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Guard: "No records found" placeholder row
        if (currentList == null || currentList.isEmpty()) return;

        OffCampusApplication app = getMatchingApp(viewRow);
        if (app == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete your off-campus entry for \"" + app.getJobTitle() +
                "\" at " + app.getCompany() + "?\n\nThis cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        String error = OffCampusService.delete(app.getOffAppId(), Session.getUserId());

        if (error == null) {
            JOptionPane.showMessageDialog(this,
                    "Entry deleted successfully.",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this,
                    error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Maps the selected table row back to the OffCampusApplication object,
     * accounting for the active status filter.
     */
    private OffCampusApplication getMatchingApp(int viewRow) {
        String filter = (String) filterCombo.getSelectedItem();
        int count = 0;
        for (OffCampusApplication app : currentList) {
            if (!"All".equals(filter) &&
                !app.getStatus().equalsIgnoreCase(filter)) continue;
            if (count == viewRow) return app;
            count++;
        }
        // If filter is "All", currentList rows map 1-to-1 to table rows
        if (viewRow < currentList.size()) return currentList.get(viewRow);
        return null;
    }

    private JButton buildButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 34));
        return btn;
    }
}