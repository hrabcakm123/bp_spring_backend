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
        name = "user_exercises",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "exercise_id"})
        }
)
@SQLRestriction("is_deleted = false")
//@SQLDelete(sql = "UPDATE user_exercises SET is_deleted = true WHERE id = ?")
public class UserExerciseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_gen")
        @SequenceGenerator(name = "users_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private ExerciseEntity exerciseEntity;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
