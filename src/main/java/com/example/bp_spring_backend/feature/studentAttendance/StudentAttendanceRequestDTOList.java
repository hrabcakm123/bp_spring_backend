package com.example.bp_spring_backend.feature.studentAttendance;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentAttendanceRequestDTOList {

    @Valid
    private List<StudentAttendanceRequestDTO> studentAttendances;
}
