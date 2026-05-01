package com.example.bp_spring_backend.feature.exerciseSession;

import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.core.dto.SuccessResponseDTO;
import com.example.bp_spring_backend.core.utils.ResponseFactory;
import com.example.bp_spring_backend.core.validation.OnCreate;
import com.example.bp_spring_backend.core.validation.OnUpdate;
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
