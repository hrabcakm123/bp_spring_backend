package com.example.bp_spring_backend.utils;

import com.example.bp_spring_backend.domains.outputDTO.ErrorResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResponseFactory {

    private <T> ResponseEntity<SuccessResponseDTO<T>> success(
            HttpStatus status,
            String message,
            T data
    ) {
        return ResponseEntity.status(status)
                .body(SuccessResponseDTO.<T>builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build());
    }

    public <T> ResponseEntity<SuccessResponseDTO<T>> ok(String message, T data) {
        return success(HttpStatus.OK, message, data);
    }

    public ResponseEntity<SuccessResponseDTO<Void>> ok(String message) {
        return success(HttpStatus.OK, message, null);
    }

    public <T> ResponseEntity<SuccessResponseDTO<T>> created(String message, T data) {
        return success(HttpStatus.CREATED, message, data);
    }

    public ResponseEntity<SuccessResponseDTO<Void>> created(String message) {
        return success(HttpStatus.CREATED, message, null);
    }

    private ResponseEntity<ErrorResponseDTO> error(HttpStatus status, String message, List<String> errors) {
        return ResponseEntity.status(status)
                .body(ErrorResponseDTO.builder()
                        .status(status.value())
                        .message(message)
                        .errors(errors)
                        .build());
    }

    public ResponseEntity<ErrorResponseDTO> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message, null);
    }

    public ResponseEntity<ErrorResponseDTO> unprocessableEntity(String message) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, message, null);
    }

    public ResponseEntity<ErrorResponseDTO> unprocessableEntity(String message, List<String> errors) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, message, errors);
    }

    public ResponseEntity<ErrorResponseDTO> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message, null);
    }

    public ResponseEntity<ErrorResponseDTO> conflict(String message) {
        return error(HttpStatus.CONFLICT, message, null);
    }

    public ResponseEntity<ErrorResponseDTO> methodNotAllowed(String message) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, message, null);
    }

    public ResponseEntity<ErrorResponseDTO> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message, null);
    }

    public ResponseEntity<ErrorResponseDTO> internalServerError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
}
