package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import com.example.bp_spring_backend.service.StudentAssignmentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-assignment-log")
@RequiredArgsConstructor
public class StudentAssignmentLogController {

    private final StudentAssignmentLogService studentAssignmentLogService;

    @GetMapping
    public ResponseEntity<List<StudentAssignmentLogResponseDTO>> getStudentAssignmentLogs(
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAssignmentLogService.getStudentAssignmentLogs(sort)
        );
    }
}
