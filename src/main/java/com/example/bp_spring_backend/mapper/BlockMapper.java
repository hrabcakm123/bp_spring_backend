package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.BlockEntity;
import com.example.bp_spring_backend.domains.inputDTO.BlockRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.BlockResponseDTO;
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
