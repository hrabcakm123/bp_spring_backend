package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentAssignmentService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentAssignmentRequestDTOList;
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
@RequestMapping("/api/v1/student-assignment")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;

    @GetMapping
    public ResponseEntity<List<StudentAssignmentResponseDTO>> getStudentAssignmentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        Map<String, Object> searchCriteria  = new HashMap<>();
        if (id != null) {
            searchCriteria.put("id", id);
        }
        List<StudentAssignmentResponseDTO> studentAssignments = studentAssignmentService.getStudentAssignmentsByCriteria(searchCriteria, sort);
        return ResponseEntity.ok(studentAssignments);
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<StudentAssignmentResponseDTO>>> addStudentAssignments(
            @Validated(OnCreate.class) @RequestBody StudentAssignmentRequestDTOList request
    ) {
        List<StudentAssignmentResponseDTO> addedStudentAssignments = studentAssignmentService.addStudentAssignments(request.getStudentAssignments());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<StudentAssignmentResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("StudentAssignments created successfully.")
                        .data(addedStudentAssignments)
                        .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> updateStudentAssignmentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentAssignmentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentAssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentAssignment updated successfully.")
                        .data(studentAssignmentService.updateStudentAssignmentById(id, request))
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> deleteStudentAssignmentById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentAssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentAssignment deleted successfully.")
                        .data(studentAssignmentService.deleteStudentAssignmentById(id))
                        .build());
    }
}
