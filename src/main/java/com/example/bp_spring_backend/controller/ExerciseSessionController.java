package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.inputDTO.ExerciseSessionRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.ExerciseSessionResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.ExerciseSessionManagerService;
import com.example.bp_spring_backend.service.ExerciseSessionService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.ExerciseSessionRequestDTOList;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise-session")
@RequiredArgsConstructor
public class ExerciseSessionController {

    private final ExerciseSessionService exerciseSessionService;
    private final ResponseFactory responseFactory;
    private final ExerciseSessionManagerService exerciseSessionManagerService;

    @GetMapping
    public ResponseEntity<List<ExerciseSessionResponseDTO>> getExerciseSessionsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        return ResponseEntity.ok(
                exerciseSessionService.getExerciseSessionsByCriteria(id, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addExerciseSessions(
            @Validated(OnCreate.class) @RequestBody ExerciseSessionRequestDTOList request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        exerciseSessionService.addExerciseSessions(request.getExerciseSessions(), currentUser.getId());
        return responseFactory.created(
                "ExerciseSessions created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateExerciseSessionById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody ExerciseSessionRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        exerciseSessionService.updateExerciseSessionById(id, request, currentUser.getId());
        return responseFactory.ok(
                "ExerciseSession updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteExerciseSessionById(
            @PathVariable Integer id
    ) {
        exerciseSessionManagerService.softDeleteExerciseSessionCascade(id);
        return responseFactory.ok(
                "ExerciseSession deleted successfully."
        );
    }
}
