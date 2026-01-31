package com.example.bp_spring_backend.domains.outputDTO;

import com.example.bp_spring_backend.domains.enums.AttendanceEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
