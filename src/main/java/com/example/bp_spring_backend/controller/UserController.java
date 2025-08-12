package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import com.example.bp_spring_backend.service.UserService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.UserRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/post")
    public ResponseEntity<SuccessResponseDTO<List<UserResponseDTO>>> addUsers(
            @Validated(OnCreate.class) @RequestBody UserRequestDTOList request
    ) {
        List<UserResponseDTO> addedUsers = userService.addUsers(request.getUsers());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<UserResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Users created successfully.")
                        .data(addedUsers)
                        .build());
    }

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> updateUserById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody UserRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<UserResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("User updated successfully.")
                        .data(userService.updateUserById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> deleteUserById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<UserResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("User deleted successfully.")
                        .data(userService.deleteUserById(id))
                        .build());
    }
}
