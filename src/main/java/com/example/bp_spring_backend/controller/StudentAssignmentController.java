package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentAssignmentService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-assignments")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;

    @GetMapping("/get")
    public ResponseEntity<List<StudentAssignmentResponseDTO>> getAllStudentAssignments() {
        List<StudentAssignmentResponseDTO> studentAssignments = studentAssignmentService.getAllStudentAssignments();
        return ResponseEntity.ok(studentAssignments);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<StudentAssignmentResponseDTO> getStudentAssignmentById(@PathVariable Integer id) {
        StudentAssignmentResponseDTO studentAssignment = studentAssignmentService.getStudentAssignmentById(id);
        return ResponseEntity.ok(studentAssignment);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> addStudentAssignment(
            @Validated(OnCreate.class) @RequestBody StudentAssignmentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<StudentAssignmentResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("StudentAssignment created successfully.")
                        .data(studentAssignmentService.addStudentAssignment(request))
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> updateStudentAssignmentById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody StudentAssignmentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentAssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentAssignment updated successfully.")
                        .data(studentAssignmentService.updateStudentAssignmentById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAssignmentResponseDTO>> deleteStudentAssignmentById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentAssignmentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentAssignment deleted successfully.")
                        .data(studentAssignmentService.deleteStudentAssignmentById(id))
                        .build());
    }
}
