package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import com.example.bp_spring_backend.domains.inputDTO.StudentToExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentManagerService;
import com.example.bp_spring_backend.service.StudentService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ResponseFactory responseFactory;
    private final StudentManagerService studentManagerService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "aisId", required = false) Integer aisId,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentService.getStudentsByCriteria(id, aisId, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<StudentResponseDTO>>> addStudents(
            @Validated(OnCreate.class) @RequestBody StudentToExerciseRequestDTO request
    ) {
        return responseFactory.created(
                "Students created successfully.",
                studentManagerService.addStudentsWithRelations(request)
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentResponseDTO>> updateStudentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentRequestDTO request
    ) {
        return responseFactory.ok(
                "Student updated successfully.",
                studentService.updateStudentById(id, request)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentResponseDTO>> deleteStudentById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "Student deleted successfully.",
                studentManagerService.softDeleteStudentCascade(id)
        );
    }
}
