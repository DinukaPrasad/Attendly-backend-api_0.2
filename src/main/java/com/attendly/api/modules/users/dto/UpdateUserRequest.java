package com.attendly.api.modules.users.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(max = 120, message = "Full name must be at most 120 characters")
    private String fullName;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String role;
}
