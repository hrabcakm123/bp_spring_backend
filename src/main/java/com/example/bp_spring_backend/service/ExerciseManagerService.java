package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ExerciseManagerService.class);

    @Transactional
    public List<ExerciseResponseDTO> addExercisesWithSessions(List<ExerciseRequestDTO> request) {
        log.info("Adding {} exercises with initial sessions", request.size());
        List<ExerciseEntity> exercises = exerciseService.addExerciseEntities(request);
        log.info("Added {} exercises to DB", exercises.size());
        exerciseSessionService.addInitialSessionsForExercises(exercises);
        log.info("Added initial sessions for all exercises");
        return exercises.stream().map(exerciseMapper::toDTO).toList();
    }

    @Transactional
    public ExerciseResponseDTO updateExerciseWithSessions(Integer id, ExerciseRequestDTO request) {
        log.info("Updating exercise id {} with sessions", id);
        ExerciseEntity exercise = exerciseService.updateExerciseEntityById(id, request);
        log.info("Updated exercise entity id {}", exercise.getId());
        exerciseSessionService.updateSessionsForExercise(exercise);
        log.info("Updated sessions for exercise id {}", exercise.getId());
        return exerciseMapper.toDTO(exercise);
    }

    // soft delete exercise, student_exercises, user_exercises, exercise_sessions, exercise_sessions -> student_attendances
    @Transactional
    public ExerciseResponseDTO softDeleteExerciseCascade(Integer exerciseId) {
        log.info("Starting soft delete cascade for exercise id {}", exerciseId);
        ExerciseResponseDTO response = exerciseService.deleteExerciseById(exerciseId);
        log.info("Soft deleted exercise entity: {}", response);
        studentExerciseService.softDeleteStudentExercisesByExerciseId(exerciseId);
        log.info("Soft deleted student exercises for exercise id {}", exerciseId);
        userExerciseService.softDeleteUserExercisesByExerciseId(exerciseId);
        log.info("Soft deleted user exercises for exercise id {}", exerciseId);

        List<Integer> exerciseSessionIds = exerciseSessionService.softDeleteExerciseSessionsByExerciseId(exerciseId);
        log.info("Soft deleted {} exercise sessions for exercise id {}", exerciseSessionIds.size(), exerciseId);
        if (!exerciseSessionIds.isEmpty()) {
            studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionIds(exerciseSessionIds);
            log.info("Soft deleted student attendances for {} exercise sessions", exerciseSessionIds.size());
        } else {
            log.info("No exercise sessions found to soft delete student attendances for exercise id {}", exerciseId);
        }
        log.info("Completed soft delete cascade for exercise id {}", exerciseId);
        return response;
    }
}
