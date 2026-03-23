package com.example.bp_spring_backend.domains.outputDTO;

import com.example.bp_spring_backend.domains.enums.RoomEnum;
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
public class ExerciseSubstitutionResponseDTO {

    private Integer id;
    private String sessionDay;
    private LocalTime startTime;
    private RoomEnum roomEnum;
    private String leaderFullName;
}
