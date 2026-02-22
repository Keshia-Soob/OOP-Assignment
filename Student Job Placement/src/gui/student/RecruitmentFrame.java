package gui.student;

import dao.JobDAO;
import gui.base.BaseFrame;
import gui.base.SidebarPanel;
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

    private Object[][] allData;
    private Object[][] filteredData;

    public RecruitmentFrame() {
        super("Recruitments", SidebarPanel.NavItem.RECRUITMENTS);
    }

    @Override
    protected JComponent buildContent() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= TOP CONTAINER (Title + Filters) =================
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Recruitments");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topContainer.add(titleLabel);
        topContainer.add(Box.createVerticalStrut(15));

        // ================= FILTER PANEL =================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        companyFilter = new JComboBox<>();
        jobFilter = new JComboBox<>();
        cgpaFilter = new JComboBox<>();

        filterPanel.add(new JLabel("Company:"));
        filterPanel.add(companyFilter);

        filterPanel.add(new JLabel("Job Title:"));
        filterPanel.add(jobFilter);

        filterPanel.add(new JLabel("CGPA:"));
        filterPanel.add(cgpaFilter);

        // ===== Reset Filters Button =====
        JButton resetButton = new JButton("Reset Filters");
        filterPanel.add(resetButton);
        resetButton.addActionListener(e -> resetFilters());

        topContainer.add(filterPanel);
        mainPanel.add(topContainer, BorderLayout.NORTH);

        // ================= DATA FROM DATABASE =================
        List<Job> jobsFromDb = JobDAO.getAllJobs();

        allData = new Object[jobsFromDb.size()][4];
        for (int i = 0; i < jobsFromDb.size(); i++) {
            Job job = jobsFromDb.get(i);
            allData[i][0] = job.getCompany();
            allData[i][1] = job.getJobTitle();
            allData[i][2] = job.getMinCgpa();
            allData[i][3] = "View Detail"; // will show description on click
        }

        filteredData = allData;
        totalPages = (int) Math.ceil((double) filteredData.length / rowsPerPage);

        populateFilters();

        // ================= TABLE =================
        String[] columns = {"Company", "Job Title", "Minimum CGPA", "Application"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // ===== Make "View Detail" clickable =====
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 3) {
                    int modelRow = table.convertRowIndexToModel(row);
                    Job job = jobsFromDb.get(modelRow + (currentPage - 1) * rowsPerPage);
                    JOptionPane.showMessageDialog(
                            RecruitmentFrame.this,
                            job.getDescription(),
                            job.getCompany() + " - " + job.getJobTitle(),
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        // ================= TABLE PANEL (NO SCROLLBAR) =================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        // Fix table height for pagination
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));

        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // ================= PAGINATION =================
        JPanel paginationPanel = new JPanel();
        paginationPanel.setBackground(Color.WHITE);

        btnPrevious = new JButton("Previous");
        btnNext = new JButton("Next");
        pageLabel = new JLabel();

        paginationPanel.add(btnPrevious);
        paginationPanel.add(pageLabel);
        paginationPanel.add(btnNext);

        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        btnPrevious.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateTable();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateTable();
            }
        });

        updateTable();

        return mainPanel;
    }

    // ================= POPULATE FILTERS =================
    private void populateFilters() {

        companyFilter.addItem("All");
        jobFilter.addItem("All");

        // CGPA Ranges
        cgpaFilter.addItem("All");
        cgpaFilter.addItem("0-1");
        cgpaFilter.addItem("1-2");
        cgpaFilter.addItem("2-3");
        cgpaFilter.addItem("3-4");
        cgpaFilter.addItem("4-5");
        cgpaFilter.addItem("5-6");
        cgpaFilter.addItem("6-7");
        cgpaFilter.addItem("7-8");
        cgpaFilter.addItem("8-9");
        cgpaFilter.addItem("9-10");

        Set<String> companies = new HashSet<>();
        Set<String> jobs = new HashSet<>();

        for (Object[] row : allData) {
            companies.add(row[0].toString());
            jobs.add(row[1].toString());
        }

        for (String c : companies)
            companyFilter.addItem(c);

        for (String j : jobs)
            jobFilter.addItem(j);

        companyFilter.addActionListener(e -> applyFilters());
        jobFilter.addActionListener(e -> applyFilters());
        cgpaFilter.addActionListener(e -> applyFilters());
    }

    // ================= APPLY FILTERS =================
    private void applyFilters() {

        String selectedCompany = companyFilter.getSelectedItem().toString();
        String selectedJob = jobFilter.getSelectedItem().toString();
        String selectedCgpa = cgpaFilter.getSelectedItem().toString();

        List<Object[]> temp = new ArrayList<>();

        for (Object[] row : allData) {

            boolean match = true;

            double cgpa = Double.parseDouble(row[2].toString());

            // Company filter
            if (!selectedCompany.equals("All") &&
                    !row[0].toString().equals(selectedCompany))
                match = false;

            // Job filter
            if (!selectedJob.equals("All") &&
                    !row[1].toString().equals(selectedJob))
                match = false;

            // CGPA Range filter
            if (!selectedCgpa.equals("All")) {

                String[] range = selectedCgpa.split("-");

                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);

                if (cgpa < min || cgpa >= max)
                    match = false;
            }

            if (match)
                temp.add(row);
        }

        filteredData = temp.toArray(Object[][]::new);

        currentPage = 1;

        totalPages = (filteredData.length == 0)
                ? 1
                : (int) Math.ceil((double) filteredData.length / rowsPerPage);

        updateTable();
    }

    // ================= UPDATE TABLE =================
    private void updateTable() {
        model.setRowCount(0);

        if (filteredData == null || filteredData.length == 0) {
            pageLabel.setText("No records found");
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        totalPages = (int) Math.ceil((double) filteredData.length / rowsPerPage);

        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, filteredData.length);

        for (int i = start; i < end; i++) {
            model.addRow(filteredData[i]);
        }

        pageLabel.setText("Page " + currentPage + " of " + totalPages);

        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    // ================= RESET FILTERS =================
    private void resetFilters() {
        companyFilter.setSelectedIndex(0);
        jobFilter.setSelectedIndex(0);
        cgpaFilter.setSelectedIndex(0);
        applyFilters();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            RecruitmentFrame frame = new RecruitmentFrame();

            frame.setSize(1000, 650);   // Bigger window
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);

        });
    }
}