package gui.student;

import dao.JobDAO;
import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import service.ApplicationService;
import util.Session;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Job;

public class RecruitmentFrame extends BaseFrame {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> companyFilter;
    private JComboBox<String> jobFilter;
    private JComboBox<String> cgpaFilter;

    private JLabel  pageLabel;
    private JButton btnPrevious;
    private JButton btnNext;

    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private int totalPages;

    private List<Job> allJobs;
    private List<Job> filteredJobs;

    public RecruitmentFrame() {
        super("Recruitments", SidebarPanel.NavItem.RECRUITMENTS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel titleLabel = new JLabel("Recruitments");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // ── One-offer banner (shown only when the student is already Selected) ─
        if (ApplicationService.countByStatus(Session.getUserId(), "Selected") > 0) {
            JPanel banner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            banner.setBackground(new Color(255, 243, 205));
            banner.setBorder(BorderFactory.createLineBorder(
                    new Color(230, 180, 30), 1, true));

            JLabel icon = new JLabel("⚠");
            icon.setFont(new Font("SansSerif", Font.BOLD, 14));
            icon.setForeground(new Color(160, 100, 0));

            JLabel msg = new JLabel(
                    "You have already been selected for a position. " +
                    "You cannot apply to further jobs.");
            msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
            msg.setForeground(new Color(100, 60, 0));

            banner.add(icon);
            banner.add(msg);
            topPanel.add(banner, BorderLayout.SOUTH);
        }

        // ── Filters ───────────────────────────────────────────────────────────
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(Color.WHITE);

        companyFilter = new JComboBox<>();
        jobFilter     = new JComboBox<>();
        cgpaFilter    = new JComboBox<>();

        filterPanel.add(new JLabel("Company:"));
        filterPanel.add(companyFilter);
        filterPanel.add(new JLabel("Job Title:"));
        filterPanel.add(jobFilter);
        filterPanel.add(new JLabel("CGPA:"));
        filterPanel.add(cgpaFilter);

        JButton resetButton = new JButton("Reset Filters");
        filterPanel.add(resetButton);
        resetButton.addActionListener(e -> resetFilters());

        topPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ── Load data ─────────────────────────────────────────────────────────
        allJobs      = JobDAO.getAllJobs();
        filteredJobs = new ArrayList<>(allJobs);
        populateFilters();

        // ── Table ─────────────────────────────────────────────────────────────
        model = new DefaultTableModel(
                new String[]{"Company", "Job Title", "Minimum CGPA", "Application"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // ── Click handler ─────────────────────────────────────────────────────
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row == -1 || col != 3) return;

                int idx = (currentPage - 1) * rowsPerPage + row;
                if (idx < 0 || idx >= filteredJobs.size()) return;

                Job job = filteredJobs.get(idx);

                String details =
                        "Key Responsibilities:\n" + job.getKeyResponsibilities() + "\n\n" +
                        "Location:\n"             + job.getLocation()            + "\n\n" +
                        "Salary:\n"               + job.getSalary()              + "\n\n" +
                        "Duration:\n"             + job.getDuration()            + "\n\n" +
                        "Contact Email:\n"        + job.getContactEmail()        + "\n\n" +
                        "Contact Number:\n"       + job.getContactNumber()       + "\n\n" +
                        "Starting Date:\n"        + job.getStartDate();

                int choice = JOptionPane.showOptionDialog(
                        RecruitmentFrame.this,
                        details,
                        job.getCompany() + " - " + job.getJobTitle(),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Apply", "Close"},
                        "Apply"
                );

                if (choice == 0) handleApply(job);
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table,                  BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // ── Pagination ────────────────────────────────────────────────────────
        JPanel paginationPanel = new JPanel();
        paginationPanel.setBackground(Color.WHITE);

        btnPrevious = new JButton("Previous");
        btnNext     = new JButton("Next");
        pageLabel   = new JLabel();

        paginationPanel.add(btnPrevious);
        paginationPanel.add(pageLabel);
        paginationPanel.add(btnNext);
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        btnPrevious.addActionListener(e -> { if (currentPage > 1)         { currentPage--; updateTable(); } });
        btnNext    .addActionListener(e -> { if (currentPage < totalPages) { currentPage++; updateTable(); } });

        updateTable();
        return mainPanel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Apply — checks CGPA eligibility first, then handles all result codes
    // ─────────────────────────────────────────────────────────────────────────

    private void handleApply(Job job) {

        // ---- CGPA CHECK ----
        double studentCgpa  = Session.getCurrentUser().getCgpa();
        double requiredCgpa = job.getMinCgpa();

        if (studentCgpa < requiredCgpa) {
            JOptionPane.showMessageDialog(
                    this,
                    "You are not eligible for this position due to low CGPA.\n\n" +
                    "Required CGPA : " + requiredCgpa + "\n" +
                    "Your CGPA     : " + studentCgpa,
                    "Not Eligible",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = Session.getUserId();
        int result = ApplicationService.apply(userId, job.getJobId());

        switch (result) {

            case ApplicationService.APPLY_SUCCESS:
                JOptionPane.showMessageDialog(
                        this,
                        "Application submitted successfully!\n" +
                        "You can view it in the Applications section.",
                        "Applied",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case ApplicationService.APPLY_ALREADY_APPLIED:
                JOptionPane.showMessageDialog(
                        this,
                        "You have already applied for this position.",
                        "Already Applied",
                        JOptionPane.WARNING_MESSAGE);
                break;

            case ApplicationService.APPLY_ALREADY_SELECTED:
                JOptionPane.showMessageDialog(
                        this,
                        "You have already been selected for a placement.\n\n" +
                        "The system allows only one active placement per student\n" +
                        "so that other students have a fair chance.",
                        "Application Not Allowed",
                        JOptionPane.WARNING_MESSAGE);
                break;

            default:
                JOptionPane.showMessageDialog(
                        this,
                        "An error occurred while submitting your application.\n" +
                        "Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Filters / pagination
    // ─────────────────────────────────────────────────────────────────────────

    private void populateFilters() {

        companyFilter.addItem("All");
        jobFilter    .addItem("All");
        cgpaFilter   .addItem("All");

        for (String r : new String[]{"0-1", "1-2", "2-3", "3-4", "4-5"})
            cgpaFilter.addItem(r);

        Set<String> companies = new LinkedHashSet<>();
        Set<String> jobs      = new LinkedHashSet<>();
        for (Job job : allJobs) { companies.add(job.getCompany()); jobs.add(job.getJobTitle()); }
        companies.forEach(companyFilter::addItem);
        jobs     .forEach(jobFilter::addItem);

        companyFilter.addActionListener(e -> applyFilters());
        jobFilter    .addActionListener(e -> applyFilters());
        cgpaFilter   .addActionListener(e -> applyFilters());
    }

    private void applyFilters() {

        String selCompany = companyFilter.getSelectedItem().toString();
        String selJob     = jobFilter    .getSelectedItem().toString();
        String selCgpa    = cgpaFilter   .getSelectedItem().toString();

        filteredJobs.clear();

        for (Job job : allJobs) {
            boolean match = true;

            if (!selCompany.equals("All") && !job.getCompany() .equals(selCompany)) match = false;
            if (!selJob    .equals("All") && !job.getJobTitle().equals(selJob))     match = false;
            if (!selCgpa   .equals("All")) {
                String[] range = selCgpa.split("-");
                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);
                if (job.getMinCgpa() < min || job.getMinCgpa() >= max)             match = false;
            }

            if (match) filteredJobs.add(job);
        }

        currentPage = 1;
        updateTable();
    }

    private void updateTable() {

        model.setRowCount(0);

        if (filteredJobs == null || filteredJobs.isEmpty()) {
            currentPage = 1; totalPages = 1;
            pageLabel.setText("No records found");
            btnPrevious.setEnabled(false);
            btnNext    .setEnabled(false);
            return;
        }

        totalPages = (int) Math.ceil((double) filteredJobs.size() / rowsPerPage);
        if (totalPages <= 0) totalPages = 1;
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int start = (currentPage - 1) * rowsPerPage;
        int end   = Math.min(start + rowsPerPage, filteredJobs.size());

        for (int i = start; i < end; i++) {
            Job job = filteredJobs.get(i);
            model.addRow(new Object[]{
                    job.getCompany(), job.getJobTitle(), job.getMinCgpa(), "View Detail"
            });
        }

        pageLabel.setText("Page " + currentPage + " of " + totalPages);
        btnPrevious.setEnabled(currentPage > 1);
        btnNext    .setEnabled(currentPage < totalPages);
    }

    private void resetFilters() {
        companyFilter.setSelectedIndex(0);
        jobFilter    .setSelectedIndex(0);
        cgpaFilter   .setSelectedIndex(0);
        applyFilters();
    }
}