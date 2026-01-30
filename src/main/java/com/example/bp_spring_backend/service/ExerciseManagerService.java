package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseManagerService {

    private final ExerciseService exerciseService;
    private final ExerciseSessionService exerciseSessionService;
    private final ExerciseMapper exerciseMapper;
    private final StudentExerciseService studentExerciseService;
    private final UserExerciseService userExerciseService;
    private final StudentAttendanceService studentAttendanceService;

    @Transactional
    public List<ExerciseResponseDTO> addExercisesWithSessions(List<ExerciseRequestDTO> request) {

        List<ExerciseEntity> exercises = exerciseService.addExerciseEntities(request);

        exerciseSessionService.addInitialSessionsForExercises(exercises);

        return exercises.stream().map(exerciseMapper::toDTO).toList();
    }

    @Transactional
    public ExerciseResponseDTO updateExerciseWithSessions(Integer id, ExerciseRequestDTO request) {

        ExerciseEntity exercise = exerciseService.updateExerciseEntityById(id, request);

        exerciseSessionService.updateSessionsForExercise(exercise);

        return exerciseMapper.toDTO(exercise);
    }

    // soft delete exercise, student_exercises, user_exercises, exercise_sessions, exercise_sessions -> student_attendances
    @Transactional
    public ExerciseResponseDTO softDeleteExerciseCascade(Integer exerciseId) {

        ExerciseResponseDTO response = exerciseService.deleteExerciseById(exerciseId);

        studentExerciseService.softDeleteStudentExercisesByExerciseId(exerciseId);
        userExerciseService.softDeleteUserExercisesByExerciseId(exerciseId);

        List<Integer> exerciseSessionIds = exerciseSessionService.softDeleteExerciseSessionsByExerciseId(exerciseId);

        if (!exerciseSessionIds.isEmpty()) {
            studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionIds(exerciseSessionIds);
        }

        return response;
    }
}
