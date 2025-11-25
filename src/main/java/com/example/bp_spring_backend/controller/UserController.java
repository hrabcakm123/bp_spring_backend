package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import com.example.bp_spring_backend.service.UserManagerService;
import com.example.bp_spring_backend.service.UserService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.UserRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;
    private final UserManagerService userManagerService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsersByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "email", required = false) String email,
            Sort sort
    ) {
        return ResponseEntity.ok(
                userService.getUsersByCriteria(id, email, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<UserResponseDTO>>> addUsers(
            @Validated(OnCreate.class) @RequestBody UserRequestDTOList request
    ) {
        return responseFactory.created(
                "Users created successfully.",
                userService.addUsers(request.getUsers())
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> updateUserById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody UserRequestDTO request
    ) {
        return responseFactory.ok(
                "User updated successfully.",
                userService.updateUserById(id, request)
        );
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> updateUsersPasswordById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "Users password updated successfully.",
                userService.updateUsersPasswordById(id)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> deleteUserById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "User deleted successfully.",
                userManagerService.softDeleteUserCascade(id)
        );
    }
}
