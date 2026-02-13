package com.attendly.api.modules.attendance;

import com.attendly.api.exception.DuplicateResourceException;
import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.attendance.dto.AttendanceResponse;
import com.attendly.api.modules.attendance.dto.MarkAttendanceRequest;
import com.attendly.api.modules.enrolments.ModuleEnrolmentRepository;
import com.attendly.api.modules.sessions.Session;
import com.attendly.api.modules.sessions.SessionRepository;
import com.attendly.api.modules.sessions.SessionStatus;
import com.attendly.api.modules.users.User;
import com.attendly.api.modules.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final ModuleEnrolmentRepository enrolmentRepository;
    private final AttendanceMapper mapper;

    @Transactional
    public AttendanceResponse markAttendance(MarkAttendanceRequest request) {
        // 1. Session exists
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session", request.getSessionId()));

        // 2. Session is OPEN
        if (session.getStatus() != SessionStatus.OPEN) {
            throw new IllegalStateException("Session is not open for attendance");
        }

        // 3. Student exists
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));

        // 4. Student is enrolled in the module
        if (!enrolmentRepository.existsByModuleIdAndStudentId(session.getModule().getId(), student.getId())) {
            throw new IllegalArgumentException("Student is not enrolled in this module");
        }

        // 5. Prevent duplicate marking
        if (attendanceRepository.existsBySessionIdAndStudentId(request.getSessionId(), request.getStudentId())) {
            throw new DuplicateResourceException("Attendance already marked for this student in this session");
        }

        // 6. Attendance code matches
        if (session.getAttendanceCode() != null &&
                !session.getAttendanceCode().equalsIgnoreCase(request.getAttendanceCode())) {
            throw new IllegalArgumentException("Invalid attendance code");
        }

        // Determine status (PRESENT or LATE based on session end time)
        AttendanceStatus status = LocalDateTime.now().isAfter(session.getEndTime())
                ? AttendanceStatus.LATE
                : AttendanceStatus.PRESENT;

        Attendance attendance = Attendance.builder()
                .session(session)
                .student(student)
                .markedAt(LocalDateTime.now())
                .status(status)
                .note(request.getNote())
                .evidenceJson(request.getEvidence())
                .build();

        return mapper.toResponse(attendanceRepository.save(attendance));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getBySessionId(Long sessionId) {
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
