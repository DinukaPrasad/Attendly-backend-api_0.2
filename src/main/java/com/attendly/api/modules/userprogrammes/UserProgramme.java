package com.attendly.api.modules.userprogrammes;

import com.attendly.api.modules.programmes.Programme;
import com.attendly.api.modules.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity mapped EXACTLY to the 'user_programmes' table.
 * Columns: id, user_id, programme_id, created_at
 * UNIQUE (user_id, programme_id)
 */
@Entity
@Table(name = "user_programmes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "programme_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id", nullable = false)
    private Programme programme;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
