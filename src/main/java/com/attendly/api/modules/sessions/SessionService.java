package com.attendly.api.modules.sessions;

import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.modules.ModuleRepository;
import com.attendly.api.modules.sessions.dto.CreateSessionRequest;
import com.attendly.api.modules.sessions.dto.SessionResponse;
import com.attendly.api.modules.users.Role;
import com.attendly.api.modules.users.User;
import com.attendly.api.modules.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    @Transactional(readOnly = true)
    public List<SessionResponse> getByModuleId(Long moduleId) {
        return sessionRepository.findByModuleId(moduleId).stream()
                .map(sessionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SessionResponse getById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));
        return sessionMapper.toResponse(session);
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request) {
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", request.getModuleId()));
        User lecturer = userRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer", request.getLecturerId()));

        if (lecturer.getRole() != Role.LECTURER && lecturer.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("User must have LECTURER or ADMIN role to create a session");
        }

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Session session = Session.builder()
                .module(module)
                .lecturer(lecturer)
                .title(request.getTitle())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(SessionStatus.CLOSED)
                .attendanceCode(request.getAttendanceCode())
                .build();

        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Transactional
    public SessionResponse openSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));

        if (session.getStatus() == SessionStatus.OPEN) {
            throw new IllegalStateException("Session is already open");
        }

        // Generate attendance code if not already set
        if (session.getAttendanceCode() == null || session.getAttendanceCode().isBlank()) {
            session.setAttendanceCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        session.setStatus(SessionStatus.OPEN);
        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Transactional
    public SessionResponse closeSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));

        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new IllegalStateException("Session is already closed");
        }

        session.setStatus(SessionStatus.CLOSED);
        return sessionMapper.toResponse(sessionRepository.save(session));
    }
}
