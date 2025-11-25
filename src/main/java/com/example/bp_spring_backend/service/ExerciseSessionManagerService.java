package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseSessionManagerService {

    private final ExerciseSessionService exerciseSessionService;
    private final StudentAttendanceService studentAttendanceService;

    public ExerciseSessionResponseDTO softDeleteExerciseSessionCascade(Integer exerciseSessionId) {

        ExerciseSessionResponseDTO response = exerciseSessionService.deleteExerciseSessionById(exerciseSessionId);

        studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionId(exerciseSessionId);

        return response;
    }
}
