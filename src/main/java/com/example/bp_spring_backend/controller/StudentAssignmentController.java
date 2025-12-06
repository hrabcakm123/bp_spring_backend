package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentGroupedItemsResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentAssignmentManagerService;
import com.example.bp_spring_backend.service.StudentAssignmentService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentAssignmentRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-assignment")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;
    private final ResponseFactory responseFactory;
    private final StudentAssignmentManagerService studentAssignmentManagerService;

    @GetMapping
    public ResponseEntity<List<StudentAssignmentResponseDTO>> getStudentAssignmentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAssignmentService.getStudentAssignmentsByCriteria(id, sort)
        );
    }

    @GetMapping("/grading")
    public ResponseEntity<List<StudentAssignmentGroupedItemsResponseDTO>> getStudentAssignmentGroupedItems(
            @RequestParam(name = "blockId") Integer blockId,
            @RequestParam(name = "studentId", required = false) Integer studentId,
            @RequestParam(name = "exerciseId", required = false) Integer exerciseId,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAssignmentService.getStudentAssignmentGroupedItems(blockId, exerciseId, studentId, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<StudentAssignmentResponseDTO>>> addStudentAssignments(
            @Validated(OnCreate.class) @RequestBody StudentAssignmentRequestDTOList request
    ) {
        return responseFactory.created(
                "StudentAssignments created successfully.",
                studentAssignmentService.addStudentAssignments(request.getStudentAssignments())
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> updateStudentAssignmentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentAssignmentRequestDTO request
    ) {
        return responseFactory.ok(
                "StudentAssignment updated successfully.",
                studentAssignmentService.updateStudentAssignmentById(id, request)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> deleteStudentAssignmentById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "StudentAssignment deleted successfully.",
                studentAssignmentManagerService.softDeleteStudentAssignmentCascade(id)
        );
    }
}
