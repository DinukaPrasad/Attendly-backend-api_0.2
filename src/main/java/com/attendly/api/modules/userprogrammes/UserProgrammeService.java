package com.attendly.api.modules.userprogrammes;

import com.attendly.api.exception.DuplicateResourceException;
import com.attendly.api.exception.ResourceNotFoundException;
import com.attendly.api.modules.programmes.Programme;
import com.attendly.api.modules.programmes.ProgrammeRepository;
import com.attendly.api.modules.users.User;
import com.attendly.api.modules.users.UserRepository;
import com.attendly.api.modules.userprogrammes.dto.CreateUserProgrammeRequest;
import com.attendly.api.modules.userprogrammes.dto.UserProgrammeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProgrammeService {

    private final UserProgrammeRepository userProgrammeRepository;
    private final UserRepository userRepository;
    private final ProgrammeRepository programmeRepository;
    private final UserProgrammeMapper mapper;

    @Transactional(readOnly = true)
    public List<UserProgrammeResponse> getByUserId(Long userId) {
        return userProgrammeRepository.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserProgrammeResponse> getByProgrammeId(Long programmeId) {
        return userProgrammeRepository.findByProgrammeId(programmeId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public UserProgrammeResponse create(CreateUserProgrammeRequest request) {
        if (userProgrammeRepository.existsByUserIdAndProgrammeId(request.getUserId(), request.getProgrammeId())) {
            throw new DuplicateResourceException("User is already linked to this programme");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        Programme programme = programmeRepository.findById(request.getProgrammeId())
                .orElseThrow(() -> new ResourceNotFoundException("Programme", request.getProgrammeId()));

        UserProgramme up = UserProgramme.builder()
                .user(user)
                .programme(programme)
                .build();

        return mapper.toResponse(userProgrammeRepository.save(up));
    }

    @Transactional
    public void delete(Long id) {
        if (!userProgrammeRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserProgramme", id);
        }
        userProgrammeRepository.deleteById(id);
    }
}
