package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentExerciseRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentExerciseResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentExerciseManagerService;
import com.example.bp_spring_backend.service.StudentExerciseService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentExerciseRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-exercise")
@RequiredArgsConstructor
public class StudentExerciseController {

    private final StudentExerciseService studentExerciseService;
    private final ResponseFactory responseFactory;
    private final StudentExerciseManagerService studentExerciseManagerService;

    @GetMapping
    public ResponseEntity<List<StudentExerciseResponseDTO>> getStudentExercisesByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "aisId", required = false) String aisId,
            @RequestParam(name = "exerciseId", required = false) Integer exerciseId,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentExerciseService.getStudentExercisesByCriteria(id, fullName, aisId, exerciseId, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<StudentExerciseResponseDTO>>> addStudentExercises(
            @Validated(OnCreate.class) @RequestBody StudentExerciseRequestDTOList request
    ) {
        return responseFactory.created(
                "StudentExercises created successfully.",
                studentExerciseService.addStudentExercises(request.getStudentExercises())
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentExerciseResponseDTO>> updateStudentExerciseById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentExerciseRequestDTO request
    ) {
        return responseFactory.ok(
                "StudentExercise updated successfully.",
                studentExerciseManagerService.updateStudentExerciseWithStudentAttendances(id, request)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentExerciseResponseDTO>> deleteStudentExerciseById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "StudentExercise deleted successfully.",
                studentExerciseService.deleteStudentExerciseById(id)
        );
    }
}
