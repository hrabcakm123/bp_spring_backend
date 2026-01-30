package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentExerciseEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.mapper.StudentExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentExerciseManagerService {

    private final StudentExerciseService studentExerciseService;
    private final StudentAttendanceService studentAttendanceService;
    private final StudentExerciseMapper studentExerciseMapper;

    @Transactional
    public StudentExerciseResponseDTO updateStudentExerciseWithStudentAttendances(Integer id, StudentExerciseRequestDTO request) {

        StudentExerciseEntity studentExercise = studentExerciseService.updateStudentExerciseEntityById(id, request);

        studentAttendanceService.updateAttendancesForStudent(request.getStudentId(), request.getExerciseId());

        return studentExerciseMapper.toDTO(studentExercise);
    }
}
