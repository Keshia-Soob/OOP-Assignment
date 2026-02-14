Recommended Folder Structure (Clean & Mark-Friendly)

Inside src, create packages like this:
Student Job Placement
│
├── src
│   ├── app
│   │    └── App.java
│   │
│   ├── gui
│   │    ├── base
│   │    │    ├── BaseFrame.java
│   │    │    ├── HeaderPanel.java
│   │    │    └── SidebarPanel.java
│   │    │
│   │    ├── student
│   │    │    ├── StudentDashboardFrame.java
│   │    │    ├── StudentProfileFrame.java
│   │    │    ├── ApplicationsFrame.java
│   │    │    ├── JobListFrame.java
│   │    │    ├── JobDetailsFrame.java
│   │    │    ├── OffCampusFrame.java
│   │    │    └── ChangePasswordFrame.java
│   │    │
│   │    └── login
│   │         ├── LoginFrame.java
│   │         └── RegisterFrame.java
│   │
│   ├── controller
│   │    ├── AuthController.java
│   │    └── StudentController.java
│   │
│   ├── service
│   │    ├── AuthService.java
│   │    ├── StudentService.java
│   │    ├── JobService.java
│   │    ├── ApplicationService.java
│   │    └── OffCampusService.java
│   │
│   ├── dao
│   │    ├── DatabaseManager.java
│   │    ├── UserDAO.java
│   │    ├── StudentDAO.java
│   │    ├── JobDAO.java
│   │    ├── ApplicationDAO.java
│   │    └── OffCampusDAO.java
│   │
│   ├── model
│   │    ├── User.java
│   │    ├── Student.java
│   │    ├── Job.java
│   │    ├── Application.java
│   │    └── OffCampusApplication.java
│   │
│   └── util
│        ├── UIConstants.java
│        └── ValidationUtils.java
│
└── README.md
