package com.attendly.api.modules.userprogrammes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProgrammeRepository extends JpaRepository<UserProgramme, Long> {

    List<UserProgramme> findByUserId(Long userId);

    List<UserProgramme> findByProgrammeId(Long programmeId);

    boolean existsByUserIdAndProgrammeId(Long userId, Long programmeId);

    void deleteByUserIdAndProgrammeId(Long userId, Long programmeId);
}
