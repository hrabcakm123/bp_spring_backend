package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.inputDTO.UserPasswordRequestDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestParam(name = "fullName",  required = false) String fullName,
            Sort sort
    ) {
        return ResponseEntity.ok(
                userService.getUsersByCriteria(id, email, fullName, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addUsers(
            @Validated(OnCreate.class) @RequestBody UserRequestDTOList request
    ) {
        userService.addUsers(request.getUsers());
        return responseFactory.created(
                "Users created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateUserById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody UserRequestDTO request
    ) {
        userService.updateUserById(id, request);
        return responseFactory.ok(
                "User updated successfully."
        );
    }

    @PutMapping("/generate-password/{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateUsersPasswordById(
            @PathVariable Integer id
    ) {
        userService.updateUsersPasswordById(id);
        return responseFactory.ok(
                "Users password updated successfully."
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<SuccessResponseDTO<Void>> updateCurrentUsersPassword(
            @Validated @RequestBody UserPasswordRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        userService.updateCurrentUsersPassword(request, currentUser.getId());
        return responseFactory.ok(
                "Users password updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteUserById(
            @PathVariable Integer id
    ) {
        userManagerService.softDeleteUserCascade(id);
        return responseFactory.ok(
                "User deleted successfully."
        );
    }
}
