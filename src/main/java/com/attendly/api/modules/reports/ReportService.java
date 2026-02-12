package com.attendly.api.modules.reports;

import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.attendance.Attendance;
import com.attendly.api.modules.attendance.AttendanceRepository;
import com.attendly.api.modules.attendance.AttendanceStatus;
import com.attendly.api.modules.attendance.dto.AttendanceResponse;
import com.attendly.api.modules.attendance.AttendanceMapper;
import com.attendly.api.modules.enrolments.ModuleEnrolmentRepository;
import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.modules.ModuleRepository;
import com.attendly.api.modules.reports.dto.ModuleAttendanceSummary;
import com.attendly.api.modules.reports.dto.StudentAttendanceRecord;
import com.attendly.api.modules.sessions.Session;
import com.attendly.api.modules.sessions.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleEnrolmentRepository enrolmentRepository;
    private final AttendanceMapper attendanceMapper;

    /**
     * GET /api/v1/sessions/{id}/attendance
     * Returns all attendance records for a specific session.
     */
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getSessionAttendance(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new ResourceNotFoundException("Session", sessionId);
        }
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    /**
     * GET /api/v1/reports/modules/{moduleId}/summary
     * Returns attendance summary for a module across all its sessions.
     */
    @Transactional(readOnly = true)
    public ModuleAttendanceSummary getModuleSummary(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module", moduleId));

        List<Session> sessions = sessionRepository.findByModuleId(moduleId);
        long totalSessions = sessions.size();
        long totalEnrolled = enrolmentRepository.findByModuleId(moduleId).size();

        long totalAttendance = 0;
        long presentCount = 0;
        long lateCount = 0;
        long absentCount = 0;

        for (Session session : sessions) {
            totalAttendance += attendanceRepository.countBySessionId(session.getId());
            presentCount += attendanceRepository.countBySessionIdAndStatus(session.getId(), AttendanceStatus.PRESENT);
            lateCount += attendanceRepository.countBySessionIdAndStatus(session.getId(), AttendanceStatus.LATE);
            absentCount += attendanceRepository.countBySessionIdAndStatus(session.getId(), AttendanceStatus.ABSENT);
        }

        double percentage = 0.0;
        long possibleAttendance = totalSessions * totalEnrolled;
        if (possibleAttendance > 0) {
            percentage = ((double) (presentCount + lateCount) / possibleAttendance) * 100.0;
        }

        return ModuleAttendanceSummary.builder()
                .moduleId(module.getId())
                .moduleCode(module.getCode())
                .moduleName(module.getName())
                .totalSessions(totalSessions)
                .totalEnrolledStudents(totalEnrolled)
                .totalAttendanceRecords(totalAttendance)
                .presentCount(presentCount)
                .lateCount(lateCount)
                .absentCount(absentCount)
                .attendancePercentage(Math.round(percentage * 100.0) / 100.0)
                .build();
    }

    /**
     * GET /api/v1/students/me/attendance
     * Returns attendance records for the currently authenticated student.
     */
    @Transactional(readOnly = true)
    public List<StudentAttendanceRecord> getStudentAttendance(Long studentId) {
        List<Attendance> records = attendanceRepository.findByStudentId(studentId);

        return records.stream()
                .map(a -> StudentAttendanceRecord.builder()
                        .sessionId(a.getSession().getId())
                        .sessionTitle(a.getSession().getTitle())
                        .moduleCode(a.getSession().getModule().getCode())
                        .moduleName(a.getSession().getModule().getName())
                        .startTime(a.getSession().getStartTime())
                        .endTime(a.getSession().getEndTime())
                        .status(a.getStatus().name())
                        .markedAt(a.getMarkedAt())
                        .build())
                .toList();
    }
}
