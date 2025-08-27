package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.StudentExerciseNotFoundException;
import com.example.bp_spring_backend.mapper.StudentExerciseMapper;
import com.example.bp_spring_backend.repository.StudentExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentExerciseService {

    private final StudentExerciseRepository studentExerciseRepository;
    private final StudentExerciseMapper studentExerciseMapper;
    private final StudentService studentService;
    private final ExerciseService exerciseService;

    public List<StudentExerciseResponseDTO> getAllStudentExercises() {
        List<StudentExerciseEntity> studentExercises = studentExerciseRepository.findAll();
        return studentExercises.stream()
                .map(studentExerciseMapper::toDTO)
                .toList();
    }

    public StudentExerciseResponseDTO getStudentExerciseById(Integer id) {
        StudentExerciseEntity studentExercise = studentExerciseRepository.findById(id)
                .orElseThrow(() -> new StudentExerciseNotFoundException(""));
        return studentExerciseMapper.toDTO(studentExercise);
    }

    public List<StudentExerciseResponseDTO> addStudentExercises(List<StudentExerciseRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<StudentExerciseEntity> studentExercises = request.stream()
                .map(dto -> {
                    StudentEntity studentEntity = studentService.getStudentEntityById(dto.getStudentId());
                    ExerciseEntity exerciseEntity = exerciseService.getExerciseEntityById(dto.getExerciseId());
                    return studentExerciseMapper.toEntity(studentEntity, exerciseEntity);
                })
                .toList();

        List<StudentExerciseEntity> savedStudentExercises = studentExerciseRepository.saveAll(studentExercises);

        return savedStudentExercises.stream()
                .map(studentExerciseMapper::toDTO)
                .toList();
    }

    public StudentExerciseResponseDTO deleteStudentExerciseById(Integer id) {
        StudentExerciseEntity studentExercise = studentExerciseRepository.findById(id)
                .orElseThrow(() -> new StudentExerciseNotFoundException(""));
        studentExerciseRepository.deleteById(id);
        return studentExerciseMapper.toDTO(studentExercise);
    }

    public StudentExerciseResponseDTO updateStudentExerciseById(Integer id, StudentExerciseRequestDTO request) {
        StudentExerciseEntity studentExercise = studentExerciseRepository.findById(id)
                .orElseThrow(() -> new StudentExerciseNotFoundException(""));

        if (request.getStudentId() != null) {
            studentExercise.setStudentEntity(studentService.getStudentEntityById(request.getStudentId()));
        }
        if (request.getExerciseId() != null) {
            studentExercise.setExerciseEntity(exerciseService.getExerciseEntityById(request.getExerciseId()));
        }

        studentExercise = studentExerciseRepository.save(studentExercise);

        return studentExerciseMapper.toDTO(studentExercise);
    }
}
