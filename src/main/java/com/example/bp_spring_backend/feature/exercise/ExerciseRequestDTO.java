package com.example.bp_spring_backend.feature.exercise;

import com.example.bp_spring_backend.feature.enums.RoomEnum;
import com.example.bp_spring_backend.core.validation.OnCreate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequestDTO {

    @NotNull(groups = OnCreate.class, message = "firstSessionDate is required")
    private LocalDate firstSessionDate;
    @NotNull(groups = OnCreate.class, message = "startTime is required")
    private LocalTime startTime;
    @NotNull(groups = OnCreate.class, message = "roomEnum is required")
    private RoomEnum roomEnum;
}
