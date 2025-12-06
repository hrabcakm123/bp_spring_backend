package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentExerciseRequestDTOList {

    @Valid
    private List<StudentExerciseRequestDTO> studentExercises;
}
