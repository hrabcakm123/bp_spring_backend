package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.UserExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserExerciseResponseDTO;
import com.example.bp_spring_backend.service.UserExerciseService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.UserExerciseRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
            Sort sort
    ) {
        return ResponseEntity.ok(
                userExerciseService.getUserExercisesByCriteria(id, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<UserExerciseResponseDTO>>> addUserExercises(
            @Validated(OnCreate.class) @RequestBody UserExerciseRequestDTOList request
    ) {
        return responseFactory.created(
                "UserExercises created successfully.",
                userExerciseService.addUserExercises(request.getUserExercises())
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<UserExerciseResponseDTO>> updateUserExerciseById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody UserExerciseRequestDTO request
    ) {
        return responseFactory.ok(
                "UserExercise updated successfully.",
                userExerciseService.updateUserExerciseById(id, request)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<UserExerciseResponseDTO>> deleteUserExerciseById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "UserExercise deleted successfully.",
                userExerciseService.deleteUserExerciseById(id)
        );
    }
}
