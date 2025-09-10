package com.example.bp_spring_backend.validation;

import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class StudentRequestDTOList {

    @Valid
    private List<StudentRequestDTO> students;
}
