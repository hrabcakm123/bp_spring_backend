package com.example.bp_spring_backend.domains.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "student_exercises",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id"})
        }
)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE student_exercises SET is_deleted = true WHERE id = ?")
public class StudentExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private StudentEntity studentEntity;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private ExerciseEntity exerciseEntity;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
