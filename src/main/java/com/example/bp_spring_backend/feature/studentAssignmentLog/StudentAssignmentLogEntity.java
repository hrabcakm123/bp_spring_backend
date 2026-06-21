package com.example.bp_spring_backend.feature.studentAssignmentLog;

import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentEntity;
import com.example.bp_spring_backend.feature.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_assignment_logs")
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE student_assignment_logs SET is_deleted = true WHERE id = ?")
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

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
