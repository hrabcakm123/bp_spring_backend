package com.example.bp_spring_backend.domains.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_assignment_logs")
public class StudentAssignmentLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_assignment_id", referencedColumnName = "id", nullable = false)
    private StudentAssignmentEntity studentAssignment;

    @Column(name = "original_points", nullable = false)
    private Double originalPoints;

    @Column(name = "updated_points", nullable = false)
    private Double updatedPoints;

    @ManyToOne
    @JoinColumn(name = "original_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity originalUser;

    @ManyToOne
    @JoinColumn(name = "updated_by_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity updatedByUser;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updatedAt;
}
