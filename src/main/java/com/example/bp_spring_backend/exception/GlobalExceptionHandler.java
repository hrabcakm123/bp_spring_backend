package com.example.bp_spring_backend.exception;

import com.example.bp_spring_backend.domains.outputDTO.ErrorResponseDTO;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseFactory responseFactory;

    // login email or password is wrong
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return responseFactory.unauthorized(
                "Bad credentials."
        );
    }

    // enum validation fails
    // inputDTO JSON body is incorrect (bad json format) or missing
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        // enum validation fails
        if (ex.getCause() instanceof InvalidFormatException ifex) {
            if (ifex.getTargetType().isEnum()) {
                return responseFactory.unprocessableEntity(
                        "Invalid value for Enum."
                );
            }
        }

        // inputDTO JSON body is incorrect (bad json format) or missing
        return responseFactory.badRequest(
                "Required request body is missing - Bad JSON format."
        );
    }

    // inputDTO validation fail
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return responseFactory.unprocessableEntity(
                "Validation failed.",
                errors
        );
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomValidationException(CustomValidationException ex) {
        return responseFactory.unprocessableEntity(
                ex.getMessage()
        );
    }

    // database unique validation fails or similar database validation fails
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return responseFactory.conflict(
                "The request could not be processed due to data integrity rules violation."
        );
    }

    // incorrect HTTP method used (e.g.: GET instead of POST ...)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return responseFactory.methodNotAllowed(
                "Incorrect HTTP method used."
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        return responseFactory.notFound(
                "User not found."
        );
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentNotFoundException(StudentNotFoundException ex) {
        return responseFactory.notFound(
                "Student not found."
        );
    }

    @ExceptionHandler(BlockNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBlockNotFoundException(BlockNotFoundException ex) {
        return responseFactory.notFound(
                "Block not found."
        );
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExerciseNotFoundException(ExerciseNotFoundException ex) {
        return responseFactory.notFound(
                "Exercise not found."
        );
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAssignmentNotFoundException(AssignmentNotFoundException ex) {
        return responseFactory.notFound(
                "Assignment not found."
        );
    }

    @ExceptionHandler(UserExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserExerciseNotFoundException(UserExerciseNotFoundException ex) {
        return responseFactory.notFound(
                "UserExercise not found."
        );
    }

    @ExceptionHandler(StudentExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentExerciseNotFoundException(StudentExerciseNotFoundException ex) {
        return responseFactory.notFound(
                "StudentExercise not found."
        );
    }

    @ExceptionHandler(ExerciseSessionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExerciseSessionNotFoundException(ExerciseSessionNotFoundException ex) {
        return responseFactory.notFound(
                "ExerciseSession not found."
        );
    }

    @ExceptionHandler(StudentAssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentAssignmentNotFoundException(StudentAssignmentNotFoundException ex) {
        return responseFactory.notFound(
                "StudentAssignment not found."
        );
    }

    // url or endpoint does not exist
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFound(NoResourceFoundException ex) {
        return responseFactory.notFound(
                "Resource not found."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        return responseFactory.internalServerError(
                ex.getMessage()
        );
    }
}
