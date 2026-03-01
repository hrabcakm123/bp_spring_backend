package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentAssignmentLogMapper {

    public StudentAssignmentLogResponseDTO toDTO(StudentAssignmentLogEntity entity) {
        return StudentAssignmentLogResponseDTO.builder()
                .id(entity.getId())
                .studentFullName(entity.getStudentAssignment().getStudentEntity().getFullName())
                .studentAisId(entity.getStudentAssignment().getStudentEntity().getAisId())
                .blockName(entity.getStudentAssignment().getAssignmentEntity().getBlockEntity().getName())
                .assignmentName(entity.getStudentAssignment().getAssignmentEntity().getName())
                .originalPoints(entity.getOriginalPoints())
                .updatedPoints(entity.getUpdatedPoints())
                .originalUserFullName(entity.getOriginalUser().getFullName())
                .updatedByUserFullName(entity.getUpdatedByUser().getFullName())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
