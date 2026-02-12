package com.attendly.api.modules.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleAttendanceSummary {
    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private long totalSessions;
    private long totalEnrolledStudents;
    private long totalAttendanceRecords;
    private long presentCount;
    private long lateCount;
    private long absentCount;
    private double attendancePercentage;
}
