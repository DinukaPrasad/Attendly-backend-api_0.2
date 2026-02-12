package com.attendly.api.modules.programmemodules.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProgrammeModuleRequest {

    @NotNull(message = "Programme ID is required")
    private Long programmeId;

    @NotNull(message = "Module ID is required")
    private Long moduleId;
}
