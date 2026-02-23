package dao;

import gui.auth.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Job;

public class JobDAO {

    /* ====================== INSERT DUMMY JOBS ====================== */
    public static void insertDummyJobs() {

        String sql = """
            INSERT INTO jobs 
            (company, job_title, location, min_cgpa, salary, description,
            key_responsibilities, duration, start_date,
            contact_email, contact_number)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            Object[][] jobs = {

                {"Accenture", "Backend Developer", "Ebene", 3.0, 45000,
                        "Work on enterprise backend systems using Java and Spring.",
                        "• Develop REST APIs\n• Optimize database queries\n• Perform code reviews",
                        "6 Months", "2026-09-21", "hr@accenture.mu", "52560001"},

                {"IBM", "Data Analyst", "Rose Hill", 3.2, 42000,
                        "Analyze large datasets and create business reports.",
                        "• Data cleaning\n• Create dashboards\n• Present insights",
                        "1 Year", "2026-10-01", "careers@ibm.mu", "52560002"},

                {"Huawei", "Network Engineer", "Port Louis", 2.8, 40000,
                        "Maintain telecom network infrastructure.",
                        "• Configure routers\n• Monitor network traffic\n• Troubleshoot issues",
                        "9 Months", "2026-09-25", "jobs@huawei.mu", "52560003"},

                {"MCB", "IT Security Officer", "Port Louis", 3.3, 50000,
                        "Ensure cybersecurity of banking systems.",
                        "• Monitor security logs\n• Conduct audits\n• Incident response",
                        "Permanent", "2026-09-21", "hr@mcb.mu", "52560004"},

                {"Emtel", "Web Developer", "Ebene", 3.0, 38000,
                        "Develop internal web portals.",
                        "• Frontend development\n• Backend integration\n• Testing",
                        "6 Months", "2026-09-28", "careers@emtel.mu", "52560005"},

                {"Orange", "IT Support Officer", "Port Louis", 2.5, 30000,
                        "Provide technical support to customers.",
                        "• Resolve tickets\n• Install software\n• Network setup",
                        "6 Months", "2026-09-21", "hr@orange.mu", "52560006"},

                {"Deloitte", "Technology Consultant", "Ebene", 3.4, 52000,
                        "Consult clients on digital transformation.",
                        "• Client meetings\n• System analysis\n• Solution implementation",
                        "1 Year", "2026-10-05", "recruit@deloitte.mu", "52560007"},

                {"PwC", "Business Analyst", "Ebene", 3.1, 47000,
                        "Bridge IT and business teams.",
                        "• Requirement gathering\n• Documentation\n• Process modeling",
                        "Permanent", "2026-09-30", "jobs@pwc.mu", "52560008"},

                {"Ceridian", "QA Engineer", "Ebene", 2.9, 36000,
                        "Test enterprise HR systems.",
                        "• Manual testing\n• Automation scripts\n• Bug reporting",
                        "8 Months", "2026-09-22", "hr@ceridian.mu", "52560009"},

                {"Infosys", "System Engineer", "Ebene", 2.7, 35000,
                        "Support enterprise IT systems.",
                        "• Server monitoring\n• System updates\n• User support",
                        "1 Year", "2026-09-24", "careers@infosys.mu", "52560010"},

                /* 20 more different entries */

                {"Google", "Cloud Engineer", "Remote", 3.5, 90000,
                        "Work on scalable cloud infrastructure.",
                        "• Deploy cloud services\n• Optimize performance\n• Security compliance",
                        "Permanent", "2026-09-21", "jobs@google.com", "52560011"},

                {"Amazon", "DevOps Engineer", "Remote", 3.4, 85000,
                        "Manage CI/CD pipelines.",
                        "• Automate deployments\n• Monitor systems\n• Infrastructure as Code",
                        "Permanent", "2026-10-01", "hr@amazon.com", "52560012"},

                {"Microsoft", "Software Engineer", "Remote", 3.6, 95000,
                        "Develop enterprise software solutions.",
                        "• Coding\n• Debugging\n• Team collaboration",
                        "Permanent", "2026-09-21", "careers@microsoft.com", "52560013"},

                {"SAP", "ERP Consultant", "Ebene", 3.2, 60000,
                        "Implement SAP solutions for clients.",
                        "• Configure modules\n• Client training\n• System testing",
                        "1 Year", "2026-09-29", "jobs@sap.mu", "52560014"},

                {"TCS", "System Analyst", "Ebene", 3.0, 45000,
                        "Analyze and improve IT systems.",
                        "• Requirement analysis\n• Documentation\n• Testing",
                        "1 Year", "2026-09-23", "hr@tcs.mu", "52560015"},

                {"Wipro", "Programmer", "Ebene", 2.9, 38000,
                        "Develop enterprise applications.",
                        "• Coding\n• Debugging\n• Maintenance",
                        "6 Months", "2026-09-27", "careers@wipro.mu", "52560016"},

                {"KPMG", "IT Auditor", "Ebene", 3.3, 55000,
                        "Audit IT systems for compliance.",
                        "• Risk assessment\n• Security review\n• Report writing",
                        "Permanent", "2026-09-21", "jobs@kpmg.mu", "52560017"},

                {"HSBC", "IT Analyst", "Port Louis", 3.1, 50000,
                        "Support banking IT systems.",
                        "• System monitoring\n• Incident resolution\n• Reporting",
                        "Permanent", "2026-09-22", "hr@hsbc.mu", "52560018"},

                {"Samsung", "Mobile Developer", "Remote", 3.4, 75000,
                        "Develop Android applications.",
                        "• UI design\n• API integration\n• Testing",
                        "1 Year", "2026-10-02", "jobs@samsung.com", "52560019"},

                {"Intel", "Embedded Engineer", "Remote", 3.5, 88000,
                        "Develop embedded system software.",
                        "• Firmware coding\n• Hardware testing\n• Optimization",
                        "Permanent", "2026-09-21", "careers@intel.com", "52560020"},

                /* Remaining 10 simplified but different */

                {"Nvidia", "AI Engineer", "Remote", 3.6, 98000, "AI model development",
                        "• Train models\n• Optimize performance\n• Deploy AI solutions",
                        "Permanent", "2026-09-21", "jobs@nvidia.com", "52560021"},

                {"Dell", "Infrastructure Engineer", "Ebene", 3.0, 47000, "Manage IT infrastructure",
                        "• Server setup\n• Backup management\n• Monitoring",
                        "1 Year", "2026-09-23", "hr@dell.com", "52560022"},

                {"Cisco", "Network Specialist", "Port Louis", 3.3, 52000, "Enterprise networking",
                        "• Router configuration\n• Firewall setup\n• Troubleshooting",
                        "Permanent", "2026-09-21", "careers@cisco.com", "52560023"},

                {"Vodafone", "Telecom Engineer", "Port Louis", 2.8, 39000, "Telecom infrastructure",
                        "• Tower maintenance\n• Signal monitoring\n• Support",
                        "9 Months", "2026-09-24", "jobs@vodafone.com", "52560024"},

                {"EY", "Cybersecurity Analyst", "Ebene", 3.4, 60000, "Security monitoring",
                        "• Threat detection\n• Risk mitigation\n• Reporting",
                        "Permanent", "2026-09-21", "hr@ey.com", "52560025"},

                {"Sony", "Application Developer", "Remote", 3.2, 72000, "Application systems",
                        "• Coding\n• Testing\n• Maintenance",
                        "1 Year", "2026-10-01", "jobs@sony.com", "52560026"},

                {"Barclays", "Junior Developer", "Port Louis", 3.0, 45000, "Banking software",
                        "• Feature development\n• Bug fixing\n• Documentation",
                        "Permanent", "2026-09-25", "hr@barclays.com", "52560027"},

                {"HP", "IT Technician", "Ebene", 2.6, 32000, "Hardware maintenance",
                        "• Device repair\n• OS installation\n• Support",
                        "6 Months", "2026-09-21", "careers@hp.com", "52560028"},

                {"TechMahindra", "Support Engineer", "Ebene", 2.7, 34000, "Technical support",
                        "• Ticket handling\n• System monitoring\n• Documentation",
                        "1 Year", "2026-09-22", "jobs@techmahindra.com", "52560029"},

                {"Amdocs", "Software Tester", "Ebene", 2.8, 36000, "Telecom testing",
                        "• Test cases\n• Automation\n• Reporting",
                        "8 Months", "2026-09-21", "hr@amdocs.com", "52560030"}
            };

            for (Object[] job : jobs) {
                ps.setString(1, (String) job[0]);
                ps.setString(2, (String) job[1]);
                ps.setString(3, (String) job[2]);
                ps.setDouble(4, ((Number) job[3]).doubleValue());
                ps.setDouble(5, ((Number) job[4]).doubleValue());
                ps.setString(6, (String) job[5]);
                ps.setString(7, (String) job[6]);
                ps.setString(8, (String) job[7]);
                ps.setDate(9, Date.valueOf((String) job[8]));
                ps.setString(10, (String) job[9]);
                ps.setString(11, (String) job[10]);
                ps.executeUpdate();
            }

            System.out.println("30 Different Dummy Jobs Inserted Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* ====================== RETRIEVE ALL JOBS ====================== */
    public static List<Job> getAllJobs() {

        List<Job> jobs = new ArrayList<>();

        String sql = """
            SELECT job_id, company, job_title, location, min_cgpa, salary,
                description, key_responsibilities, duration,
                start_date, contact_email, contact_number
            FROM jobs
        """;

        try (Connection con = DB.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Job job = new Job(
                        rs.getInt("job_id"),
                        rs.getString("company"),
                        rs.getString("job_title"),
                        rs.getString("location"),
                        rs.getDouble("min_cgpa"),
                        rs.getDouble("salary"),
                        rs.getString("description"),
                        rs.getString("key_responsibilities"),
                        rs.getString("duration"),
                        rs.getDate("start_date"),
                        rs.getString("contact_email"),
                        rs.getString("contact_number")
                );

                jobs.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }
}