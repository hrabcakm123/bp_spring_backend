package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StudentAssignmentMapper {

    private final AssignmentMapper assignmentMapper;
    private final StudentMapper studentMapper;
    private final ExerciseSessionMapper exerciseSessionMapper;
    private final UserMapper userMapper;

    public StudentAssignmentResponseDTO toDTO(StudentAssignmentEntity studentAssignmentEntity) {
        return StudentAssignmentResponseDTO.builder()
                .id(studentAssignmentEntity.getId())
                .assignment(assignmentMapper.toDTO(studentAssignmentEntity.getAssignmentEntity()))
                .student(studentMapper.toDTO(studentAssignmentEntity.getStudentEntity()))
                .exerciseSession(exerciseSessionMapper.toDTO(studentAssignmentEntity.getExerciseSessionEntity()))
                .earnedPoints(studentAssignmentEntity.getEarnedPoints())
                .createdBy(userMapper.toDTO(studentAssignmentEntity.getCreatedBy()))
                .createdAt(studentAssignmentEntity.getCreatedAt())
                .updatedBy(studentAssignmentEntity.getUpdatedBy() == null ? null : userMapper.toDTO(studentAssignmentEntity.getUpdatedBy()))
                .updatedAt(studentAssignmentEntity.getUpdatedAt())
                .build();
    }

    public StudentAssignmentEntity toEntity(StudentAssignmentRequestDTO request, AssignmentEntity assignmentEntity, StudentEntity studentEntity, ExerciseSessionEntity exerciseSessionEntity, UserEntity createdBy, LocalDateTime createdAt, UserEntity updatedBy, LocalDateTime updatedAt) {
        return StudentAssignmentEntity.builder()
                .assignmentEntity(assignmentEntity)
                .studentEntity(studentEntity)
                .exerciseSessionEntity(exerciseSessionEntity)
                .earnedPoints(request.getEarnedPoints())
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .build();
    }
}
