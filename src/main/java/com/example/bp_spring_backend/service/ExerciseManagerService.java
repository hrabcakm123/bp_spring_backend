package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseManagerService {

    private final ExerciseService exerciseService;
    private final ExerciseSessionService exerciseSessionService;
    private final ExerciseMapper exerciseMapper;

    public List<ExerciseResponseDTO> addExercisesWithSessions(List<ExerciseRequestDTO> request) {

        List<ExerciseEntity> exercises = exerciseService.addExerciseEntities(request);

        exerciseSessionService.addInitialSessionsForExercises(exercises);

        return exercises.stream().map(exerciseMapper::toDTO).toList();
    }

    public ExerciseResponseDTO updateExerciseWithSessions(Integer id, ExerciseRequestDTO request) {

        ExerciseEntity exercise = exerciseService.updateExerciseEntityById(id, request);

        exerciseSessionService.updateSessionsForExercise(exercise);

        return exerciseMapper.toDTO(exercise);
    }
}
