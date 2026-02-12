package com.attendly.api.modules.userprogrammes;

import com.attendly.api.modules.userprogrammes.dto.UserProgrammeResponse;
import org.springframework.stereotype.Component;

@Component
public class UserProgrammeMapper {

    public UserProgrammeResponse toResponse(UserProgramme up) {
        return UserProgrammeResponse.builder()
                .id(up.getId())
                .userId(up.getUser().getId())
                .userFullName(up.getUser().getFullName())
                .programmeId(up.getProgramme().getId())
                .programmeCode(up.getProgramme().getCode())
                .programmeName(up.getProgramme().getName())
                .createdAt(up.getCreatedAt())
                .build();
    }
}
