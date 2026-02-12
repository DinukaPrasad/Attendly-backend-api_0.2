package com.attendly.api.modules.userprogrammes;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.userprogrammes.dto.CreateUserProgrammeRequest;
import com.attendly.api.modules.userprogrammes.dto.UserProgrammeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-programmes")
@RequiredArgsConstructor
public class UserProgrammeController {

    private final UserProgrammeService service;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<UserProgrammeResponse>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(service.getByUserId(userId)));
    }

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<ApiResponse<List<UserProgrammeResponse>>> getByProgramme(@PathVariable Long programmeId) {
        return ResponseEntity.ok(ApiResponse.success(service.getByProgrammeId(programmeId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProgrammeResponse>> create(
            @Valid @RequestBody CreateUserProgrammeRequest request) {
        UserProgrammeResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User linked to programme", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("User-Programme link removed", null));
    }
}
