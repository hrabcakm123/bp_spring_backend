package com.example.bp_spring_backend.exception;

import com.example.bp_spring_backend.domains.outputDTO.ErrorResponseDTO;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // login email or password is wrong
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Bad credentials.")
                        .build());
    }

    // enum validation fails
    // inputDTO JSON body is incorrect (bad json format) or missing
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        // enum validation fails
        if (ex.getCause() instanceof InvalidFormatException ifex) {
            if (ifex.getTargetType().isEnum()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(ErrorResponseDTO.builder()
                                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                                .message("Invalid value for Enum.")
                                .build());
            }
        }

        // inputDTO JSON body is incorrect (bad json format) or missing
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Required request body is missing - Bad JSON format.")
                        .build());
    }

    // inputDTO validation fail
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .message("Validation failed.")
                        .errors(errors)
                        .build());
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomValidationException(CustomValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .message(ex.getMessage())
                        .build());
    }

    // database unique validation fails or similar database validation fails
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .message("The request could not be processed due to data integrity rules violation.")
                        .build());
    }

    // incorrect HTTP method used (e.g.: GET instead of POST ...)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .message("Incorrect HTTP method used.")
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("User not found.")
                        .build());
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentNotFoundException(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Student not found.")
                        .build());
    }

    @ExceptionHandler(BlockNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBlockNotFoundException(BlockNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Block not found.")
                        .build());
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExerciseNotFoundException(ExerciseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Exercise not found.")
                        .build());
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAssignmentNotFoundException(AssignmentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Assignment not found.")
                        .build());
    }

    // url or endpoint does not exist
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Resource not found.")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage()) // later change to Internal server error (to not leak anything) ... this is only for debugging purposes
                        .build());
    }
}
