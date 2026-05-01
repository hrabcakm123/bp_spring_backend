package com.example.bp_spring_backend.feature.studentExercise;

import com.example.bp_spring_backend.feature.exercise.ExerciseResponseDTO;
import com.example.bp_spring_backend.feature.student.StudentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentExerciseResponseDTO {

    private Integer id;
    private StudentResponseDTO student;
    private ExerciseResponseDTO exercise;
}
