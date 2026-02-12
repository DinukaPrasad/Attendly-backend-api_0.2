-- =============================================
-- Attendly Database Schema
-- Strict alignment with Database Design Document
-- PostgreSQL adaptations: DATETIME→TIMESTAMP, JSON→JSONB, ENUM→VARCHAR+CHECK
-- =============================================

-- 1. users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (
        role IN (
            'STUDENT',
            'LECTURER',
            'ADMIN'
        )
    ),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. programmes
CREATE TABLE programmes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(160) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. user_programmes (junction: users ↔ programmes)
CREATE TABLE user_programmes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    programme_id BIGINT NOT NULL REFERENCES programmes (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_programme UNIQUE (user_id, programme_id)
);

-- 4. modules
CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(160) NOT NULL,
    level SMALLINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. programme_modules (junction: programmes ↔ modules)
CREATE TABLE programme_modules (
    id BIGSERIAL PRIMARY KEY,
    programme_id BIGINT NOT NULL REFERENCES programmes (id) ON DELETE CASCADE,
    module_id BIGINT NOT NULL REFERENCES modules (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_programme_module UNIQUE (programme_id, module_id)
);

-- 6. module_enrolments (junction: modules ↔ students)
CREATE TABLE module_enrolments (
    id BIGSERIAL PRIMARY KEY,
    module_id BIGINT NOT NULL REFERENCES modules (id) ON DELETE CASCADE,
    student_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_module_student UNIQUE (module_id, student_id)
);

-- 7. sessions
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    module_id BIGINT NOT NULL REFERENCES modules (id) ON DELETE CASCADE,
    lecturer_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title VARCHAR(160) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'CLOSED' CHECK (status IN ('OPEN', 'CLOSED')),
    attendance_code VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. attendance
CREATE TABLE attendance (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions (id) ON DELETE CASCADE,
    student_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    marked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) NOT NULL CHECK (
        status IN (
            'PRESENT',
            'LATE',
            'ABSENT',
            'REJECTED'
        )
    ),
    evidence_json JSONB,
    note VARCHAR(255),
    CONSTRAINT uq_session_student UNIQUE (session_id, student_id)
);

-- Indexes for performance
CREATE INDEX idx_users_email ON users (email);

CREATE INDEX idx_users_role ON users (role);

CREATE INDEX idx_sessions_module ON sessions (module_id);

CREATE INDEX idx_sessions_lecturer ON sessions (lecturer_id);

CREATE INDEX idx_attendance_session ON attendance (session_id);

CREATE INDEX idx_attendance_student ON attendance (student_id);

CREATE INDEX idx_module_enrolments_module ON module_enrolments (module_id);

CREATE INDEX idx_module_enrolments_student ON module_enrolments (student_id);