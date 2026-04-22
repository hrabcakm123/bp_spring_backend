package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseForStudentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

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

    public ExerciseForStudentResponseDTO toExerciseForStudentDTO(StudentExerciseEntity studentExerciseEntity) {
        return ExerciseForStudentResponseDTO.builder()
                .sessionDay(studentExerciseEntity.getExerciseEntity().getFirstSessionDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("sk", "SK")))
                .startTime(studentExerciseEntity.getExerciseEntity().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .roomEnum(studentExerciseEntity.getExerciseEntity().getRoomEnum())
                .build();
    }

    public StudentExerciseEntity toEntity(StudentEntity studentEntity, ExerciseEntity exerciseEntity) {
        return StudentExerciseEntity.builder()
                .studentEntity(studentEntity)
                .exerciseEntity(exerciseEntity)
                .build();
    }
}
