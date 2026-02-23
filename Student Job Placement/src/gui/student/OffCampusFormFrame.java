package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class OffCampusFormFrame extends BaseFrame {

    private JTextField txtCompany;
    private JTextField txtJobTitle;
    private JTextField txtDateApplied;
    private JComboBox<String> comboStatus;

    public OffCampusFormFrame() {
        super("Add Off-Campus Job", SidebarPanel.NavItem.OFF_CAMPUS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);

        // ---- Title ----
        JLabel title = new JLabel("Add Off-Campus Job");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);

        // ---- Form Card ----
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Company
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(new JLabel("Company"), gbc);

        txtCompany = new JTextField();
        styleField(txtCompany);
        gbc.gridy = 1;
        card.add(txtCompany, gbc);

        // Job Title
        gbc.gridy = 2;
        card.add(new JLabel("Job Title"), gbc);

        txtJobTitle = new JTextField();
        styleField(txtJobTitle);
        gbc.gridy = 3;
        card.add(txtJobTitle, gbc);

        // Date Applied
        gbc.gridy = 4;
        card.add(new JLabel("Date Applied"), gbc);

        txtDateApplied = new JTextField();
        txtDateApplied.setToolTipText("Use format YYYY-MM-DD");
        styleField(txtDateApplied);
        gbc.gridy = 5;
        card.add(txtDateApplied, gbc);

        // Status
        gbc.gridy = 6;
        card.add(new JLabel("Status"), gbc);

        comboStatus = new JComboBox<>(new String[]{"Applied", "Shortlisted", "Selected", "Rejected"});
        comboStatus.setPreferredSize(new Dimension(1, 32));
        styleField(comboStatus);
        gbc.gridy = 7;
        card.add(comboStatus, gbc);

        // ---- Buttons Row ----
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        JButton btnSave = new JButton("Save");

        btnCancel.setFocusPainted(false);
        btnSave.setFocusPainted(false);

        btnSave.setBackground(new Color(58, 102, 171));
        btnSave.setForeground(Color.WHITE);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);

        btnCancel.addActionListener(e -> goBack());
        btnSave.addActionListener(e -> onSave());

        buttons.add(btnCancel);
        buttons.add(btnSave);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card, BorderLayout.NORTH);

        root.add(top, BorderLayout.NORTH);
        root.add(centerWrap, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);

        return root;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new LineBorder(new Color(200, 210, 225), 1, true));
        field.setPreferredSize(new Dimension(1, 32));
    }

    private void onSave() {
        String company = txtCompany.getText().trim();
        String jobTitle = txtJobTitle.getText().trim();
        String dateApplied = txtDateApplied.getText().trim();
        String status = (String) comboStatus.getSelectedItem();

        if (company.isEmpty() || jobTitle.isEmpty() || dateApplied.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Saved (demo)\n\nCompany: " + company +
                        "\nJob Title: " + jobTitle +
                        "\nDate Applied: " + dateApplied +
                        "\nStatus: " + status
        );

        goBack();
    }

    private void goBack() {
        new OffCampusFrame().setVisible(true);
        dispose();
    }

}