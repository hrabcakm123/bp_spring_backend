package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentAssignmentManagerService {

    private final StudentAssignmentService studentAssignmentService;
    private final StudentAssignmentLogService studentAssignmentLogService;
    private static final Logger log = LoggerFactory.getLogger(StudentAssignmentManagerService.class);

    @Transactional
    public StudentAssignmentResponseDTO softDeleteStudentAssignmentCascade(Integer studentAssignmentId) {
        log.info("Starting soft delete cascade for student assignment id {}", studentAssignmentId);
        StudentAssignmentResponseDTO response = studentAssignmentService.deleteStudentAssignmentById(studentAssignmentId);
        log.info("Soft deleted student assignment entity: {}", response);
        studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentId(studentAssignmentId);
        log.info("Soft deleted student assignment logs for student assignment id {}", studentAssignmentId);

        log.info("Completed soft delete cascade for student assignment id {}", studentAssignmentId);
        return response;
    }
}
