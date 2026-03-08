package model;

/**
 * Maps to the `users` table.
 */
public class User {

    private int    userId;
    private String fullName;
    private String email;
    private String address;
    private String studentId;
    private String contactNumber;
    private String course;
    private String faculty;
    private double cgpa;
    private String level;
    private int    age;
    private String passwordHash;
    private String role;

    // ====================== CONSTRUCTORS ======================

    public User() {}

    public User(int userId, String fullName, String email, String address,
                String studentId, String contactNumber, String course,
                String faculty, double cgpa, String level, int age,
                String passwordHash, String role) {
        this.userId        = userId;
        this.fullName      = fullName;
        this.email         = email;
        this.address       = address;
        this.studentId     = studentId;
        this.contactNumber = contactNumber;
        this.course        = course;
        this.faculty       = faculty;
        this.cgpa          = cgpa;
        this.level         = level;
        this.age           = age;
        this.passwordHash  = passwordHash;
        this.role          = role;
    }

    // ====================== GETTERS ======================

    public int    getUserId()        { return userId; }
    public String getFullName()      { return fullName; }
    public String getEmail()         { return email; }
    public String getAddress()       { return address; }
    public String getStudentId()     { return studentId; }
    public String getContactNumber() { return contactNumber; }
    public String getCourse()        { return course; }
    public String getFaculty()       { return faculty; }
    public double getCgpa()          { return cgpa; }
    public String getLevel()         { return level; }
    public int    getAge()           { return age; }
    public String getPasswordHash()  { return passwordHash; }
    public String getRole()          { return role; }

    // ====================== SETTERS ======================

    public void setUserId(int userId)               { this.userId = userId; }
    public void setFullName(String fullName)        { this.fullName = fullName; }
    public void setEmail(String email)              { this.email = email; }
    public void setAddress(String address)          { this.address = address; }
    public void setStudentId(String studentId)      { this.studentId = studentId; }
    public void setContactNumber(String c)          { this.contactNumber = c; }
    public void setCourse(String course)            { this.course = course; }
    public void setFaculty(String faculty)          { this.faculty = faculty; }
    public void setCgpa(double cgpa)                { this.cgpa = cgpa; }
    public void setLevel(String level)              { this.level = level; }
    public void setAge(int age)                     { this.age = age; }
    public void setPasswordHash(String passwordHash){ this.passwordHash = passwordHash; }
    public void setRole(String role)                { this.role = role; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", fullName='" + fullName +
               "', email='" + email + "', role='" + role + "'}";
    }
}
