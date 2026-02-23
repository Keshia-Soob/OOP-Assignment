package model;

import java.sql.Date;

public class Job {

    private int jobId;
    private String company;
    private String jobTitle;
    private String location;
    private double minCgpa;
    private double salary;
    private String description;
    private String keyResponsibilities;
    private String duration;
    private Date startDate;
    private String contactEmail;
    private String contactNumber;

    /* ====================== CONSTRUCTOR ====================== */
    public Job(int jobId, String company, String jobTitle,
               String location, double minCgpa, double salary,
               String description, String keyResponsibilities,
               String duration, Date startDate,
               String contactEmail, String contactNumber) {

        this.jobId = jobId;
        this.company = company;
        this.jobTitle = jobTitle;
        this.location = location;
        this.minCgpa = minCgpa;
        this.salary = salary;
        this.description = description;
        this.keyResponsibilities = keyResponsibilities;
        this.duration = duration;
        this.startDate = startDate;
        this.contactEmail = contactEmail;
        this.contactNumber = contactNumber;
    }

    /* ====================== GETTERS ====================== */

    public int getJobId() {
        return jobId;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public double getSalary() {
        return salary;
    }

    public String getDescription() {
        return description;
    }

    public String getKeyResponsibilities() {
        return keyResponsibilities;
    }

    public String getDuration() {
        return duration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    /* ====================== SETTERS ====================== */

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeyResponsibilities(String keyResponsibilities) {
        this.keyResponsibilities = keyResponsibilities;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /* ====================== TO STRING ====================== */

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", company='" + company + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", location='" + location + '\'' +
                ", minCgpa=" + minCgpa +
                ", salary=" + salary +
                ", duration='" + duration + '\'' +
                ", startDate=" + startDate +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}