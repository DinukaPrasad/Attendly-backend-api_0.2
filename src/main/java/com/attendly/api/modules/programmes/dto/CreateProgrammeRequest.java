package com.attendly.api.modules.programmes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProgrammeRequest {

    @NotBlank(message = "Programme code is required")
    @Size(max = 20, message = "Code must be at most 20 characters")
    private String code;

    @NotBlank(message = "Programme name is required")
    @Size(max = 160, message = "Name must be at most 160 characters")
    private String name;
}
