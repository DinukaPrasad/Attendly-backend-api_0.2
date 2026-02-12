package com.attendly.api.modules.programmemodules;

import com.attendly.api.modules.programmemodules.dto.ProgrammeModuleResponse;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeModuleMapper {

    public ProgrammeModuleResponse toResponse(ProgrammeModule pm) {
        return ProgrammeModuleResponse.builder()
                .id(pm.getId())
                .programmeId(pm.getProgramme().getId())
                .programmeCode(pm.getProgramme().getCode())
                .programmeName(pm.getProgramme().getName())
                .moduleId(pm.getModule().getId())
                .moduleCode(pm.getModule().getCode())
                .moduleName(pm.getModule().getName())
                .createdAt(pm.getCreatedAt())
                .build();
    }
}
