package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class StudentRequestDTOList {

    @NotEmpty(message = "Student list must not be empty")
    @Valid
    private List<StudentRequestDTO> students;
}
