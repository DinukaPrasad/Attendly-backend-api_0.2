package com.attendly.api.modules.programmes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {

    Optional<Programme> findByCode(String code);

    boolean existsByCode(String code);
}
