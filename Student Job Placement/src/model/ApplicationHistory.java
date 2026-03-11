package model;

import java.sql.Timestamp;

/**
 * Represents one row in the application_history table.
 * Every time an application status changes, a row is added here.
 */
public class ApplicationHistory {

    private int       historyId;
    private int       applicationId;
    private String    status;
    private Timestamp changedAt;

    public ApplicationHistory() {}

    public ApplicationHistory(int historyId, int applicationId,
                               String status, Timestamp changedAt) {
        this.historyId     = historyId;
        this.applicationId = applicationId;
        this.status        = status;
        this.changedAt     = changedAt;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int       getHistoryId()      { return historyId; }
    public int       getApplicationId()  { return applicationId; }
    public String    getStatus()         { return status; }
    public Timestamp getChangedAt()      { return changedAt; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setHistoryId(int v)          { historyId = v; }
    public void setApplicationId(int v)      { applicationId = v; }
    public void setStatus(String v)          { status = v; }
    public void setChangedAt(Timestamp v)    { changedAt = v; }

    @Override
    public String toString() {
        return "ApplicationHistory{" +
               "applicationId=" + applicationId +
               ", status='" + status + '\'' +
               ", changedAt=" + changedAt + '}';
    }
}
