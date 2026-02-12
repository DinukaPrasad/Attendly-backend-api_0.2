# Claude Opus Optimized Prompt (DATABASE-ALIGNED VERSION)

Attendly Backend --- Strict Database-Driven Implementation

You are a senior Spring Boot engineer.

Your task is to build a clean, production-structured prototype backend
for Attendly (University Attendance Management System).

Tech Stack: - Java 17 - Maven - Spring Boot 3 - PostgreSQL (Docker) -
Flyway - Spring Security (JWT) - Springdoc OpenAPI

------------------------------------------------------------------------

# CRITICAL DATABASE RULE

You MUST strictly follow the official Database Design Document.

The Database Design Document is the SINGLE SOURCE OF TRUTH for:

-   Table names
-   Column names
-   Primary keys
-   Foreign keys
-   Unique constraints
-   Many-to-many relationships
-   Audit fields (created_at, updated_at)

DO NOT: - Rename tables - Rename columns - Add new columns unless
justified - Remove constraints - Modify relationship structures

The backend must conform to the database design. The backend does NOT
redefine the schema.

PostgreSQL syntax adjustments are allowed only where required: -
DATETIME → TIMESTAMP - JSON → JSONB - ENUM → VARCHAR + CHECK constraint
(or PostgreSQL ENUM)

Hibernate must use: spring.jpa.hibernate.ddl-auto=validate

Flyway must create and manage schema.

------------------------------------------------------------------------

# DATABASE CONNECTION (Already Running)

Host: localhost Port: 5432 Database: attendly Username: dinuka@99
Password: Henzer@99

IMPORTANT: - NEVER hardcode credentials - Use environment variables -
Provide .env.example structure

------------------------------------------------------------------------

# ARCHITECTURE RULES

-   Controller → Service → Repository → Entity
-   Use DTOs for all API requests/responses
-   Never expose Entities directly
-   Use GlobalExceptionHandler
-   JWT-based stateless authentication
-   Role-based access (ADMIN / LECTURER / STUDENT)
-   All endpoints under /api/v1
-   Clean modular package structure

------------------------------------------------------------------------

# STRICT IMPLEMENTATION ORDER

You must implement in this exact sequence. Stop after each part and wait
for confirmation.

------------------------------------------------------------------------

## PART 1 --- Spring Boot Setup

1.  Generate Spring Boot 3 project (Java 17, Maven)
2.  Add dependencies:
    -   Spring Web
    -   Validation
    -   Spring Data JPA
    -   PostgreSQL Driver
    -   Spring Security
    -   Flyway
    -   Springdoc OpenAPI
    -   Lombok (optional)
3.  Provide:
    -   pom.xml
    -   main application class
4.  Confirm project runs

STOP after Part 1.

------------------------------------------------------------------------

## PART 2 --- PostgreSQL Configuration

1.  Create application.yml using environment variables
2.  Configure:
    -   datasource
    -   ddl-auto=validate
    -   Flyway enabled
3.  Add: GET /api/v1/health endpoint
4.  Show how to test DB connection

STOP after Part 2.

------------------------------------------------------------------------

## PART 3 --- Clean Architecture Structure

Create packages aligned to database tables:

modules/ - users - programmes - userprogrammes - modules -
programmemodules - enrolments - sessions - attendance - reports

Add: - GlobalExceptionHandler - ApiResponse wrapper

STOP after Part 3.

------------------------------------------------------------------------

## PART 4 --- Flyway Schema (STRICT DATABASE ALIGNMENT)

Create V1\_\_init.sql using EXACT tables from database design:

1.  users
2.  programmes
3.  user_programmes
4.  modules
5.  programme_modules
6.  module_enrolments
7.  sessions
8.  attendance

Constraints MUST include:

-   UNIQUE (user_id, programme_id)
-   UNIQUE (programme_id, module_id)
-   UNIQUE (module_id, student_id)
-   UNIQUE (session_id, student_id)

DO NOT rename any table.

Verify Flyway runs successfully.

STOP after Part 4.

------------------------------------------------------------------------

## PART 5 --- Authentication (Aligned with users table)

Users entity must map EXACTLY to:

-   id
-   full_name
-   email
-   password_hash
-   role
-   created_at
-   updated_at

Roles must be: - STUDENT - LECTURER - ADMIN

Implement: POST /api/v1/auth/login GET /api/v1/users/me

STOP after Part 5.

------------------------------------------------------------------------

## PART 6 --- Core CRUD Modules

For each table implement: Entity + Repository + Service + Controller +
DTOs

Implement modules for:

-   Users
-   Programmes
-   UserProgrammes
-   Modules
-   ProgrammeModules
-   ModuleEnrolments

All relationships must reflect the database design exactly.

STOP after Part 6.

------------------------------------------------------------------------

## PART 7 --- Sessions

Implement:

POST /api/v1/sessions POST /api/v1/sessions/{id}/open POST
/api/v1/sessions/{id}/close GET /api/v1/modules/{moduleId}/sessions

Fields must match sessions table exactly.

STOP after Part 7.

------------------------------------------------------------------------

## PART 8 --- Attendance (Core Feature)

POST /api/v1/attendance/mark

Validation Rules: - Session exists and is OPEN - Student enrolled -
Attendance code matches - Prevent duplicate marking

Store in attendance table exactly as defined.

STOP after Part 8.

------------------------------------------------------------------------

## PART 9 --- Reports

Implement:

GET /api/v1/sessions/{id}/attendance GET
/api/v1/reports/modules/{moduleId}/summary GET
/api/v1/students/me/attendance

Reports must derive strictly from existing tables.

STOP after Part 9.

------------------------------------------------------------------------

## PART 10 --- Swagger + Tests

1.  Configure OpenAPI documentation
2.  Add integration tests for:
    -   login
    -   attendance marking
3.  Ensure everything runs with PostgreSQL Docker

------------------------------------------------------------------------

# REQUIRED RESPONSE FORMAT

For EACH part:

1.  Short explanation
2.  Files created/modified
3.  Full code for those files
4.  How to run and test

Wait for confirmation before moving to next part.
