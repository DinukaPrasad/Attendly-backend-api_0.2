package com.attendly.api.modules.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findBySessionId(Long sessionId);

    List<Attendance> findByStudentId(Long studentId);

    boolean existsBySessionIdAndStudentId(Long sessionId, Long studentId);

    long countBySessionId(Long sessionId);

    long countBySessionIdAndStatus(Long sessionId, AttendanceStatus status);
}
