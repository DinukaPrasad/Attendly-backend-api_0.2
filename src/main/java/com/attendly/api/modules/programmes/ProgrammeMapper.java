package com.attendly.api.modules.programmes;

import com.attendly.api.modules.programmes.dto.ProgrammeResponse;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeMapper {

    public ProgrammeResponse toResponse(Programme programme) {
        return ProgrammeResponse.builder()
                .id(programme.getId())
                .code(programme.getCode())
                .name(programme.getName())
                .createdAt(programme.getCreatedAt())
                .build();
    }
}
