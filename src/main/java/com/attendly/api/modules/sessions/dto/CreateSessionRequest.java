package com.attendly.api.modules.sessions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {

    @NotNull(message = "Module ID is required")
    private Long moduleId;

    @NotNull(message = "Lecturer ID is required")
    private Long lecturerId;

    @NotBlank(message = "Title is required")
    @Size(max = 160, message = "Title must be at most 160 characters")
    private String title;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Size(max = 10, message = "Attendance code must be at most 10 characters")
    private String attendanceCode;
}
