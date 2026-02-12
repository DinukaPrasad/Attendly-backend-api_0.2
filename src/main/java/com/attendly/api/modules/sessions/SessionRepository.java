package com.attendly.api.modules.sessions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByModuleId(Long moduleId);

    List<Session> findByLecturerId(Long lecturerId);

    List<Session> findByModuleIdAndStatus(Long moduleId, SessionStatus status);
}
