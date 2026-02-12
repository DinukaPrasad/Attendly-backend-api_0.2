package com.attendly.api.modules.programmes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammeResponse {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime createdAt;
}
