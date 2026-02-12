package com.attendly.api.modules.enrolments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleEnrolmentResponse {
    private Long id;
    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private Long studentId;
    private String studentFullName;
    private LocalDateTime createdAt;
}
