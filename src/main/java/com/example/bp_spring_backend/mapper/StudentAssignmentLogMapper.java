package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentAssignmentLogMapper {

    private final StudentAssignmentMapper studentAssignmentMapper;
    private final UserMapper userMapper;

    public StudentAssignmentLogResponseDTO toDTO(StudentAssignmentLogEntity entity) {
        return StudentAssignmentLogResponseDTO.builder()
                .id(entity.getId())
                .studentAssignment(studentAssignmentMapper.toDTO(entity.getStudentAssignment()))
                .originalPoints(entity.getOriginalPoints())
                .updatedPoints(entity.getUpdatedPoints())
                .originalUser(userMapper.toDTO(entity.getOriginalUser()))
                .updatedByUser(userMapper.toDTO(entity.getUpdatedByUser()))
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
