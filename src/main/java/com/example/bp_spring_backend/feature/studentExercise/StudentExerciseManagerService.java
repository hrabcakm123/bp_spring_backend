package com.example.bp_spring_backend.feature.studentExercise;

import com.example.bp_spring_backend.feature.studentAttendance.StudentAttendanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentExerciseManagerService {

    private final StudentExerciseService studentExerciseService;
    private final StudentAttendanceService studentAttendanceService;
    private static final Logger log = LoggerFactory.getLogger(StudentExerciseManagerService.class);

    @Transactional
    public void updateStudentExerciseWithStudentAttendances(Integer id, StudentExerciseRequestDTO request) {
        log.info("Updating student exercise id {} with student attendances", id);
        studentExerciseService.updateStudentExerciseEntityById(id, request);
        log.info("Updated student exercise entity");
        studentAttendanceService.updateAttendancesForStudent(request.getStudentId(), request.getExerciseId());
        log.info("Updated student attendances for student id {} and exercise id {}", request.getStudentId(), request.getExerciseId());
    }
}
