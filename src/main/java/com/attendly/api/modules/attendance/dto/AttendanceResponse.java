package com.attendly.api.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private Long id;
    private Long sessionId;
    private String sessionTitle;
    private Long studentId;
    private String studentFullName;
    private LocalDateTime markedAt;
    private String status;
    private String note;
}
