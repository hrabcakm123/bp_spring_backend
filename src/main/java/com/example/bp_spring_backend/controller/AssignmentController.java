package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.AssignmentService;
import com.example.bp_spring_backend.validation.AssignmentRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/get")
    public ResponseEntity<List<AssignmentResponseDTO>> getAllAssignments() {
        List<AssignmentResponseDTO> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(@PathVariable Integer id) {
        AssignmentResponseDTO assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<List<AssignmentResponseDTO>>> addAssignments(
            @Validated(OnCreate.class) @RequestBody AssignmentRequestDTOList request
    ) {
        List<AssignmentResponseDTO> addedAssignments = assignmentService.addAssignments(request.getAssignments());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<AssignmentResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Assignments created successfully.")
                        .data(addedAssignments)
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<AssignmentResponseDTO>> updateAssignmentById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody AssignmentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<AssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Assignment updated successfully.")
                        .data(assignmentService.updateAssignmentById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<AssignmentResponseDTO>> deleteAssignmentById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<AssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Assignment deleted successfully.")
                        .data(assignmentService.deleteAssignmentById(id))
                        .build());
    }
}
