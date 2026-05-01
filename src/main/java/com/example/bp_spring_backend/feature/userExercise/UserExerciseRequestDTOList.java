package com.example.bp_spring_backend.feature.userExercise;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class UserExerciseRequestDTOList {

    @Valid
    private List<UserExerciseRequestDTO> userExercises;
}
