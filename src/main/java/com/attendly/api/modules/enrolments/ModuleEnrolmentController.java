package com.attendly.api.modules.enrolments;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.enrolments.dto.CreateEnrolmentRequest;
import com.attendly.api.modules.enrolments.dto.ModuleEnrolmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrolments")
@RequiredArgsConstructor
public class ModuleEnrolmentController {

    private final ModuleEnrolmentService service;

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<ApiResponse<List<ModuleEnrolmentResponse>>> getByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(ApiResponse.success(service.getByModuleId(moduleId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<ModuleEnrolmentResponse>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(service.getByStudentId(studentId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<ModuleEnrolmentResponse>> enrol(
            @Valid @RequestBody CreateEnrolmentRequest request) {
        ModuleEnrolmentResponse response = service.enrol(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Student enrolled", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success("Enrolment removed", null));
    }
}
