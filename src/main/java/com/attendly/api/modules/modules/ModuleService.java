package com.attendly.api.modules.modules;

import com.attendly.api.exception.DuplicateResourceException;
import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.modules.dto.CreateModuleRequest;
import com.attendly.api.modules.modules.dto.ModuleResponse;
import com.attendly.api.modules.modules.dto.UpdateModuleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Transactional(readOnly = true)
    public List<ModuleResponse> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(moduleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ModuleResponse getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", id));
        return moduleMapper.toResponse(module);
    }

    @Transactional
    public ModuleResponse createModule(CreateModuleRequest request) {
        if (moduleRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Module with code " + request.getCode() + " already exists");
        }
        Module module = Module.builder()
                .code(request.getCode())
                .name(request.getName())
                .level(request.getLevel())
                .build();
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Transactional
    public ModuleResponse updateModule(Long id, UpdateModuleRequest request) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", id));
        if (request.getCode() != null) module.setCode(request.getCode());
        if (request.getName() != null) module.setName(request.getName());
        if (request.getLevel() != null) module.setLevel(request.getLevel());
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Transactional
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Module", id);
        }
        moduleRepository.deleteById(id);
    }
}
