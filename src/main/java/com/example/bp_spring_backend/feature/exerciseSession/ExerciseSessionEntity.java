package com.example.bp_spring_backend.feature.exerciseSession;

import com.example.bp_spring_backend.feature.exercise.ExerciseEntity;
import com.example.bp_spring_backend.feature.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "exercise_sessions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"exercise_id", "session_date"})
        }
)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE exercise_sessions SET is_deleted = true WHERE id = ?")
public class ExerciseSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private ExerciseEntity exerciseEntity;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private UserEntity createdBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id", nullable = true)
    private UserEntity updatedBy;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime updatedAt;
}
