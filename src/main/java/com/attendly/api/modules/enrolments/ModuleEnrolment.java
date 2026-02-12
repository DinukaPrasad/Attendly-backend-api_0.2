package com.attendly.api.modules.enrolments;

import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity mapped EXACTLY to the 'module_enrolments' table.
 * Columns: id, module_id, student_id, created_at
 * UNIQUE (module_id, student_id)
 */
@Entity
@Table(name = "module_enrolments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"module_id", "student_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleEnrolment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
