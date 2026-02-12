package com.attendly.api.modules.programmemodules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgrammeModuleRepository extends JpaRepository<ProgrammeModule, Long> {

    List<ProgrammeModule> findByProgrammeId(Long programmeId);

    List<ProgrammeModule> findByModuleId(Long moduleId);

    boolean existsByProgrammeIdAndModuleId(Long programmeId, Long moduleId);
}
