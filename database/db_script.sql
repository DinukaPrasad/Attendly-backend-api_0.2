-- =====================================================
-- DATABASE
-- =====================================================
CREATE DATABASE IF NOT EXISTS attendly CHARACTER
SET
    utf8mb4 COLLATE utf8mb4_unicode_ci;

USE attendly;

-- =====================================================
-- 1) USERS
-- =====================================================
CREATE TABLE users (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM(
        'STUDENT',
        'LECTURER',
        'ADMIN'
    ) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- =====================================================
-- 2) PROGRAMMES (Degrees)
-- =====================================================
CREATE TABLE programmes (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(160) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- =====================================================
-- 3) USER_PROGRAMMES (Users ↔ Programmes M:N)
-- =====================================================
CREATE TABLE user_programmes (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    programme_id BIGINT UNSIGNED NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_prog_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_prog_programme FOREIGN KEY (programme_id) REFERENCES programmes (id) ON DELETE CASCADE,
    CONSTRAINT uq_user_programme UNIQUE (user_id, programme_id)
) ENGINE = InnoDB;

-- =====================================================
-- 4) MODULES
-- =====================================================
CREATE TABLE modules (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(160) NOT NULL,
    level TINYINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- =====================================================
-- 5) PROGRAMME_MODULES (Programmes ↔ Modules M:N)
-- =====================================================
CREATE TABLE programme_modules (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    programme_id BIGINT UNSIGNED NOT NULL,
    module_id BIGINT UNSIGNED NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prog_mod_programme FOREIGN KEY (programme_id) REFERENCES programmes (id) ON DELETE CASCADE,
    CONSTRAINT fk_prog_mod_module FOREIGN KEY (module_id) REFERENCES modules (id) ON DELETE CASCADE,
    CONSTRAINT uq_programme_module UNIQUE (programme_id, module_id)
) ENGINE = InnoDB;

-- =====================================================
-- 6) MODULE_ENROLMENTS (Students ↔ Modules M:N)
-- =====================================================
CREATE TABLE module_enrolments (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    module_id BIGINT UNSIGNED NOT NULL,
    student_id BIGINT UNSIGNED NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_menrol_module FOREIGN KEY (module_id) REFERENCES modules (id) ON DELETE CASCADE,
    CONSTRAINT fk_menrol_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_module_student UNIQUE (module_id, student_id)
) ENGINE = InnoDB;

-- =====================================================
-- 7) SESSIONS (Lectures / Labs / Tutorials)
-- =====================================================
CREATE TABLE sessions (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    module_id BIGINT UNSIGNED NOT NULL,
    lecturer_id BIGINT UNSIGNED NOT NULL,
    title VARCHAR(160) NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status ENUM('OPEN', 'CLOSED') NOT NULL DEFAULT 'CLOSED',
    attendance_code VARCHAR(10) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sessions_module FOREIGN KEY (module_id) REFERENCES modules (id),
    CONSTRAINT fk_sessions_lecturer FOREIGN KEY (lecturer_id) REFERENCES users (id),
    INDEX idx_sessions_module (module_id),
    INDEX idx_sessions_lecturer (lecturer_id),
    INDEX idx_sessions_status (status),
    INDEX idx_sessions_time (start_time, end_time)
) ENGINE = InnoDB;

-- =====================================================
-- 8) ATTENDANCE
-- =====================================================
CREATE TABLE attendance (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT UNSIGNED NOT NULL,
    student_id BIGINT UNSIGNED NOT NULL,
    marked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM(
        'PRESENT',
        'LATE',
        'ABSENT',
        'REJECTED'
    ) NOT NULL DEFAULT 'PRESENT',
    evidence_json JSON NULL,
    note VARCHAR(255) NULL,
    CONSTRAINT fk_attendance_session FOREIGN KEY (session_id) REFERENCES sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_attendance UNIQUE (session_id, student_id),
    INDEX idx_attendance_session (session_id),
    INDEX idx_attendance_student (student_id),
    INDEX idx_attendance_marked_at (marked_at)
) ENGINE = InnoDB;