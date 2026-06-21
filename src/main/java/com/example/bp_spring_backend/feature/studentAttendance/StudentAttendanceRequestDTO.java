package com.example.bp_spring_backend.feature.studentAttendance;

import com.example.bp_spring_backend.feature.enums.AttendanceEnum;
import com.example.bp_spring_backend.core.validation.OnCreate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceRequestDTO {

    @NotNull(groups = OnCreate.class, message = "studentId is required")
    private Integer studentId;
    @NotNull(groups = OnCreate.class, message = "exerciseSessionId is required")
    private Integer exerciseSessionId;
    @NotNull(groups = OnCreate.class, message = "attendanceEnum is required")
    private AttendanceEnum attendanceEnum;
}
