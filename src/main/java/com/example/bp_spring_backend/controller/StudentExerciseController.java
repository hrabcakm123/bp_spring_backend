package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentExerciseService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentExerciseRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-exercises")
@RequiredArgsConstructor
public class StudentExerciseController {

    private final StudentExerciseService studentExerciseService;

    @GetMapping("/get")
    public ResponseEntity<List<StudentExerciseResponseDTO>> getAllStudentExercises() {
        List<StudentExerciseResponseDTO> studentExercises = studentExerciseService.getAllStudentExercises();
        return ResponseEntity.ok(studentExercises);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<StudentExerciseResponseDTO> getStudentExerciseById(@PathVariable Integer id) {
        StudentExerciseResponseDTO studentExercise = studentExerciseService.getStudentExerciseById(id);
        return ResponseEntity.ok(studentExercise);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<List<StudentExerciseResponseDTO>>> addStudentExercises(
            @Validated(OnCreate.class) @RequestBody StudentExerciseRequestDTOList request
    ) {
        List<StudentExerciseResponseDTO> addedStudentExercises = studentExerciseService.addStudentExercises(request.getStudentExercises());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<StudentExerciseResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("StudentExercises created successfully.")
                        .data(addedStudentExercises)
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<StudentExerciseResponseDTO>> updateStudentExerciseById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody StudentExerciseRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentExercise updated successfully.")
                        .data(studentExerciseService.updateStudentExerciseById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<StudentExerciseResponseDTO>> deleteStudentExerciseById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentExercise deleted successfully.")
                        .data(studentExerciseService.deleteStudentExerciseById(id))
                        .build());
    }
}
