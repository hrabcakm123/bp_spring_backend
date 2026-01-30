package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentAssignmentManagerService {

    private final StudentAssignmentService studentAssignmentService;
    private final StudentAssignmentLogService studentAssignmentLogService;

    @Transactional
    public StudentAssignmentResponseDTO softDeleteStudentAssignmentCascade(Integer studentAssignmentId) {

        StudentAssignmentResponseDTO response = studentAssignmentService.deleteStudentAssignmentById(studentAssignmentId);

        studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentId(studentAssignmentId);

        return response;
    }
}
