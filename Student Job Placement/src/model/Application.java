package model;

import java.sql.Timestamp;

public class Application {

    private int       applicationId;
    private int       userId;
    private int       jobId;

    // Joined from jobs table for display
    private String    company;
    private String    jobTitle;

    private Timestamp dateApplied;
    private String    status;

    // ---- Constructors ----

    public Application() {}

    public Application(int userId, int jobId) {
        this.userId = userId;
        this.jobId  = jobId;
        this.status = "Applied";
    }

    // ---- Getters & Setters ----

    public int       getApplicationId()           { return applicationId; }
    public void      setApplicationId(int v)       { applicationId = v; }

    public int       getUserId()                   { return userId; }
    public void      setUserId(int v)              { userId = v; }

    public int       getJobId()                    { return jobId; }
    public void      setJobId(int v)               { jobId = v; }

    public String    getCompany()                  { return company; }
    public void      setCompany(String v)          { company = v; }

    public String    getJobTitle()                 { return jobTitle; }
    public void      setJobTitle(String v)         { jobTitle = v; }

    public Timestamp getDateApplied()              { return dateApplied; }
    public void      setDateApplied(Timestamp v)   { dateApplied = v; }

    public String    getStatus()                   { return status; }
    public void      setStatus(String v)           { status = v; }
}