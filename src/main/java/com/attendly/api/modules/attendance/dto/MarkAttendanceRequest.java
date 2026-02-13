package com.attendly.api.modules.attendance.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkAttendanceRequest {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Attendance code is required")
    @Size(max = 10, message = "Attendance code must be at most 10 characters")
    private String attendanceCode;

    @Size(max = 255, message = "Note must be at most 255 characters")
    private String note;

    private JsonNode evidence;
}
