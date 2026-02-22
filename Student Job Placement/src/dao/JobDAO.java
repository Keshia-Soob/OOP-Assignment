package dao;

import gui.auth.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Job;

public class JobDAO {

    /* ====================== INSERT DUMMY JOBS ====================== */
    public static void insertDummyJobs() {
        try (Connection con = DB.getConnection(); Statement st = con.createStatement()) {

           String[] sql = {

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Accenture','Software Developer',3.0,'A','Java systems')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('IBM','Data Analyst',3.2,'B','Data reports')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Huawei','Network Engineer',2.8,'C','Network support')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('MCB','IT Officer',3.3,'A','Bank systems')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Emtel','Web Developer',3.0,'B','Website systems')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Orange','IT Support',2.5,'C','Customer support')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Ceridian','QA Tester',2.6,'A','Testing software')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Infosys','System Engineer',2.7,'B','System support')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Deloitte','Consultant',3.4,'A','IT consulting')",
                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('PwC','Business Analyst',3.1,'C','Business analysis')",

                /* ===== 20 NEW ROWS ===== */

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Google','Backend Developer',3.5,'A','Cloud backend systems')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Amazon','Cloud Engineer',3.4,'B','AWS cloud infrastructure')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Microsoft','Software Engineer',3.6,'A','Enterprise software development')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('SAP','ERP Consultant',3.2,'C','ERP implementation projects')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('TCS','System Analyst',3.0,'B','System requirement analysis')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Wipro','Programmer',2.9,'A','Software coding tasks')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('TechMahindra','Support Engineer',2.7,'C','Technical support services')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('HP','IT Technician',2.6,'B','Hardware and software maintenance')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Dell','Infrastructure Engineer',3.1,'A','Server infrastructure')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Cisco','Network Specialist',3.3,'C','Network configuration')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Vodafone','Network Support',2.8,'B','Telecom network support')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Amdocs','Software Tester',2.7,'A','Testing telecom software')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('EY','IT Auditor',3.2,'C','IT audit services')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('KPMG','Technology Consultant',3.3,'A','IT consulting services')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('HSBC','IT Analyst',3.1,'B','Banking systems analysis')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Barclays','Junior Developer',3.0,'C','Financial software')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Samsung','Software Engineer',3.4,'A','Mobile software')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Sony','Application Developer',3.2,'B','Application development')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Intel','Embedded Engineer',3.5,'C','Embedded systems')",

                "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Nvidia','AI Engineer',3.6,'A','Artificial intelligence systems')"

            };

            for (String s : sql) st.executeUpdate(s);

            System.out.println("Dummy Jobs Inserted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ====================== RETRIEVE ALL JOBS ====================== */
    public static List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT job_id, company, job_title, min_cgpa, section, description FROM jobs";

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Job job = new Job(
                        rs.getInt("job_id"),
                        rs.getString("company"),
                        rs.getString("job_title"),
                        rs.getDouble("min_cgpa"),
                        rs.getString("section"),
                        rs.getString("description")
                );
                jobs.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }
}