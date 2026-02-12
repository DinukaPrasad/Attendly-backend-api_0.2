package com.attendly.api.modules.attendance;

import com.attendly.api.modules.attendance.dto.AttendanceResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .sessionId(attendance.getSession().getId())
                .sessionTitle(attendance.getSession().getTitle())
                .studentId(attendance.getStudent().getId())
                .studentFullName(attendance.getStudent().getFullName())
                .markedAt(attendance.getMarkedAt())
                .status(attendance.getStatus().name())
                .note(attendance.getNote())
                .build();
    }
}
