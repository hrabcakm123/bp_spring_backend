package com.example.bp_spring_backend.feature.exerciseSession;

import com.example.bp_spring_backend.feature.studentAttendance.StudentAttendanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExerciseSessionManagerService {

    private final ExerciseSessionService exerciseSessionService;
    private final StudentAttendanceService studentAttendanceService;
    private static final Logger log = LoggerFactory.getLogger(ExerciseSessionManagerService.class);

    @Transactional
    public void softDeleteExerciseSessionCascade(Integer exerciseSessionId) {
        log.info("Starting soft delete cascade for exercise session id {}", exerciseSessionId);
        exerciseSessionService.deleteExerciseSessionById(exerciseSessionId);
        log.info("Soft deleted exercise session entity");
        studentAttendanceService.softDeleteStudentAttendancesByExerciseSessionId(exerciseSessionId);
        log.info("Soft deleted student attendances for exercise session id {}", exerciseSessionId);
        log.info("Completed soft delete cascade for exercise session id {}", exerciseSessionId);
    }
}
