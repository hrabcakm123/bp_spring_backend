package com.example.bp_spring_backend.feature.studentExercise;

import com.example.bp_spring_backend.feature.enums.RoomEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseForStudentResponseDTO {

    private String sessionDay;
    private String startTime;
    private RoomEnum roomEnum;
}
