package com.attendly.api.modules.reports;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.attendance.dto.AttendanceResponse;
import com.attendly.api.modules.reports.dto.ModuleAttendanceSummary;
import com.attendly.api.modules.reports.dto.StudentAttendanceRecord;
import com.attendly.api.modules.users.User;
import com.attendly.api.modules.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    @GetMapping("/api/v1/sessions/{id}/attendance")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getSessionAttendance(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getSessionAttendance(id)));
    }

    @GetMapping("/api/v1/reports/modules/{moduleId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<ModuleAttendanceSummary>> getModuleSummary(@PathVariable Long moduleId) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getModuleSummary(moduleId)));
    }

    @GetMapping("/api/v1/students/me/attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<StudentAttendanceRecord>>> getMyAttendance(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(reportService.getStudentAttendance(user.getId())));
    }
}
