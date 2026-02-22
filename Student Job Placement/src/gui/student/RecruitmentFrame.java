package gui.student;

import gui.base.BaseFrame;
import gui.base.SidebarPanel;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

        // ================= DATA =================
        allData = new Object[][]{
                {"Infosys", "Software Engineer", 6.5, "View Detail"},
                {"TCS", "Data Analyst", 6.3, "View Detail"},
                {"Google", "Backend Developer", 7.0, "View Detail"},
                {"Accenture", "Marketing Associate", 7.0, "View Detail"},
                {"TCS", "System Engineer", 6.0, "View Detail"},
                {"Amazon", "Cloud Engineer", 7.5, "View Detail"},
                {"Microsoft", "Frontend Developer", 7.2, "View Detail"},
                {"IBM", "Consultant", 6.8, "View Detail"},
                {"Capgemini", "Software Tester", 6.2, "View Detail"},
                {"Deloitte", "Business Analyst", 7.1, "View Detail"},
                {"Oracle", "Database Admin", 6.9, "View Detail"},
                {"Oracle", "Database Admin", 6.9, "View Detail"},
                {"HCL", "Support Engineer", 6.4, "View Detail"}
        };

        filteredData = allData;
        totalPages = (int) Math.ceil((double) filteredData.length / rowsPerPage);

        populateFilters();

        // ================= TABLE =================
        String[] columns = {
                "Company",
                "Job Title",
                "Minimum CGPA",
                "Application"
        };

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

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
        cgpaFilter.addItem("All");

        Set<String> companies = new HashSet<>();
        Set<String> jobs = new HashSet<>();
        Set<String> cgpas = new HashSet<>();

        for (Object[] row : allData) {
            companies.add(row[0].toString());
            jobs.add(row[1].toString());
            cgpas.add(row[2].toString());
        }

        for (String c : companies) companyFilter.addItem(c);
        for (String j : jobs) jobFilter.addItem(j);
        for (String g : cgpas) cgpaFilter.addItem(g);

        companyFilter.addActionListener(e -> applyFilters());
        jobFilter.addActionListener(e -> applyFilters());
        cgpaFilter.addActionListener(e -> applyFilters());
    }

    // ================= APPLY FILTERS =================
    private void applyFilters() {

        String selectedCompany = companyFilter.getSelectedItem().toString();
        String selectedJob = jobFilter.getSelectedItem().toString();
        String selectedCgpa = cgpaFilter.getSelectedItem().toString(); // ✅ Fixed

        java.util.List<Object[]> temp = new ArrayList<>();

        for (Object[] row : allData) {

            boolean match = true;

            if (!selectedCompany.equals("All") && !row[0].toString().equals(selectedCompany))
                match = false;

            if (!selectedJob.equals("All") && !row[1].toString().equals(selectedJob))
                match = false;

            if (!selectedCgpa.equals("All") && !row[2].toString().equals(selectedCgpa))
                match = false;

            if (match)
                temp.add(row);
        }

        filteredData = temp.toArray(Object[][]::new);

        currentPage = 1;
        totalPages = (filteredData.length == 0) ? 1 : (int) Math.ceil((double) filteredData.length / rowsPerPage);

        updateTable();
    }

    // ================= UPDATE TABLE =================
    private void updateTable() {

        model.setRowCount(0);

        // Prevent invalid pagination
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
        companyFilter.setSelectedIndex(0); // "All"
        jobFilter.setSelectedIndex(0);     // "All"
        cgpaFilter.setSelectedIndex(0);    // "All"

        applyFilters();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RecruitmentFrame().setVisible(true);
        });
    }
}
