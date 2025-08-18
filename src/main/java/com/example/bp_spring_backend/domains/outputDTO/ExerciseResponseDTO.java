package com.example.bp_spring_backend.domains.outputDTO;

import com.example.bp_spring_backend.domains.enums.RoomEnum;
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
public class ExerciseResponseDTO {

    private Integer id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private RoomEnum roomEnum;
}
