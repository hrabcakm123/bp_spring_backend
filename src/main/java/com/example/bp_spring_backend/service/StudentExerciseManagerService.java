package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.mapper.StudentExerciseMapper;
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
    private final StudentExerciseMapper studentExerciseMapper;
    private static final Logger log = LoggerFactory.getLogger(StudentExerciseManagerService.class);

    @Transactional
    public StudentExerciseResponseDTO updateStudentExerciseWithStudentAttendances(Integer id, StudentExerciseRequestDTO request) {
        log.info("Updating student exercise id {} with student attendances", id);
        StudentExerciseEntity studentExercise = studentExerciseService.updateStudentExerciseEntityById(id, request);
        log.info("Updated student exercise entity id {}", studentExercise.getId());
        studentAttendanceService.updateAttendancesForStudent(request.getStudentId(), request.getExerciseId());
        log.info("Updated student attendances for student id {} and exercise id {}", request.getStudentId(), request.getExerciseId());
        return studentExerciseMapper.toDTO(studentExercise);
    }
}
