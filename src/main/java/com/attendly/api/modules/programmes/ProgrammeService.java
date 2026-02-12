package com.attendly.api.modules.programmes;

import com.attendly.api.exception.DuplicateResourceException;
import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.programmes.dto.CreateProgrammeRequest;
import com.attendly.api.modules.programmes.dto.ProgrammeResponse;
import com.attendly.api.modules.programmes.dto.UpdateProgrammeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final ProgrammeMapper programmeMapper;

    @Transactional(readOnly = true)
    public List<ProgrammeResponse> getAllProgrammes() {
        return programmeRepository.findAll().stream()
                .map(programmeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProgrammeResponse getProgrammeById(Long id) {
        Programme programme = programmeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programme", id));
        return programmeMapper.toResponse(programme);
    }

    @Transactional
    public ProgrammeResponse createProgramme(CreateProgrammeRequest request) {
        if (programmeRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Programme with code " + request.getCode() + " already exists");
        }
        Programme programme = Programme.builder()
                .code(request.getCode())
                .name(request.getName())
                .build();
        return programmeMapper.toResponse(programmeRepository.save(programme));
    }

    @Transactional
    public ProgrammeResponse updateProgramme(Long id, UpdateProgrammeRequest request) {
        Programme programme = programmeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programme", id));
        if (request.getCode() != null) programme.setCode(request.getCode());
        if (request.getName() != null) programme.setName(request.getName());
        return programmeMapper.toResponse(programmeRepository.save(programme));
    }

    @Transactional
    public void deleteProgramme(Long id) {
        if (!programmeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Programme", id);
        }
        programmeRepository.deleteById(id);
    }
}
