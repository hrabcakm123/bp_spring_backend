package com.example.bp_spring_backend.feature.exercise;

import com.example.bp_spring_backend.feature.enums.RoomEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ExerciseLeaderProjection {

    Integer getId();
    LocalDate getFirstSessionDate();
    LocalTime getStartTime();
    RoomEnum getRoomEnum();
    String getLeaderFullName();
}
