package com.example.bp_spring_backend.feature.studentAttendance;

import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionEntity;
import com.example.bp_spring_backend.feature.enums.AttendanceEnum;
import com.example.bp_spring_backend.feature.student.StudentEntity;
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
@Table(
        name = "student_attendances",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "exercise_session_id"})
        }
)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE student_attendances SET is_deleted = true WHERE id = ?")
public class StudentAttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private StudentEntity studentEntity;

    @ManyToOne
    @JoinColumn(name = "exercise_session_id", referencedColumnName = "id", nullable = false)
    private ExerciseSessionEntity exerciseSessionEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_enum", nullable = false)
    private AttendanceEnum attendanceEnum;

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
