package com.attendly.api.modules.userprogrammes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserProgrammeRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Programme ID is required")
    private Long programmeId;
}
