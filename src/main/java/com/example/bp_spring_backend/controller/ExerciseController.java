package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.ExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.ExerciseManagerService;
import com.example.bp_spring_backend.service.ExerciseService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.ExerciseRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    
    private final ExerciseService exerciseService;
    private final ResponseFactory responseFactory;
    private final ExerciseManagerService exerciseManagerService;

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getExercisesByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        return ResponseEntity.ok(
                exerciseService.getExercisesByCriteria(id, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addExercises(
            @Validated(OnCreate.class) @RequestBody ExerciseRequestDTOList request
    ) {
        exerciseManagerService.addExercisesWithSessions(request.getExercises());
        return responseFactory.created(
                "Exercises created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateExerciseById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody ExerciseRequestDTO request
    ) {
        exerciseManagerService.updateExerciseWithSessions(id, request);
        return responseFactory.ok(
                "Exercise updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteExerciseById(
            @PathVariable Integer id
    ) {
        exerciseManagerService.softDeleteExerciseCascade(id);
        return responseFactory.ok(
                "Exercise deleted successfully."
        );
    }
}
