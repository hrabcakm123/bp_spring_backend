package com.example.bp_spring_backend.feature.exercise;

import com.example.bp_spring_backend.feature.enums.RoomEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSubstitutionResponseDTO {

    private Integer id;
    private String sessionDay;
    private LocalTime startTime;
    private RoomEnum roomEnum;
    private String leaderFullName;
}
