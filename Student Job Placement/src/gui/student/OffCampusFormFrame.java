package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import service.OffCampusService;
import util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class OffCampusFormFrame extends BaseFrame {

    private JTextField     txtCompany;
    private JTextField     txtJobTitle;
    private JTextField     txtNotes;
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
        gbc.insets  = new Insets(8, 10, 8, 10);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx   = 0;

        // Company
        gbc.gridy = 0; card.add(makeLabel("Company *"), gbc);
        txtCompany = new JTextField();
        styleField(txtCompany);
        gbc.gridy = 1; card.add(txtCompany, gbc);

        // Job Title
        gbc.gridy = 2; card.add(makeLabel("Job Title *"), gbc);
        txtJobTitle = new JTextField();
        styleField(txtJobTitle);
        gbc.gridy = 3; card.add(txtJobTitle, gbc);

        // Status
        gbc.gridy = 4; card.add(makeLabel("Status"), gbc);
        comboStatus = new JComboBox<>(new String[]{"Applied", "Shortlisted", "Selected", "Rejected"});
        styleField(comboStatus);
        gbc.gridy = 5; card.add(comboStatus, gbc);

        // Notes
        gbc.gridy = 6; card.add(makeLabel("Notes (optional)"), gbc);
        txtNotes = new JTextField();
        styleField(txtNotes);
        gbc.gridy = 7; card.add(txtNotes, gbc);

        // ---- Buttons ----
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        JButton btnSave   = new JButton("Save");

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

        root.add(top,        BorderLayout.NORTH);
        root.add(centerWrap, BorderLayout.CENTER);
        root.add(buttons,    BorderLayout.SOUTH);

        return root;
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return lbl;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new LineBorder(new Color(200, 210, 225), 1, true));
        field.setPreferredSize(new Dimension(1, 32));
    }

    private void onSave() {
        String company  = txtCompany.getText().trim();
        String jobTitle = txtJobTitle.getText().trim();
        String status   = (String) comboStatus.getSelectedItem();
        String notes    = txtNotes.getText().trim();

        // Use the logged-in user's ID from the session
        int userId = Session.getUserId();

        String error = OffCampusService.add(userId, company, jobTitle, status, notes);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Off-campus job saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        goBack();
    }

    private void goBack() {
        new OffCampusFrame().setVisible(true);
        dispose();
    }
}