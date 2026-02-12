package com.attendly.api.modules.modules.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModuleRequest {

    @Size(max = 20, message = "Code must be at most 20 characters")
    private String code;

    @Size(max = 160, message = "Name must be at most 160 characters")
    private String name;

    private Short level;
}
