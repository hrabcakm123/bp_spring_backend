package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentExerciseMapper {

    private final StudentMapper studentMapper;
    private final ExerciseMapper exerciseMapper;

    public StudentExerciseResponseDTO toDTO(StudentExerciseEntity studentExerciseEntity) {
        return StudentExerciseResponseDTO.builder()
                .id(studentExerciseEntity.getId())
                .student(studentMapper.toDTO(studentExerciseEntity.getStudentEntity()))
                .exercise(exerciseMapper.toDTO(studentExerciseEntity.getExerciseEntity()))
                .build();
    }

    public StudentExerciseEntity toEntity(StudentEntity studentEntity, ExerciseEntity exerciseEntity) {
        return StudentExerciseEntity.builder()
                .studentEntity(studentEntity)
                .exerciseEntity(exerciseEntity)
                .build();
    }
}
