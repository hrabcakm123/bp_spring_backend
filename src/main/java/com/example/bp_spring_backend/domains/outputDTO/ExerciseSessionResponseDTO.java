package com.example.bp_spring_backend.domains.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSessionResponseDTO {

    private Integer id;
    private ExerciseResponseDTO exercise;
    private LocalDate sessionDate;
}
