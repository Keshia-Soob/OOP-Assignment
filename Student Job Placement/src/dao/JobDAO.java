package dao;
import gui.auth.DB;
import java.sql.Connection;
import java.sql.Statement;

public class JobDAO {

    /* ======================
       INSERT JOBS
    ====================== */

    public static void insertDummyJobs() {

        try {
            Connection con = DB.getConnection();
            Statement st = con.createStatement();

            String sql[] = {

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Accenture','Software Developer',3.0,'A','Java systems')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('IBM','Data Analyst',3.2,'B','Data reports')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Huawei','Network Engineer',2.8,'C','Network support')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('MCB','IT Officer',3.3,'A','Bank systems')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Emtel','Web Developer',3.0,'B','Website systems')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Orange','IT Support',2.5,'C','Customer support')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Ceridian','QA Tester',2.6,'A','Testing software')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Infosys','System Engineer',2.7,'B','System support')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('Deloitte','Consultant',3.4,'A','IT consulting')",

            "INSERT INTO jobs (company,job_title,min_cgpa,section,description) VALUES ('PwC','Business Analyst',3.1,'C','Business analysis')"

            };

            for(String s : sql)
                st.executeUpdate(s);

            System.out.println("Dummy Jobs Inserted");

            con.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    /* ======================
       INSERT APPLICATIONS
    ====================== */

    public static void insertDummyApplications() {

        try {
            Connection con = DB.getConnection();
            Statement st = con.createStatement();

            for(int i=1;i<=10;i++){

                int userId = (i % 3) + 1;

                String sql =
                        "INSERT INTO applications (user_id,job_id,status) VALUES ("+
                        userId+","+i+",'Applied')";

                st.executeUpdate(sql);
            }

            System.out.println("Dummy Applications Inserted");

            con.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    /* ======================
       INSERT OFF CAMPUS
    ====================== */

    public static void insertDummyOffCampus() {

        try {
            Connection con = DB.getConnection();
            Statement st = con.createStatement();

            String sql[] = {

            "INSERT INTO off_campus_applications (user_id,company,job_title,status,notes) VALUES (1,'Google','Software Intern','Applied','Online')",

            "INSERT INTO off_campus_applications (user_id,company,job_title,status,notes) VALUES (2,'Amazon','Cloud Support','Interview','Online')",

            "INSERT INTO off_campus_applications (user_id,company,job_title,status,notes) VALUES (3,'Microsoft','Engineer','Applied','Online')"

            };

            for(String s : sql)
                st.executeUpdate(s);

            System.out.println("Dummy OffCampus Inserted");

            con.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}