package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.domains.enums.RoomEnum;
import com.example.bp_spring_backend.validation.OnCreate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequestDTO {

    @NotNull(groups = OnCreate.class, message = "dayOfWeek is required")
    private DayOfWeek dayOfWeek;
    @NotNull(groups = OnCreate.class, message = "startTime is required")
    private LocalTime startTime;
    @NotNull(groups = OnCreate.class, message = "endTime is required")
    private LocalTime endTime;
    @NotNull(groups = OnCreate.class, message = "roomEnum is required")
    private RoomEnum roomEnum;
}
