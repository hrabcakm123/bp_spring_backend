package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentLogResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentAssignmentLogService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-assignment-log")
@RequiredArgsConstructor
public class StudentAssignmentLogController {

    private final StudentAssignmentLogService studentAssignmentLogService;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<List<StudentAssignmentLogResponseDTO>> getStudentAssignmentLogsByCriteria(
            @RequestParam(name = "originalUserFullName", required = false) String originalUserFullName,
            @RequestParam(name = "updatedByUserFullName", required = false) String updatedByUserFullName,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAssignmentLogService.getStudentAssignmentLogsByCriteria(
                        originalUserFullName,
                        updatedByUserFullName,
                        sort
                )
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteStudentAssignmentLogById(
            @PathVariable Integer id
    ) {
        studentAssignmentLogService.deleteStudentAssignmentLogById(id);
        return responseFactory.ok(
                "StudentAssignmentLog deleted successfully."
        );
    }
}
