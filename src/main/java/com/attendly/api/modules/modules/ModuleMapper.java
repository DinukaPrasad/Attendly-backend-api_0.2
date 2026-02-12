package com.attendly.api.modules.modules;

import com.attendly.api.modules.modules.dto.ModuleResponse;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public ModuleResponse toResponse(Module module) {
        return ModuleResponse.builder()
                .id(module.getId())
                .code(module.getCode())
                .name(module.getName())
                .level(module.getLevel())
                .createdAt(module.getCreatedAt())
                .build();
    }
}
