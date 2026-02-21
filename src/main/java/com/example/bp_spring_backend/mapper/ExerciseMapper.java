package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSubstitutionResponseDTO;
import com.example.bp_spring_backend.repository.ExerciseLeaderProjection;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapper {

    public ExerciseResponseDTO toDTO(ExerciseEntity exerciseEntity) {
        return ExerciseResponseDTO.builder()
                .id(exerciseEntity.getId())
                .firstSessionDate(exerciseEntity.getFirstSessionDate())
                .startTime(exerciseEntity.getStartTime())
                .roomEnum(exerciseEntity.getRoomEnum())
                .build();
    }

    public ExerciseSubstitutionResponseDTO toExerciseSubstitutionDTO(ExerciseLeaderProjection exerciseLeaderProjection) {
        return ExerciseSubstitutionResponseDTO.builder()
                .id(exerciseLeaderProjection.getId())
                .firstSessionDate(exerciseLeaderProjection.getFirstSessionDate())
                .startTime(exerciseLeaderProjection.getStartTime())
                .roomEnum(exerciseLeaderProjection.getRoomEnum())
                .leaderFullName(exerciseLeaderProjection.getLeaderFullName())
                .build();
    }

    public ExerciseEntity toEntity(ExerciseRequestDTO request) {
        return ExerciseEntity.builder()
                .firstSessionDate(request.getFirstSessionDate())
                .startTime(request.getStartTime())
                .roomEnum(request.getRoomEnum())
                .build();
    }
}
