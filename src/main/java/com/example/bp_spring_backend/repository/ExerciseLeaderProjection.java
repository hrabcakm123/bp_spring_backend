package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.enums.RoomEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ExerciseLeaderProjection {

    Integer getId();
    LocalDate getFirstSessionDate();
    LocalTime getStartTime();
    RoomEnum getRoomEnum();
    String getLeaderFullName();
}
