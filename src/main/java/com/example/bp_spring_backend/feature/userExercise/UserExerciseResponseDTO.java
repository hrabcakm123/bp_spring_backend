package com.example.bp_spring_backend.feature.userExercise;

import com.example.bp_spring_backend.feature.exercise.ExerciseResponseDTO;
import com.example.bp_spring_backend.feature.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExerciseResponseDTO {

    private Integer id;
    private UserResponseDTO user;
    private ExerciseResponseDTO exercise;
}
