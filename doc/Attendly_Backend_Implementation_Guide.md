# Attendly Backend Implementation Guide (UPDATED & DATABASE-ALIGNED)

Spring Boot 3 + Java 17 + Maven + PostgreSQL (Docker)

------------------------------------------------------------------------

## IMPORTANT DATABASE RULE

All database tables, relationships, constraints, and naming conventions
MUST strictly follow the official **Database Design Document**.

This document is the single source of truth for:

-   Table names
-   Column names
-   Data types
-   Primary keys
-   Foreign keys
-   Unique constraints
-   Many-to-many relationships
-   Audit fields

The backend implementation must conform to the database design. The
backend does NOT redefine the schema.

PostgreSQL adaptations are allowed only for syntax differences: -
DATETIME → TIMESTAMP - JSON → JSONB - ENUM → VARCHAR + CHECK constraint
(or PostgreSQL ENUM)

------------------------------------------------------------------------

## Tech Stack

-   Java 17
-   Maven
-   Spring Boot 3
-   PostgreSQL (Docker)
-   Flyway
-   Spring Security (JWT)
-   Springdoc OpenAPI (Swagger)

------------------------------------------------------------------------

## Database (Running in Docker)

Host: localhost\
Port: 5432\
Database: attendly\
Username: dinuka@99\
Password: Henzer@99

IMPORTANT: - Never hardcode credentials. - Use environment variables. -
Use spring.jpa.hibernate.ddl-auto=validate - Flyway must control schema
creation.

------------------------------------------------------------------------

# GLOBAL ARCHITECTURE RULES

-   Controller → Service → Repository → Entity
-   DTOs for request/response
-   GlobalExceptionHandler
-   JWT stateless authentication
-   Role-based access (ADMIN / LECTURER / STUDENT)
-   All endpoints under `/api/v1`
-   Flyway-managed schema
-   Respect created_at and updated_at fields

------------------------------------------------------------------------

# BUILD PLAN (STRICT ORDER)

------------------------------------------------------------------------

## PART 0 --- Repository Setup

1.  Create .gitignore
2.  Create README.md
3.  Create .env.example
4.  Ensure .env is ignored

------------------------------------------------------------------------

## PART 1 --- Spring Boot Setup

Dependencies: - Spring Web - Spring Validation - Spring Data JPA -
PostgreSQL Driver - Spring Security - Flyway - Springdoc OpenAPI -
Lombok (optional)

Ensure project compiles and runs.

------------------------------------------------------------------------

## PART 2 --- PostgreSQL Configuration

application.yml must:

-   Use environment variables
-   Configure datasource
-   Set ddl-auto=validate
-   Enable Flyway

Add: GET /api/v1/health endpoint

------------------------------------------------------------------------

## PART 3 --- Folder Structure (Matches Database Tables)

modules/ - users - programmes - userprogrammes - modules -
programmemodules - enrolments - sessions - attendance - reports

Each package must map directly to a table defined in the Database Design
Document.

------------------------------------------------------------------------

## PART 4 --- Flyway Schema Implementation

Create V1\_\_init.sql using EXACT table names:

1.  users
2.  programmes
3.  user_programmes
4.  modules
5.  programme_modules
6.  module_enrolments
7.  sessions
8.  attendance

Constraints MUST match design:

-   UNIQUE (user_id, programme_id)
-   UNIQUE (programme_id, module_id)
-   UNIQUE (module_id, student_id)
-   UNIQUE (session_id, student_id)

Do NOT rename tables.

------------------------------------------------------------------------

## PART 5 --- Authentication

Users entity must match:

-   id
-   full_name
-   email
-   password_hash
-   role
-   created_at
-   updated_at

Roles: - STUDENT - LECTURER - ADMIN

Implement:

POST /api/v1/auth/login\
GET /api/v1/users/me

------------------------------------------------------------------------

## PART 6 --- CRUD Modules (Database-Driven)

### Users

Admin create/list/update

### Programmes

CRUD

### UserProgrammes

Manage many-to-many relationship

### Modules

CRUD

### ProgrammeModules

Manage many-to-many relationship

### ModuleEnrolments

Enroll/remove students

All relationships must reflect the database design.

------------------------------------------------------------------------

## PART 7 --- Sessions

Fields must match sessions table:

-   module_id
-   lecturer_id
-   title
-   start_time
-   end_time
-   status (OPEN/CLOSED)
-   attendance_code
-   created_at

Only assigned lecturer can open/close session.

------------------------------------------------------------------------

## PART 8 --- Attendance

Table name must be:

attendance

Fields:

-   session_id
-   student_id
-   marked_at
-   status (PRESENT/LATE/ABSENT/REJECTED)
-   evidence_json
-   note

Unique constraint prevents duplicate marking.

------------------------------------------------------------------------

## PART 9 --- Reports

Reports must derive data from:

-   attendance
-   sessions
-   module_enrolments
-   programme_modules
-   user_programmes

------------------------------------------------------------------------

## PART 10 --- Swagger & Tests

-   Configure OpenAPI documentation
-   Add integration tests for login and attendance
-   Ensure system runs fully with PostgreSQL Docker

------------------------------------------------------------------------

# EXECUTION STRATEGY

Implement strictly in order: 1. Setup 2. DB config 3. Flyway schema 4.
Auth 5. Core CRUD 6. Sessions 7. Attendance 8. Reports 9. Swagger polish

Verify each stage before proceeding.

------------------------------------------------------------------------

END OF UPDATED IMPLEMENTATION GUIDE
