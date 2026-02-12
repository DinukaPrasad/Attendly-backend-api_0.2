package com.attendly.api.modules.enrolments;

import com.attendly.api.modules.enrolments.dto.ModuleEnrolmentResponse;
import org.springframework.stereotype.Component;

@Component
public class ModuleEnrolmentMapper {

    public ModuleEnrolmentResponse toResponse(ModuleEnrolment enrolment) {
        return ModuleEnrolmentResponse.builder()
                .id(enrolment.getId())
                .moduleId(enrolment.getModule().getId())
                .moduleCode(enrolment.getModule().getCode())
                .moduleName(enrolment.getModule().getName())
                .studentId(enrolment.getStudent().getId())
                .studentFullName(enrolment.getStudent().getFullName())
                .createdAt(enrolment.getCreatedAt())
                .build();
    }
}
