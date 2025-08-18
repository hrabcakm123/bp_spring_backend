package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.ExerciseService;
import com.example.bp_spring_backend.validation.ExerciseRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    
    private final ExerciseService exerciseService;

    @GetMapping("/get")
    public ResponseEntity<List<ExerciseResponseDTO>> getAllExercises() {
        List<ExerciseResponseDTO> exercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable Integer id) {
        ExerciseResponseDTO exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<List<ExerciseResponseDTO>>> addExercises(
            @Validated(OnCreate.class) @RequestBody ExerciseRequestDTOList request
    ) {
        List<ExerciseResponseDTO> addedExercises = exerciseService.addExercises(request.getExercises());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<ExerciseResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Exercises created successfully.")
                        .data(addedExercises)
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<ExerciseResponseDTO>> updateExerciseById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody ExerciseRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<ExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Exercise updated successfully.")
                        .data(exerciseService.updateExerciseById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<ExerciseResponseDTO>> deleteExerciseById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<ExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Exercise deleted successfully.")
                        .data(exerciseService.deleteExerciseById(id))
                        .build());
    }
}
