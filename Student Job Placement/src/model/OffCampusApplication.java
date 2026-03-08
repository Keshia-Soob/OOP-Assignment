package model;

import java.sql.Timestamp;

public class OffCampusApplication {

    private int offAppId;
    private int userId;
    private String company;
    private String jobTitle;
    private Timestamp dateApplied;
    private String status;
    private String notes;

    // ====================== CONSTRUCTORS ======================

    public OffCampusApplication() {}

    // Used when inserting (no id / date yet — DB sets those)
    public OffCampusApplication(int userId, String company, String jobTitle,
                                 String status, String notes) {
        this.userId   = userId;
        this.company  = company;
        this.jobTitle = jobTitle;
        this.status   = status;
        this.notes    = notes;
    }

    // Used when reading back from DB
    public OffCampusApplication(int offAppId, int userId, String company,
                                 String jobTitle, Timestamp dateApplied,
                                 String status, String notes) {
        this.offAppId    = offAppId;
        this.userId      = userId;
        this.company     = company;
        this.jobTitle    = jobTitle;
        this.dateApplied = dateApplied;
        this.status      = status;
        this.notes       = notes;
    }

    // ====================== GETTERS ======================

    public int getOffAppId()          { return offAppId; }
    public int getUserId()            { return userId; }
    public String getCompany()        { return company; }
    public String getJobTitle()       { return jobTitle; }
    public Timestamp getDateApplied() { return dateApplied; }
    public String getStatus()         { return status; }
    public String getNotes()          { return notes; }

    // ====================== SETTERS ======================

    public void setOffAppId(int offAppId)            { this.offAppId = offAppId; }
    public void setUserId(int userId)                { this.userId = userId; }
    public void setCompany(String company)           { this.company = company; }
    public void setJobTitle(String jobTitle)         { this.jobTitle = jobTitle; }
    public void setDateApplied(Timestamp dateApplied){ this.dateApplied = dateApplied; }
    public void setStatus(String status)             { this.status = status; }
    public void setNotes(String notes)               { this.notes = notes; }

    @Override
    public String toString() {
        return "OffCampusApplication{" +
                "offAppId=" + offAppId +
                ", userId=" + userId +
                ", company='" + company + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", dateApplied=" + dateApplied +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
