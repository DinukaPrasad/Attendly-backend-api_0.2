package com.attendly.api.modules.programmemodules;

import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.programmes.Programme;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity mapped EXACTLY to the 'programme_modules' table.
 * Columns: id, programme_id, module_id, created_at
 * UNIQUE (programme_id, module_id)
 */
@Entity
@Table(name = "programme_modules",
       uniqueConstraints = @UniqueConstraint(columnNames = {"programme_id", "module_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgrammeModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id", nullable = false)
    private Programme programme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
