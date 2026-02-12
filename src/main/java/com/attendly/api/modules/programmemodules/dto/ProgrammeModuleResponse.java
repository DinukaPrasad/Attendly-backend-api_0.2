package com.attendly.api.modules.programmemodules.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammeModuleResponse {
    private Long id;
    private Long programmeId;
    private String programmeCode;
    private String programmeName;
    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private LocalDateTime createdAt;
}
