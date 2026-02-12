package com.attendly.api.modules.programmemodules;

import com.attendly.api.exception.DuplicateResourceException;
import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.modules.ModuleRepository;
import com.attendly.api.modules.programmes.Programme;
import com.attendly.api.modules.programmes.ProgrammeRepository;
import com.attendly.api.modules.programmemodules.dto.CreateProgrammeModuleRequest;
import com.attendly.api.modules.programmemodules.dto.ProgrammeModuleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgrammeModuleService {

    private final ProgrammeModuleRepository repository;
    private final ProgrammeRepository programmeRepository;
    private final ModuleRepository moduleRepository;
    private final ProgrammeModuleMapper mapper;

    @Transactional(readOnly = true)
    public List<ProgrammeModuleResponse> getByProgrammeId(Long programmeId) {
        return repository.findByProgrammeId(programmeId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProgrammeModuleResponse> getByModuleId(Long moduleId) {
        return repository.findByModuleId(moduleId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ProgrammeModuleResponse create(CreateProgrammeModuleRequest request) {
        if (repository.existsByProgrammeIdAndModuleId(request.getProgrammeId(), request.getModuleId())) {
            throw new DuplicateResourceException("Module is already linked to this programme");
        }

        Programme programme = programmeRepository.findById(request.getProgrammeId())
                .orElseThrow(() -> new ResourceNotFoundException("Programme", request.getProgrammeId()));
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", request.getModuleId()));

        ProgrammeModule pm = ProgrammeModule.builder()
                .programme(programme)
                .module(module)
                .build();

        return mapper.toResponse(repository.save(pm));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ProgrammeModule", id);
        }
        repository.deleteById(id);
    }
}
