package com.example.bp_spring_backend.feature.studentExercise;

import com.example.bp_spring_backend.feature.exercise.ExerciseEntity;
import com.example.bp_spring_backend.feature.exercise.ExerciseMapper;
import com.example.bp_spring_backend.feature.student.StudentEntity;
import com.example.bp_spring_backend.feature.student.StudentMapper;
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
