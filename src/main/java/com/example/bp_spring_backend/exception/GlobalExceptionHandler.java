package com.example.bp_spring_backend.exception;

import com.example.bp_spring_backend.domains.outputDTO.ErrorResponseDTO;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // login email or password is wrong
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials");
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
                log.error("Invalid value for Enum.");
                return responseFactory.unprocessableEntity(
                        "Invalid value for Enum."
                );
            }
        }

        // inputDTO JSON body is incorrect (bad json format) or missing
        log.error("Required request body is missing - Bad JSON format.");
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

        log.error("inputDTO validation failed");
        return responseFactory.unprocessableEntity(
                "Validation failed.",
                errors
        );
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomValidationException(CustomValidationException ex) {
        log.error(ex.getMessage());
        return responseFactory.unprocessableEntity(
                ex.getMessage()
        );
    }

    // database unique validation fails or similar database validation fails
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("The request could not be processed due to data integrity rules violation");
        return responseFactory.conflict(
                "The request could not be processed due to data integrity rules violation."
        );
    }

    // incorrect HTTP method used (e.g.: GET instead of POST ...)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("incorrect HTTP method used");
        return responseFactory.methodNotAllowed(
                "Incorrect HTTP method used."
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found");
        return responseFactory.notFound(
                "User not found."
        );
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentNotFoundException(StudentNotFoundException ex) {
        log.error("Student not found");
        return responseFactory.notFound(
                "Student not found."
        );
    }

    @ExceptionHandler(BlockNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBlockNotFoundException(BlockNotFoundException ex) {
        log.error("Block not found");
        return responseFactory.notFound(
                "Block not found."
        );
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExerciseNotFoundException(ExerciseNotFoundException ex) {
        log.error("Exercise not found");
        return responseFactory.notFound(
                "Exercise not found."
        );
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAssignmentNotFoundException(AssignmentNotFoundException ex) {
        log.error("Assignment not found");
        return responseFactory.notFound(
                "Assignment not found."
        );
    }

    @ExceptionHandler(UserExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserExerciseNotFoundException(UserExerciseNotFoundException ex) {
        log.error("UserExercise not found");
        return responseFactory.notFound(
                "UserExercise not found."
        );
    }

    @ExceptionHandler(StudentExerciseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentExerciseNotFoundException(StudentExerciseNotFoundException ex) {
        log.error("Student Exercise not found");
        return responseFactory.notFound(
                "StudentExercise not found."
        );
    }

    @ExceptionHandler(ExerciseSessionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExerciseSessionNotFoundException(ExerciseSessionNotFoundException ex) {
        log.error("ExerciseSession not found");
        return responseFactory.notFound(
                "ExerciseSession not found."
        );
    }

    @ExceptionHandler(StudentAssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentAssignmentNotFoundException(StudentAssignmentNotFoundException ex) {
        log.error("Student Assignment not found");
        return responseFactory.notFound(
                "StudentAssignment not found."
        );
    }

    @ExceptionHandler(StudentAttendanceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentAttendanceNotFoundException(StudentAttendanceNotFoundException ex) {
        log.error("Student Attendance not found");
        return responseFactory.notFound(
                "StudentAttendance not found."
        );
    }

    @ExceptionHandler(StudentAssignmentLogNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStudentAssignmentLogNotFoundException(StudentAssignmentLogNotFoundException ex) {
        log.error("Student Assignment Log not found");
        return responseFactory.notFound(
                "StudentAssignmentLog not found."
        );
    }

    // url or endpoint does not exist
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFound(NoResourceFoundException ex) {
        log.error("Resource not found");
        return responseFactory.notFound(
                "Resource not found."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        log.error("Internal server error - fallback for any unhandled exception");
        return responseFactory.internalServerError(
                //ex.getMessage()
                "Internal server error."
        );
    }
}
