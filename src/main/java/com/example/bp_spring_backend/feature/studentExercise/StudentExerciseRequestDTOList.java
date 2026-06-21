package com.example.bp_spring_backend.feature.studentExercise;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentExerciseRequestDTOList {

    @Valid
    private List<StudentExerciseRequestDTO> studentExercises;
}
