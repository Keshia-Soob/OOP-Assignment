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

    private JLabel pageLabel;
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

        // ================= TITLE =================
        JLabel titleLabel = new JLabel("Recruitments");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // ================= FILTER PANEL =================
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

        // ================= LOAD DATA =================
        allJobs      = JobDAO.getAllJobs();
        filteredJobs = new ArrayList<>(allJobs);

        populateFilters();

        // ================= TABLE =================
        String[] columns = {"Company", "Job Title", "Minimum CGPA", "Application"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // ================= CLICK HANDLER =================
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row == -1 || col != 3) return;

                int actualIndex = (currentPage - 1) * rowsPerPage + row;
                if (actualIndex < 0 || actualIndex >= filteredJobs.size()) return;

                Job job = filteredJobs.get(actualIndex);

                String details =
                        "Key Responsibilities:\n" + job.getKeyResponsibilities() + "\n\n" +
                        "Location:\n"             + job.getLocation()            + "\n\n" +
                        "Salary:\n"               + job.getSalary()              + "\n\n" +
                        "Duration:\n"             + job.getDuration()            + "\n\n" +
                        "Contact Email:\n"        + job.getContactEmail()        + "\n\n" +
                        "Contact Number:\n"       + job.getContactNumber()       + "\n\n" +
                        "Starting Date:\n"        + job.getStartDate();

                int result = JOptionPane.showOptionDialog(
                        RecruitmentFrame.this,
                        details,
                        job.getCompany() + " - " + job.getJobTitle(),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Apply", "Close"},
                        "Apply"
                );

                if (result == 0) {
                    // ---- CGPA CHECK ----
                    double studentCgpa  = Session.getCurrentUser().getCgpa();
                    double requiredCgpa = job.getMinCgpa();

                    if (studentCgpa < requiredCgpa) {
                        JOptionPane.showMessageDialog(
                                RecruitmentFrame.this,
                                "You are not eligible for this position due to low CGPA.\n\n" +
                                "Required CGPA : " + requiredCgpa + "\n" +
                                "Your CGPA     : " + studentCgpa,
                                "Not Eligible",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    // ---- APPLY: save to DB via ApplicationService ----
                    int userId  = Session.getUserId();
                    boolean ok  = ApplicationService.apply(userId, job.getJobId());

                    if (ok) {
                        JOptionPane.showMessageDialog(
                                RecruitmentFrame.this,
                                "Application submitted successfully!\nYou can view it in the Applications section.",
                                "Applied",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                RecruitmentFrame.this,
                                "You have already applied for this position.",
                                "Already Applied",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // ================= PAGINATION =================
        JPanel paginationPanel = new JPanel();
        paginationPanel.setBackground(Color.WHITE);

        btnPrevious = new JButton("Previous");
        btnNext     = new JButton("Next");
        pageLabel   = new JLabel();

        paginationPanel.add(btnPrevious);
        paginationPanel.add(pageLabel);
        paginationPanel.add(btnNext);
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        btnPrevious.addActionListener(e -> { if (currentPage > 1)          { currentPage--; updateTable(); } });
        btnNext    .addActionListener(e -> { if (currentPage < totalPages)  { currentPage++; updateTable(); } });

        updateTable();
        return mainPanel;
    }

    // ================= POPULATE FILTERS =================
    private void populateFilters() {

        companyFilter.addItem("All");
        jobFilter.addItem("All");

        cgpaFilter.addItem("All");
        for (String r : new String[]{"0-1","1-2","2-3","3-4","4-5"})
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

    // ================= APPLY FILTERS =================
    private void applyFilters() {

        String selCompany = companyFilter.getSelectedItem().toString();
        String selJob     = jobFilter.getSelectedItem().toString();
        String selCgpa    = cgpaFilter.getSelectedItem().toString();

        filteredJobs.clear();

        for (Job job : allJobs) {
            boolean match = true;

            if (!selCompany.equals("All") && !job.getCompany().equals(selCompany))   match = false;
            if (!selJob    .equals("All") && !job.getJobTitle().equals(selJob))      match = false;
            if (!selCgpa   .equals("All")) {
                String[] range = selCgpa.split("-");
                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);
                if (job.getMinCgpa() < min || job.getMinCgpa() >= max)               match = false;
            }

            if (match) filteredJobs.add(job);
        }

        currentPage = 1;
        updateTable();
    }

    // ================= UPDATE TABLE =================
    private void updateTable() {

        model.setRowCount(0);

        if (filteredJobs == null || filteredJobs.isEmpty()) {
            currentPage = 1; totalPages = 1;
            pageLabel.setText("No records found");
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
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
            model.addRow(new Object[]{job.getCompany(), job.getJobTitle(), job.getMinCgpa(), "View Detail"});
        }

        pageLabel.setText("Page " + currentPage + " of " + totalPages);
        btnPrevious.setEnabled(currentPage > 1);
        btnNext    .setEnabled(currentPage < totalPages);
    }

    // ================= RESET FILTERS =================
    private void resetFilters() {
        companyFilter.setSelectedIndex(0);
        jobFilter    .setSelectedIndex(0);
        cgpaFilter   .setSelectedIndex(0);
        applyFilters();
    }
}