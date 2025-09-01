package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ExerciseSessionMapper {

    private final ExerciseMapper exerciseMapper;

    public ExerciseSessionResponseDTO toDTO(ExerciseSessionEntity exerciseSessionEntity) {
        return ExerciseSessionResponseDTO.builder()
                .id(exerciseSessionEntity.getId())
                .exercise(exerciseMapper.toDTO(exerciseSessionEntity.getExerciseEntity()))
                .sessionDate(exerciseSessionEntity.getSessionDate())
                .build();
    }

    public ExerciseSessionEntity toEntity(ExerciseSessionRequestDTO request, ExerciseEntity exerciseEntity, UserEntity createdBy, LocalDateTime createdAt, UserEntity updatedBy, LocalDateTime updatedAt) {
        return ExerciseSessionEntity.builder()
                .exerciseEntity(exerciseEntity)
                .sessionDate(request.getSessionDate())
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .build();
    }
}
