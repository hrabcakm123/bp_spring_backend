package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.ExerciseSessionService;
import com.example.bp_spring_backend.validation.ExerciseSessionRequestDTOList;
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
@RequestMapping("/api/v1/exercise-session")
@RequiredArgsConstructor
public class ExerciseSessionController {

    private final ExerciseSessionService exerciseSessionService;

    @GetMapping
    public ResponseEntity<List<ExerciseSessionResponseDTO>> getExerciseSessionsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        Map<String, Object> searchCriteria  = new HashMap<>();
        if (id != null) {
            searchCriteria.put("id", id);
        }
        List<ExerciseSessionResponseDTO> exerciseSessions = exerciseSessionService.getExerciseSessionsByCriteria(searchCriteria, sort);
        return ResponseEntity.ok(exerciseSessions);
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<ExerciseSessionResponseDTO>>> addExerciseSessions(
            @Validated(OnCreate.class) @RequestBody ExerciseSessionRequestDTOList request
    ) {
        List<ExerciseSessionResponseDTO> addedExerciseSessions = exerciseSessionService.addExerciseSessions(request.getExerciseSessions());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<ExerciseSessionResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("ExerciseSessions created successfully.")
                        .data(addedExerciseSessions)
                        .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<ExerciseSessionResponseDTO>> updateExerciseSessionById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody ExerciseSessionRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<ExerciseSessionResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("ExerciseSession updated successfully.")
                        .data(exerciseSessionService.updateExerciseSessionById(id, request))
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<ExerciseSessionResponseDTO>> deleteExerciseSessionById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<ExerciseSessionResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("ExerciseSession deleted successfully.")
                        .data(exerciseSessionService.deleteExerciseSessionById(id))
                        .build());
    }
}
