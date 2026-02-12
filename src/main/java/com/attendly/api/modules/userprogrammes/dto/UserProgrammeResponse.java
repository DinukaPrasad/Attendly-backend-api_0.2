package com.attendly.api.modules.userprogrammes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgrammeResponse {
    private Long id;
    private Long userId;
    private String userFullName;
    private Long programmeId;
    private String programmeCode;
    private String programmeName;
    private LocalDateTime createdAt;
}
