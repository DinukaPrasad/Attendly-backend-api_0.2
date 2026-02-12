package com.attendly.api.modules.enrolments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnrolmentRequest {

    @NotNull(message = "Module ID is required")
    private Long moduleId;

    @NotNull(message = "Student ID is required")
    private Long studentId;
}
