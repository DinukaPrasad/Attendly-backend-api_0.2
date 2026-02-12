package com.attendly.api.modules.programmemodules;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.programmemodules.dto.CreateProgrammeModuleRequest;
import com.attendly.api.modules.programmemodules.dto.ProgrammeModuleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programme-modules")
@RequiredArgsConstructor
public class ProgrammeModuleController {

    private final ProgrammeModuleService service;

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<ApiResponse<List<ProgrammeModuleResponse>>> getByProgramme(@PathVariable Long programmeId) {
        return ResponseEntity.ok(ApiResponse.success(service.getByProgrammeId(programmeId)));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<ApiResponse<List<ProgrammeModuleResponse>>> getByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(ApiResponse.success(service.getByModuleId(moduleId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProgrammeModuleResponse>> create(
            @Valid @RequestBody CreateProgrammeModuleRequest request) {
        ProgrammeModuleResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Module linked to programme", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Programme-Module link removed", null));
    }
}
