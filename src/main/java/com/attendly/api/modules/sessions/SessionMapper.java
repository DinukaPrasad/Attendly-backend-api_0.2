package com.attendly.api.modules.sessions;

import com.attendly.api.modules.sessions.dto.SessionResponse;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionResponse toResponse(Session session) {
        return SessionResponse.builder()
                .id(session.getId())
                .moduleId(session.getModule().getId())
                .moduleCode(session.getModule().getCode())
                .moduleName(session.getModule().getName())
                .lecturerId(session.getLecturer().getId())
                .lecturerName(session.getLecturer().getFullName())
                .title(session.getTitle())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .status(session.getStatus().name())
                .attendanceCode(session.getAttendanceCode())
                .createdAt(session.getCreatedAt())
                .build();
    }
}
