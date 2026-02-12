package com.attendly.api.modules.sessions;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.sessions.dto.CreateSessionRequest;
import com.attendly.api.modules.sessions.dto.SessionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<SessionResponse>> create(@Valid @RequestBody CreateSessionRequest request) {
        SessionResponse response = sessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Session created", response));
    }

    @PostMapping("/{id}/open")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<SessionResponse>> open(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Session opened", sessionService.openSession(id)));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<SessionResponse>> close(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Session closed", sessionService.closeSession(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sessionService.getById(id)));
    }
}

