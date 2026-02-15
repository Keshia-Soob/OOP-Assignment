package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class JobDetailsFrame extends BaseFrame {

    private JTable jobTable;
    private DefaultTableModel model;

    private JComboBox<String> filterStatus;
    private JComboBox<String> filterSection;

    public JobDetailsFrame() {
        super("Job Details", SidebarPanel.NavItem.RECRUITMENTS); // highlight Recruitments in sidebar
    }

    @Override
    protected JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBackground(Color.WHITE);

        // -------- Page Title --------
        JLabel title = new JLabel("Job Details");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(40, 40, 40));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(buildFilterBar(), BorderLayout.CENTER);

        // -------- Table --------
        JScrollPane tablePanel = buildTablePanel();

        // -------- Apply Button (big) --------
        JButton applyBtn = new JButton("Apply for Job");
        applyBtn.setFocusPainted(false);
        applyBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        applyBtn.setPreferredSize(new Dimension(1, 44));
        applyBtn.setBackground(new Color(78, 121, 196));
        applyBtn.setForeground(Color.WHITE);

        applyBtn.addActionListener(e -> onApply());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(6, 0, 0, 0));
        bottom.add(applyBtn, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);
        root.add(tablePanel, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new GridBagLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(10, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        // Filter 1 (left)
        filterStatus = new JComboBox<>(new String[]{"Filter : All", "Applied", "Offered", "Selected", "Rejected"});
        filterStatus.setPreferredSize(new Dimension(220, 32));

        // Filter 2 (right)
        filterSection = new JComboBox<>(new String[]{"Filter : A1", "A2", "B1", "B2"});
        filterSection.setPreferredSize(new Dimension(220, 32));

        gbc.gridx = 0;
        gbc.weightx = 1;
        bar.add(filterStatus, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        bar.add(filterSection, gbc);

        // Hook (demo)
        filterStatus.addActionListener(e -> {
            // Later: reload table from DB based on selected status
        });
        filterSection.addActionListener(e -> {
            // Later: reload based on section filter
        });

        return bar;
    }

    private JScrollPane buildTablePanel() {
        // Columns include a final "Action" icon column
        model = new DefaultTableModel(new Object[]{"Company", "Job Title", "Status", ""}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Only action column clickable
                return col == 3;
            }
        };

        // Sample rows (replace with DB later)
        model.addRow(new Object[]{"Infosys", "Data Scientist", "Applied", ">"});
        model.addRow(new Object[]{"Google", "UX Designer", "Offered", ">"});

        jobTable = new JTable(model);
        jobTable.setRowHeight(44);
        jobTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        jobTable.setShowVerticalLines(false);
        jobTable.setShowHorizontalLines(true);
        jobTable.setGridColor(new Color(225, 225, 225));

        JTableHeader th = jobTable.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setBackground(new Color(245, 245, 245));
        th.setForeground(new Color(60, 60, 60));

        // Column widths to resemble your UI
        TableColumnModel cm = jobTable.getColumnModel();
        cm.getColumn(0).setPreferredWidth(180); // Company
        cm.getColumn(1).setPreferredWidth(220); // Job Title
        cm.getColumn(2).setPreferredWidth(130); // Status
        cm.getColumn(3).setPreferredWidth(40);  // Action icon

        // Status styling (badge-like)
        cm.getColumn(2).setCellRenderer(new StatusCellRenderer());

        // Action icon column (clickable)
        cm.getColumn(3).setCellRenderer(new ActionIconRenderer());
        cm.getColumn(3).setCellEditor(new ActionIconEditor(new JCheckBox()));

        JScrollPane sp = new JScrollPane(jobTable);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        sp.getViewport().setBackground(Color.WHITE);

        return sp;
    }

    private void onApply() {
        int row = jobTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a job row first.");
            return;
        }

        String company = model.getValueAt(row, 0).toString();
        String jobTitle = model.getValueAt(row, 1).toString();

        // Later: call Controller -> Service -> DAO to insert application
        JOptionPane.showMessageDialog(this,
                "Applied to: " + company + " - " + jobTitle + "\n(Connect to DB later)");
    }

    // ---------------------------
    // RENDERERS / EDITORS
    // ---------------------------

    /** Renders the status column like a small pill/badge */
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            lbl.setOpaque(true);

            String status = (value == null) ? "" : value.toString().toLowerCase();

            Color bg;
            Color fg = Color.WHITE;

            if (status.contains("applied")) bg = new Color(88, 128, 200);
            else if (status.contains("offered")) bg = new Color(60, 160, 110);
            else if (status.contains("rejected")) bg = new Color(200, 70, 70);
            else if (status.contains("selected")) bg = new Color(120, 90, 200);
            else bg = new Color(120, 120, 120);

            if (isSelected) {
                // keep selection readable
                lbl.setBackground(bg.darker());
            } else {
                lbl.setBackground(bg);
            }
            lbl.setForeground(fg);
            lbl.setBorder(new EmptyBorder(6, 10, 6, 10));

            return lbl;
        }
    }

    /** Shows a small icon-like action in the last column */
    private static class ActionIconRenderer extends DefaultTableCellRenderer {
        private final JLabel iconLabel = new JLabel();

        public ActionIconRenderer() {
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setOpaque(true);
            iconLabel.setBackground(Color.WHITE);
            iconLabel.setForeground(new Color(80, 80, 80));
            iconLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            iconLabel.setText("↗"); // simple “open/details” icon
            iconLabel.setBackground(isSelected ? new Color(245, 245, 245) : Color.WHITE);
            return iconLabel;
        }
    }

    /** Makes the action icon column clickable */
    private class ActionIconEditor extends DefaultCellEditor {
        private final JButton button = new JButton("↗");
        private int currentRow = -1;

        public ActionIconEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setHorizontalAlignment(SwingConstants.CENTER);

            button.addActionListener(e -> {
                stopCellEditing();
                openDetailsForRow(currentRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }
    }

    private void openDetailsForRow(int row) {
        if (row < 0) return;

        String company = model.getValueAt(row, 0).toString();
        String jobTitle = model.getValueAt(row, 1).toString();
        String status = model.getValueAt(row, 2).toString();

        JOptionPane.showMessageDialog(this,
                "Job Details\n\nCompany: " + company +
                        "\nJob Title: " + jobTitle +
                        "\nStatus: " + status +
                        "\n\n(Replace this popup with a full details screen later)");
    }

    // Quick test runner (optional). Remove when you run from App.java
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobDetailsFrame().setVisible(true));
    }
}
