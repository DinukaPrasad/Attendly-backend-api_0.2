package com.attendly.api.modules.programmes;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.programmes.dto.CreateProgrammeRequest;
import com.attendly.api.modules.programmes.dto.ProgrammeResponse;
import com.attendly.api.modules.programmes.dto.UpdateProgrammeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programmes")
@RequiredArgsConstructor
public class ProgrammeController {

    private final ProgrammeService programmeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgrammeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(programmeService.getAllProgrammes()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgrammeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(programmeService.getProgrammeById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProgrammeResponse>> create(@Valid @RequestBody CreateProgrammeRequest request) {
        ProgrammeResponse response = programmeService.createProgramme(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Programme created", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProgrammeResponse>> update(
            @PathVariable Long id, @Valid @RequestBody UpdateProgrammeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Programme updated", programmeService.updateProgramme(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        programmeService.deleteProgramme(id);
        return ResponseEntity.ok(ApiResponse.success("Programme deleted", null));
    }
}
