package dao;

import gui.auth.DB;
import java.sql.Connection; // change if your DB class package changes
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    public static void initializeDatabase() {
        createUsersTable();
        createJobsTable();
        createApplicationsTable();
        createOffCampusApplicationsTable(); // optional but matches your UI button
       // insertDummyJobs(); // optional but helps testing
    }

    private static void createUsersTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INT AUTO_INCREMENT PRIMARY KEY,

                full_name VARCHAR(100) NOT NULL,
                email VARCHAR(120) NOT NULL UNIQUE,
                address VARCHAR(255),
                student_id VARCHAR(30) NOT NULL UNIQUE,
                contact_number VARCHAR(30),

                course VARCHAR(100),
                faculty VARCHAR(100),

                cgpa DECIMAL(4,2),
                level VARCHAR(30),
                age INT,

                password_hash VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        execute(sql, "users");
    }

    private static void createJobsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS jobs (
              job_id INT AUTO_INCREMENT PRIMARY KEY,
              company VARCHAR(120) NOT NULL,
              job_title VARCHAR(120) NOT NULL,
              min_cgpa DECIMAL(3,2) DEFAULT 0.00,
              section VARCHAR(20), 
              description TEXT,
              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        execute(sql, "jobs");
    }

    private static void createApplicationsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS applications (
              application_id INT AUTO_INCREMENT PRIMARY KEY,
              user_id INT NOT NULL,
              job_id INT NOT NULL,
              date_applied TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              status VARCHAR(30) NOT NULL DEFAULT 'Applied',

              -- prevent same user applying twice to same job
              UNIQUE KEY uq_user_job (user_id, job_id),

              CONSTRAINT fk_app_user FOREIGN KEY (user_id)
                REFERENCES users(user_id)
                ON DELETE CASCADE,

              CONSTRAINT fk_app_job FOREIGN KEY (job_id)
                REFERENCES jobs(job_id)
                ON DELETE CASCADE
            );
        """;

        execute(sql, "applications");
    }

    // OPTIONAL: matches your "Add Off-Campus Job" button
    private static void createOffCampusApplicationsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS off_campus_applications (
              off_app_id INT AUTO_INCREMENT PRIMARY KEY,
              user_id INT NOT NULL,
              company VARCHAR(120) NOT NULL,
              job_title VARCHAR(120) NOT NULL,
              date_applied TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              status VARCHAR(30) NOT NULL DEFAULT 'Applied',
              notes VARCHAR(255),

              CONSTRAINT fk_off_user FOREIGN KEY (user_id)
                REFERENCES users(user_id)
                ON DELETE CASCADE
            );
        """;

        execute(sql, "off_campus_applications");
    }

    private static void execute(String sql, String tableName) {
        try (Connection con = DB.getConnection();
             Statement st = con.createStatement()) {

            st.execute(sql);
            System.out.println("Table ready: " + tableName);

        } catch (SQLException e) {
            System.out.println("Failed creating table: " + tableName);
            e.printStackTrace();
        }
    }
}