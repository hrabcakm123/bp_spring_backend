package com.example.bp_spring_backend.feature.studentAttendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceItemResponseDTO {

    private Integer attendanceId;
    private String attendance;
}
