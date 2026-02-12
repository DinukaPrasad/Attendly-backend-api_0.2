package com.attendly.api.modules.sessions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {
    private Long id;
    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private Long lecturerId;
    private String lecturerName;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String attendanceCode;
    private LocalDateTime createdAt;
}
