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
                .dayOfWeek(exerciseEntity.getDayOfWeek())
                .startTime(exerciseEntity.getStartTime())
                .endTime(exerciseEntity.getEndTime())
                .roomEnum(exerciseEntity.getRoomEnum())
                .build();
    }

    public ExerciseEntity toEntity(ExerciseRequestDTO request) {
        return ExerciseEntity.builder()
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .roomEnum(request.getRoomEnum())
                .build();
    }
}
