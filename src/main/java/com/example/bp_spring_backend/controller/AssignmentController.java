package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.AssignmentManagerService;
import com.example.bp_spring_backend.service.AssignmentService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.AssignmentRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final ResponseFactory responseFactory;
    private final AssignmentManagerService assignmentManagerService;

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "blockId", required = false) Integer blockId,
            Sort sort
    ) {
        return ResponseEntity.ok(
                assignmentService.getAssignmentsByCriteria(id, blockId, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addAssignments(
            @Validated(OnCreate.class) @RequestBody AssignmentRequestDTOList request
    ) {
        assignmentManagerService.addAssignmentsWithStudentAssignments(request.getAssignments());
        return responseFactory.created(
                "Assignments created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateAssignmentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody AssignmentRequestDTO request
    ) {
        assignmentService.updateAssignmentById(id, request);
        return responseFactory.ok(
                "Assignment updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteAssignmentById(
            @PathVariable Integer id
    ) {
        assignmentManagerService.softDeleteAssignmentCascade(id);
        return responseFactory.ok(
                "Assignment deleted successfully."
        );
    }
}
