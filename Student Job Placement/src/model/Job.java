package model;

public class Job {

    private int jobId;
    private String company;
    private String jobTitle;
    private double minCgpa;
    private String section;
    private String description;

    public Job(int jobId, String company, String jobTitle, double minCgpa, String section, String description) {
        this.jobId = jobId;
        this.company = company;
        this.jobTitle = jobTitle;
        this.minCgpa = minCgpa;
        this.section = section;
        this.description = description;
    }

    public int getJobId() {
        return jobId;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public String getSection() {
        return section;
    }

    public String getDescription() {
        return description;
    }
}