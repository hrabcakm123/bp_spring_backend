package com.example.bp_spring_backend.feature.studentAttendance;

import com.example.bp_spring_backend.feature.enums.AttendanceEnum;
import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.feature.student.StudentResponseDTO;
import com.example.bp_spring_backend.feature.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceResponseDTO {

    private Integer id;
    private StudentResponseDTO student;
    private ExerciseSessionResponseDTO exerciseSession;
    private AttendanceEnum attendanceEnum;
    private UserResponseDTO createdBy;
    private LocalDateTime createdAt;
    private UserResponseDTO updatedBy;
    private LocalDateTime updatedAt;
}
