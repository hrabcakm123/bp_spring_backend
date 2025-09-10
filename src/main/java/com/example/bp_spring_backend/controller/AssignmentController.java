package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.AssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.AssignmentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.AssignmentService;
import com.example.bp_spring_backend.validation.AssignmentRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        Map<String, Object> searchCriteria  = new HashMap<>();
        if (id != null) {
            searchCriteria.put("id", id);
        }
        List<AssignmentResponseDTO> assignments = assignmentService.getAssignmentsByCriteria(searchCriteria, sort);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping
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

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<AssignmentResponseDTO>> updateAssignmentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody AssignmentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<AssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Assignment updated successfully.")
                        .data(assignmentService.updateAssignmentById(id, request))
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<AssignmentResponseDTO>> deleteAssignmentById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<AssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Assignment deleted successfully.")
                        .data(assignmentService.deleteAssignmentById(id))
                        .build());
    }
}
