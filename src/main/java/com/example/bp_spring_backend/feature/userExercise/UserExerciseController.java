package com.example.bp_spring_backend.feature.userExercise;

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
@RequestMapping("/api/v1/user-exercise")
@RequiredArgsConstructor
public class UserExerciseController {

    private final UserExerciseService userExerciseService;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<List<UserExerciseResponseDTO>> getUserExercisesByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "userId", required = false) Integer userId,
            Sort sort
    ) {
        return ResponseEntity.ok(
                userExerciseService.getUserExercisesByCriteria(id, userId, sort)
        );
    }

    @GetMapping("/current")
    public ResponseEntity<List<ExerciseSummaryResponseDTO>> getCurrentUserExercises(
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        return ResponseEntity.ok(
                userExerciseService.getExercisesForCurrentUser(currentUser.getId())
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addUserExercises(
            @Validated(OnCreate.class) @RequestBody UserExerciseRequestDTOList request
    ) {
        userExerciseService.addUserExercises(request.getUserExercises());
        return responseFactory.created(
                "UserExercises created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateUserExerciseById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody UserExerciseRequestDTO request
    ) {
        userExerciseService.updateUserExerciseById(id, request);
        return responseFactory.ok(
                "UserExercise updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteUserExerciseById(
            @PathVariable Integer id
    ) {
        userExerciseService.deleteUserExerciseById(id);
        return responseFactory.ok(
                "UserExercise deleted successfully."
        );
    }
}
