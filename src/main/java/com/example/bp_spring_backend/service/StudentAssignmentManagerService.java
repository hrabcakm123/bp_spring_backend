package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentAssignmentManagerService {

    private final StudentAssignmentService studentAssignmentService;
    private final StudentAssignmentLogService studentAssignmentLogService;

    public StudentAssignmentResponseDTO softDeleteStudentAssignmentCascade(Integer studentAssignmentId) {

        StudentAssignmentResponseDTO response = studentAssignmentService.deleteStudentAssignmentById(studentAssignmentId);

        studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentId(studentAssignmentId);

        return response;
    }
}
