# Complaint Management System

A console-based Complaint Management System built using Java and MySQL. This system allows users to **register, track, update, and resolve complaints** efficiently with database integration.

---

## 🚀 Features
- ➕ Add/register new complaints  
- 📄 View all complaints  
- ✏️ Update complaint status  
- ❌ Delete resolved complaints  
- MySQL database integration for persistent storage  

---

## 🛠️ Tech Stack
- Java (Core Java)  
- JDBC  
- MySQL  
- Eclipse IDE  

---

## 📂 Project Structure
ComplaintManagementSystem/
│
├── src/
│ ├── com.cms.main → Main application (entry point)
│ ├── com.cms.dao → Database operations (CRUD logic)
│ ├── com.cms.model → Complaint entity class
│ └── com.cms.util → Database connection utility
│
├── lib/
│ └── mysql-connector-j.jar


---

## 🗄️ Database Setup
```sql
CREATE DATABASE cmsdb;

USE cmsdb;

CREATE TABLE complaint (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50),
    email VARCHAR(50),
    complaint_text TEXT,
    status VARCHAR(20)
);

▶️ How to Run
Clone the repository
Import into Eclipse as a Java Project
Add MySQL Connector/J (JDBC driver) to the project
Configure database credentials in DBConnection.java
Run MainApp.java

📌 Learning Outcomes
JDBC connectivity with MySQL
CRUD operations in Java
Layered architecture (DAO, Model, Utility)
Managing and tracking data efficiently

🔮 Future Enhancements
GUI using Java Swing / JavaFX
REST API using Spring Boot
User authentication system
Dashboard with Power BI integration


👩‍💻 Author
Ananya
