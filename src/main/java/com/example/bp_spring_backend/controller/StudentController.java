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

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam(name = "q") String searchQuery,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentService.searchStudents(searchQuery, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addStudents(
            @Validated(OnCreate.class) @RequestBody StudentToExerciseRequestDTO request
    ) {
        studentManagerService.addStudentsWithRelations(request);
        return responseFactory.created(
                "Students created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateStudentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentRequestDTO request
    ) {
        studentService.updateStudentById(id, request);
        return responseFactory.ok(
                "Student updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteStudentById(
            @PathVariable Integer id
    ) {
        studentManagerService.softDeleteStudentCascade(id);
        return responseFactory.ok(
                "Student deleted successfully."
        );
    }
}
