package com.example.bp_spring_backend.feature.studentAttendance;

import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionEntity;
import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionMapper;
import com.example.bp_spring_backend.feature.student.StudentEntity;
import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.feature.student.StudentMapper;
import com.example.bp_spring_backend.feature.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StudentAttendanceMapper {

    private final StudentMapper studentMapper;
    private final ExerciseSessionMapper exerciseSessionMapper;
    private final UserMapper userMapper;

    public StudentAttendanceResponseDTO toDTO(StudentAttendanceEntity studentAttendanceEntity) {
        return StudentAttendanceResponseDTO.builder()
                .id(studentAttendanceEntity.getId())
                .student(studentMapper.toDTO(studentAttendanceEntity.getStudentEntity()))
                .exerciseSession(exerciseSessionMapper.toDTO(studentAttendanceEntity.getExerciseSessionEntity()))
                .attendanceEnum(studentAttendanceEntity.getAttendanceEnum())
                .createdBy(userMapper.toDTO(studentAttendanceEntity.getCreatedBy()))
                .createdAt(studentAttendanceEntity.getCreatedAt())
                .updatedBy(studentAttendanceEntity.getUpdatedBy() == null ? null : userMapper.toDTO(studentAttendanceEntity.getUpdatedBy()))
                .updatedAt(studentAttendanceEntity.getUpdatedAt())
                .build();
    }

    public StudentAttendanceEntity toEntity(StudentAttendanceRequestDTO request, StudentEntity studentEntity, ExerciseSessionEntity exerciseSessionEntity, UserEntity createdBy, LocalDateTime createdAt, UserEntity updatedBy, LocalDateTime updatedAt) {
        return StudentAttendanceEntity.builder()
                .studentEntity(studentEntity)
                .exerciseSessionEntity(exerciseSessionEntity)
                .attendanceEnum(request.getAttendanceEnum())
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .build();
    }
}
