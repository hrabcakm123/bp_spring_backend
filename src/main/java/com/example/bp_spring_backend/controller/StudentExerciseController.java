package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentExerciseService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentExerciseRequestDTOList;
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
@RequestMapping("/api/v1/student-exercise")
@RequiredArgsConstructor
public class StudentExerciseController {

    private final StudentExerciseService studentExerciseService;

    @GetMapping
    public ResponseEntity<List<StudentExerciseResponseDTO>> getStudentExercisesByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        Map<String, Object> searchCriteria  = new HashMap<>();
        if (id != null) {
            searchCriteria.put("id", id);
        }
        List<StudentExerciseResponseDTO> studentExercises = studentExerciseService.getStudentExercisesByCriteria(searchCriteria, sort);
        return ResponseEntity.ok(studentExercises);
    }

    @PostMapping
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

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentExerciseResponseDTO>> updateStudentExerciseById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentExerciseRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentExercise updated successfully.")
                        .data(studentExerciseService.updateStudentExerciseById(id, request))
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentExerciseResponseDTO>> deleteStudentExerciseById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("StudentExercise deleted successfully.")
                        .data(studentExerciseService.deleteStudentExerciseById(id))
                        .build());
    }
}
