package com.attendly.api.modules.modules;

import com.attendly.api.common.ApiResponse;
import com.attendly.api.modules.modules.dto.CreateModuleRequest;
import com.attendly.api.modules.modules.dto.ModuleResponse;
import com.attendly.api.modules.modules.dto.UpdateModuleRequest;
import com.attendly.api.modules.sessions.SessionService;
import com.attendly.api.modules.sessions.dto.SessionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;
    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModuleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(moduleService.getAllModules()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(moduleService.getModuleById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ModuleResponse>> create(@Valid @RequestBody CreateModuleRequest request) {
        ModuleResponse response = moduleService.createModule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Module created", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ModuleResponse>> update(
            @PathVariable Long id, @Valid @RequestBody UpdateModuleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Module updated", moduleService.updateModule(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.ok(ApiResponse.success("Module deleted", null));
    }

    @GetMapping("/{moduleId}/sessions")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessionsByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(ApiResponse.success(sessionService.getByModuleId(moduleId)));
    }
}
