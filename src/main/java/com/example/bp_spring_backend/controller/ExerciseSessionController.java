package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.ExerciseSessionService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise-sessions")
@RequiredArgsConstructor
public class ExerciseSessionController {

    private final ExerciseSessionService exerciseSessionService;

    @GetMapping("/get")
    public ResponseEntity<List<ExerciseSessionResponseDTO>> getAllExerciseSessions() {
        List<ExerciseSessionResponseDTO> exerciseSessions = exerciseSessionService.getAllExerciseSessions();
        return ResponseEntity.ok(exerciseSessions);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ExerciseSessionResponseDTO> getExerciseSessionById(@PathVariable Integer id) {
        ExerciseSessionResponseDTO exerciseSession = exerciseSessionService.getExerciseSessionById(id);
        return ResponseEntity.ok(exerciseSession);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<ExerciseSessionResponseDTO>> addExerciseSession(
            @Validated(OnCreate.class) @RequestBody ExerciseSessionRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<ExerciseSessionResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("ExerciseSession created successfully.")
                        .data(exerciseSessionService.addExerciseSession(request))
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<ExerciseSessionResponseDTO>> updateExerciseSessionById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody ExerciseSessionRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<ExerciseSessionResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("ExerciseSession updated successfully.")
                        .data(exerciseSessionService.updateExerciseSessionById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<ExerciseSessionResponseDTO>> deleteExerciseSessionById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<ExerciseSessionResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("ExerciseSession deleted successfully.")
                        .data(exerciseSessionService.deleteExerciseSessionById(id))
                        .build());
    }
}
