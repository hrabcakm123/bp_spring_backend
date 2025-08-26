package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.UserExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserExerciseResponseDTO;
import com.example.bp_spring_backend.service.UserExerciseService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.UserExerciseRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-exercises")
@RequiredArgsConstructor
public class UserExerciseController {

    private final UserExerciseService userExerciseService;

    @GetMapping("/get")
    public ResponseEntity<List<UserExerciseResponseDTO>> getAllUserExercises() {
        List<UserExerciseResponseDTO> userExercises = userExerciseService.getAllUserExercises();
        return ResponseEntity.ok(userExercises);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<UserExerciseResponseDTO> getUserExerciseById(@PathVariable Integer id) {
        UserExerciseResponseDTO userExercise = userExerciseService.getUserExerciseById(id);
        return ResponseEntity.ok(userExercise);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<List<UserExerciseResponseDTO>>> addUserExercises(
            @Validated(OnCreate.class) @RequestBody UserExerciseRequestDTOList request
    ) {
        List<UserExerciseResponseDTO> addedUserExercises = userExerciseService.addUserExercises(request.getUserExercises());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<UserExerciseResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("UserExercises created successfully.")
                        .data(addedUserExercises)
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<UserExerciseResponseDTO>> updateUserExerciseById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody UserExerciseRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<UserExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("UserExercise updated successfully.")
                        .data(userExerciseService.updateUserExerciseById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<UserExerciseResponseDTO>> deleteUserExerciseById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<UserExerciseResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("UserExercise deleted successfully.")
                        .data(userExerciseService.deleteUserExerciseById(id))
                        .build());
    }
}
