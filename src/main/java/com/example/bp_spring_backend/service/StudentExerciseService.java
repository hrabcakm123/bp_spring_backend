package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.ExerciseNotFoundException;
import com.example.bp_spring_backend.exception.StudentExerciseNotFoundException;
import com.example.bp_spring_backend.exception.StudentNotFoundException;
import com.example.bp_spring_backend.mapper.StudentExerciseMapper;
import com.example.bp_spring_backend.repository.StudentExerciseRepository;
import com.example.bp_spring_backend.specification.StudentExerciseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentExerciseService {

    private final StudentExerciseRepository studentExerciseRepository;
    private final StudentExerciseMapper studentExerciseMapper;
    private final StudentService studentService;
    private final ExerciseService exerciseService;

    public List<StudentExerciseResponseDTO> getStudentExercisesByCriteria(
            Integer id,
            String fullName,
            String aisId,
            Integer exerciseId,
            Sort sort
    ) {
        Specification<StudentExerciseEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentExerciseSpecification.hasId(id));
        }
        if (fullName != null && !fullName.isBlank()) {
            spec = spec.and(StudentExerciseSpecification.containsStudentFullName(fullName));
        }
        if (aisId != null && !aisId.isBlank()) {
            spec = spec.and(StudentExerciseSpecification.containsStudentAisId(aisId));
        }
        if (exerciseId != null) {
            spec = spec.and(StudentExerciseSpecification.hasExerciseId(exerciseId));
        }

        return studentExerciseRepository.findAll(spec, sort).stream()
                .map(studentExerciseMapper::toDTO)
                .toList();
    }

    public List<StudentExerciseResponseDTO> addStudentExercises(List<StudentExerciseRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }

        List<Integer> studentIds = request.stream()
                .map(StudentExerciseRequestDTO::getStudentId)
                .distinct()
                .toList();

        List<Integer> exerciseIds = request.stream()
                .map(StudentExerciseRequestDTO::getExerciseId)
                .distinct()
                .toList();

        List<StudentEntity> students = studentService.getStudentEntitiesByIds(studentIds);
        Map<Integer, StudentEntity> studentMap = students.stream()
                .collect(Collectors.toMap(StudentEntity::getId, Function.identity()));

        List<ExerciseEntity> exercises = exerciseService.getExerciseEntitiesByIds(exerciseIds);
        Map<Integer, ExerciseEntity> exerciseMap = exercises.stream()
                .collect(Collectors.toMap(ExerciseEntity::getId, Function.identity()));

        List<StudentExerciseEntity> studentExercises = request.stream()
                .map(dto -> {
                    StudentEntity student = studentMap.get(dto.getStudentId());
                    ExerciseEntity exercise = exerciseMap.get(dto.getExerciseId());

                    if (student == null) {
                        throw new StudentNotFoundException("");
                    }
                    if (exercise == null) {
                        throw new ExerciseNotFoundException("");
                    }

                    return studentExerciseMapper.toEntity(student, exercise);
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

    public StudentExerciseEntity updateStudentExerciseEntityById(Integer id, StudentExerciseRequestDTO request) {
        StudentExerciseEntity studentExercise = studentExerciseRepository.findById(id)
                .orElseThrow(() -> new StudentExerciseNotFoundException(""));

        if (request.getStudentId() != null) {
            studentExercise.setStudentEntity(studentService.getStudentEntityById(request.getStudentId()));
        }
        if (request.getExerciseId() != null) {
            studentExercise.setExerciseEntity(exerciseService.getExerciseEntityById(request.getExerciseId()));
        }

        return studentExerciseRepository.save(studentExercise);
    }

    public void addStudentsToExercise(List<StudentEntity> students, Integer exerciseId) {

        List<StudentExerciseEntity> toSave = new ArrayList<>();
        ExerciseEntity exercise = exerciseService.getExerciseEntityById(exerciseId);

        for (StudentEntity student : students) {
            toSave.add(StudentExerciseEntity.builder()
                    .studentEntity(student)
                    .exerciseEntity(exercise)
                    .build());
        }
        studentExerciseRepository.saveAll(toSave);
    }

    @Transactional
    public void softDeleteStudentExercisesByStudentId(Integer studentId) {
        studentExerciseRepository.softDeleteByStudentId(studentId);
    }

    @Transactional
    public void softDeleteStudentExercisesByExerciseId(Integer exerciseId) {
        studentExerciseRepository.softDeleteByExerciseId(exerciseId);
    }
}
