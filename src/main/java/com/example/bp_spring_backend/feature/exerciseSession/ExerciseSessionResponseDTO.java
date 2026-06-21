package com.example.bp_spring_backend.feature.exerciseSession;

import com.example.bp_spring_backend.feature.exercise.ExerciseResponseDTO;
import com.example.bp_spring_backend.feature.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSessionResponseDTO {

    private Integer id;
    private ExerciseResponseDTO exercise;
    private LocalDate sessionDate;
    private UserResponseDTO createdBy;
    private LocalDateTime createdAt;
    private UserResponseDTO updatedBy;
    private LocalDateTime updatedAt;
}
