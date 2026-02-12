
# Attendly – Database Design Document

## 1. Introduction
This document describes the relational database design for **Attendly**, a university attendance management system prototype.  
The database is implemented using **MySQL** and follows standard relational database design principles, including normalization, referential integrity, and role-based access control.

The design supports:
- Multiple academic programmes
- Shared modules across programmes
- Multiple user roles (Student, Lecturer, Admin)
- Lecture/session-based attendance recording

---

## 2. Database Overview

### Database Type
- **Relational Database Management System (RDBMS)**
- **MySQL**

### Design Principles
- Normalized structure (up to 3NF)
- Primary and Foreign Key constraints
- Many-to-Many relationships via junction tables
- Data integrity enforced at database level
- Audit fields (`created_at`, `updated_at`)

---

## 3. Tables Summary

| Table Name | Description |
|----------|------------|
| users | Stores all system users |
| programmes | Stores academic degree programmes |
| user_programmes | Links users to programmes |
| modules | Stores academic modules |
| programme_modules | Links programmes and modules |
| module_enrolments | Links students to enrolled modules |
| sessions | Stores lecture/lab/tutorial sessions |
| attendance | Stores attendance records |

---

## 4. Table Definitions

### 4.1 users
Stores all users who can access the system.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Unique user identifier |
| full_name | VARCHAR(120) | User full name |
| email | VARCHAR(190) | Unique login email |
| password_hash | VARCHAR(255) | Hashed password |
| role | ENUM | STUDENT / LECTURER / ADMIN |
| created_at | DATETIME | Record creation timestamp |
| updated_at | DATETIME | Last update timestamp |

**Notes**
- Implements role-based access control.
- `updated_at` tracks profile changes.

---

### 4.2 programmes
Represents academic degree programmes.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Programme identifier |
| code | VARCHAR(20) | Unique programme code |
| name | VARCHAR(160) | Programme name |
| created_at | DATETIME | Creation timestamp |

---

### 4.3 user_programmes
Junction table implementing a many-to-many relationship between users and programmes.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Record identifier |
| user_id | BIGINT (FK) | References users.id |
| programme_id | BIGINT (FK) | References programmes.id |
| created_at | DATETIME | Creation timestamp |

**Constraint**
- UNIQUE (user_id, programme_id)

---

### 4.4 modules
Stores academic modules such as Advanced Programming or Cyber Security.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Module identifier |
| code | VARCHAR(20) | Unique module code |
| name | VARCHAR(160) | Module name |
| level | TINYINT | Academic level |
| created_at | DATETIME | Creation timestamp |

---

### 4.5 programme_modules
Links modules to programmes (many-to-many).

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Record identifier |
| programme_id | BIGINT (FK) | References programmes.id |
| module_id | BIGINT (FK) | References modules.id |
| created_at | DATETIME | Creation timestamp |

**Constraint**
- UNIQUE (programme_id, module_id)

---

### 4.6 module_enrolments
Stores which students are enrolled in which modules.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Record identifier |
| module_id | BIGINT (FK) | References modules.id |
| student_id | BIGINT (FK) | References users.id |
| created_at | DATETIME | Creation timestamp |

**Constraint**
- UNIQUE (module_id, student_id)

---

### 4.7 sessions
Represents individual lecture, lab, or tutorial sessions.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Session identifier |
| module_id | BIGINT (FK) | References modules.id |
| lecturer_id | BIGINT (FK) | References users.id |
| title | VARCHAR(160) | Session title |
| start_time | DATETIME | Start time |
| end_time | DATETIME | End time |
| status | ENUM | OPEN / CLOSED |
| attendance_code | VARCHAR(10) | Optional verification code |
| created_at | DATETIME | Creation timestamp |

---

### 4.8 attendance
Stores attendance records for students in sessions.

| Column | Type | Description |
|------|------|-------------|
| id | BIGINT (PK) | Attendance record identifier |
| session_id | BIGINT (FK) | References sessions.id |
| student_id | BIGINT (FK) | References users.id |
| marked_at | DATETIME | Attendance time |
| status | ENUM | PRESENT / LATE / ABSENT / REJECTED |
| evidence_json | JSON | Optional verification data |
| note | VARCHAR(255) | Optional manual note |

**Constraint**
- UNIQUE (session_id, student_id)

---

## 5. Relationships Summary

- Users ↔ Programmes (Many-to-Many)
- Programmes ↔ Modules (Many-to-Many)
- Users ↔ Modules (Many-to-Many via enrolments)
- Modules → Sessions (One-to-Many)
- Lecturers → Sessions (One-to-Many)
- Sessions → Attendance (One-to-Many)
- Students → Attendance (One-to-Many)

---

## 6. Conclusion
This database design provides a scalable, normalized, and academically sound foundation for the Attendly prototype.  
It supports realistic university structures while remaining simple enough for rapid development and demonstration.
