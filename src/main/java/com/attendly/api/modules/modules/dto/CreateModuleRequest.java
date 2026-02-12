package com.attendly.api.modules.modules.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuleRequest {

    @NotBlank(message = "Module code is required")
    @Size(max = 20, message = "Code must be at most 20 characters")
    private String code;

    @NotBlank(message = "Module name is required")
    @Size(max = 160, message = "Name must be at most 160 characters")
    private String name;

    @NotNull(message = "Level is required")
    private Short level;
}
