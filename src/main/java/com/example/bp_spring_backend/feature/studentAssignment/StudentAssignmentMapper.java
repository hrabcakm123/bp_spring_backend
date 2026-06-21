package com.example.bp_spring_backend.feature.studentAssignment;

import com.example.bp_spring_backend.feature.assignment.AssignmentEntity;
import com.example.bp_spring_backend.feature.assignment.AssignmentMapper;
import com.example.bp_spring_backend.feature.student.StudentEntity;
import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.feature.student.StudentMapper;
import com.example.bp_spring_backend.feature.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StudentAssignmentMapper {

    private final AssignmentMapper assignmentMapper;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;

    public StudentAssignmentResponseDTO toDTO(StudentAssignmentEntity studentAssignmentEntity) {
        return StudentAssignmentResponseDTO.builder()
                .id(studentAssignmentEntity.getId())
                .assignment(assignmentMapper.toDTO(studentAssignmentEntity.getAssignmentEntity()))
                .student(studentMapper.toDTO(studentAssignmentEntity.getStudentEntity()))
                .earnedPoints(studentAssignmentEntity.getEarnedPoints())
                .note(studentAssignmentEntity.getNote())
                .createdBy(userMapper.toDTO(studentAssignmentEntity.getCreatedBy()))
                .createdAt(studentAssignmentEntity.getCreatedAt())
                .updatedBy(studentAssignmentEntity.getUpdatedBy() == null ? null : userMapper.toDTO(studentAssignmentEntity.getUpdatedBy()))
                .updatedAt(studentAssignmentEntity.getUpdatedAt())
                .build();
    }

    public StudentAssignmentEntity toEntity(StudentAssignmentRequestDTO request, AssignmentEntity assignmentEntity, StudentEntity studentEntity, UserEntity createdBy, LocalDateTime createdAt, UserEntity updatedBy, LocalDateTime updatedAt) {
        return StudentAssignmentEntity.builder()
                .assignmentEntity(assignmentEntity)
                .studentEntity(studentEntity)
                .earnedPoints(request.getEarnedPoints())
                .note(request.getNote())
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .build();
    }
}
