package com.attendly.api.modules.enrolments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleEnrolmentRepository extends JpaRepository<ModuleEnrolment, Long> {

    List<ModuleEnrolment> findByModuleId(Long moduleId);

    List<ModuleEnrolment> findByStudentId(Long studentId);

    boolean existsByModuleIdAndStudentId(Long moduleId, Long studentId);
}
