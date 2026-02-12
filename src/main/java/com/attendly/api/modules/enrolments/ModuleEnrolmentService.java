package com.attendly.api.modules.enrolments;

import com.attendly.api.exception.DuplicateResourceException;
import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.enrolments.dto.CreateEnrolmentRequest;
import com.attendly.api.modules.enrolments.dto.ModuleEnrolmentResponse;
import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.modules.ModuleRepository;
import com.attendly.api.modules.users.Role;
import com.attendly.api.modules.users.User;
import com.attendly.api.modules.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleEnrolmentService {

    private final ModuleEnrolmentRepository enrolmentRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final ModuleEnrolmentMapper mapper;

    @Transactional(readOnly = true)
    public List<ModuleEnrolmentResponse> getByModuleId(Long moduleId) {
        return enrolmentRepository.findByModuleId(moduleId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ModuleEnrolmentResponse> getByStudentId(Long studentId) {
        return enrolmentRepository.findByStudentId(studentId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ModuleEnrolmentResponse enrol(CreateEnrolmentRequest request) {
        if (enrolmentRepository.existsByModuleIdAndStudentId(request.getModuleId(), request.getStudentId())) {
            throw new DuplicateResourceException("Student is already enrolled in this module");
        }

        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", request.getModuleId()));
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("User must have STUDENT role to enrol in a module");
        }

        ModuleEnrolment enrolment = ModuleEnrolment.builder()
                .module(module)
                .student(student)
                .build();

        return mapper.toResponse(enrolmentRepository.save(enrolment));
    }

    @Transactional
    public void remove(Long id) {
        if (!enrolmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("ModuleEnrolment", id);
        }
        enrolmentRepository.deleteById(id);
    }
}
