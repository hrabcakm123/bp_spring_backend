package com.example.bp_spring_backend.feature.block;

import org.springframework.stereotype.Component;

@Component
public class BlockMapper {

    public BlockResponseDTO toDTO(BlockEntity blockEntity) {
        return BlockResponseDTO.builder()
                .id(blockEntity.getId())
                .name(blockEntity.getName())
                .maxPoints(blockEntity.getMaxPoints())
                .requiredPoints(blockEntity.getRequiredPoints())
                .build();
    }

    public BlockEntity toEntity(BlockRequestDTO request) {
        return BlockEntity.builder()
                .name(request.getName())
                .maxPoints(request.getMaxPoints())
                .requiredPoints(request.getRequiredPoints())
                .build();
    }
}
