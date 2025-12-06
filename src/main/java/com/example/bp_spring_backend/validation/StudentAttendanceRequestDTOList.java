package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.StudentAttendanceRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentAttendanceRequestDTOList {

    @Valid
    private List<StudentAttendanceRequestDTO> studentAttendances;
}
