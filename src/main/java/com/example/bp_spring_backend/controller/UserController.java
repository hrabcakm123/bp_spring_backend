package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.UserRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.UserResponseDTO;
import com.example.bp_spring_backend.service.UserService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.UserRequestDTOList;
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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsersByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "email", required = false) String email,
            Sort sort
    ) {
        Map<String, Object> searchCriteria  = new HashMap<>();
        if (id != null) {
            searchCriteria.put("id", id);
        }
        if (email != null) {
            searchCriteria.put("email", email);
        }
        List<UserResponseDTO> users = userService.getUsersByCriteria(searchCriteria, sort);
        return ResponseEntity.ok(users);
    }

    @PostMapping
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

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> updateUserById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody UserRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<UserResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("User updated successfully.")
                        .data(userService.updateUserById(id, request))
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<UserResponseDTO>> deleteUserById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<UserResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("User deleted successfully.")
                        .data(userService.deleteUserById(id))
                        .build());
    }
}
