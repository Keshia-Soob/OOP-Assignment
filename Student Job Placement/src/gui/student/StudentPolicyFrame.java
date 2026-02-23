package gui.student;


import gui.base.BaseFrame;
import gui.base.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentPolicyFrame extends BaseFrame {

    private JRadioButton agreeRadio;
    private JRadioButton notAgreeRadio;
    private JButton continueButton;

    public StudentPolicyFrame() {
        super("Policy", SidebarPanel.NavItem.POLICY);
    }

    @Override
    protected JComponent buildContent() {

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---- Title ----
        JLabel title = new JLabel("Student Placement Policy");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);

        // ---- Policy Text ----
        JTextArea policyText = new JTextArea(getPolicyContent());
        policyText.setEditable(false);
        policyText.setLineWrap(true);
        policyText.setWrapStyleWord(true);
        policyText.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(policyText);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // ---- Agreement Section ----
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setOpaque(false);

        agreeRadio = new JRadioButton("I Agree");
        notAgreeRadio = new JRadioButton("I Do Not Agree");

        // Default = Not Agree
        notAgreeRadio.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(agreeRadio);
        group.add(notAgreeRadio);

        radioPanel.add(agreeRadio);
        radioPanel.add(notAgreeRadio);

        continueButton = new JButton("Continue");
        continueButton.setEnabled(false);

        // ---- Logic ----
        agreeRadio.addActionListener(e -> continueButton.setEnabled(true));
        notAgreeRadio.addActionListener(e -> continueButton.setEnabled(false));

        continueButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Policy accepted successfully.",
                    "Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new StudentDashboardFrame().setVisible(true);
            this.dispose();
        });

        bottom.add(radioPanel, BorderLayout.WEST);
        bottom.add(continueButton, BorderLayout.EAST);

        // ---- Assemble ----
        root.add(top, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    private String getPolicyContent() {
        return """
        1. Students must maintain required academic eligibility.

        2. Students may only apply to companies they qualify for.

        3. Once an offer is accepted, withdrawal is not permitted without approval.

        4. Providing false information will result in disciplinary action.

        5. Students must respect recruitment procedures and deadlines.

        6. The placement office reserves the right to update policies at any time.

        Please read carefully before selecting your decision.
        """;
    }
}