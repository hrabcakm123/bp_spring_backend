package com.example.bp_spring_backend.feature.assignment;

import com.example.bp_spring_backend.feature.block.BlockEntity;
import com.example.bp_spring_backend.feature.block.BlockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentMapper {

    private final BlockMapper blockMapper;

    public AssignmentResponseDTO toDTO(AssignmentEntity assignmentEntity) {
        return AssignmentResponseDTO.builder()
                .id(assignmentEntity.getId())
                .block(blockMapper.toDTO(assignmentEntity.getBlockEntity()))
                .name(assignmentEntity.getName())
                .maxPoints(assignmentEntity.getMaxPoints())
                .build();
    }

    public AssignmentEntity toEntity(AssignmentRequestDTO request, BlockEntity blockEntity) {
        return AssignmentEntity.builder()
                .blockEntity(blockEntity)
                .name(request.getName())
                .maxPoints(request.getMaxPoints())
                .build();
    }
}
