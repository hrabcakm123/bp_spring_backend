package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
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

    public ExerciseEntity toEntity(ExerciseRequestDTO request) {
        return ExerciseEntity.builder()
                .firstSessionDate(request.getFirstSessionDate())
                .startTime(request.getStartTime())
                .roomEnum(request.getRoomEnum())
                .build();
    }
}
