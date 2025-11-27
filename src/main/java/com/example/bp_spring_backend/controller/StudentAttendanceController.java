package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentAttendanceRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAttendanceGroupedItemsResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAttendanceResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentAttendanceService;
import com.example.bp_spring_backend.utils.ResponseFactory;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentAttendanceRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-attendance")
@RequiredArgsConstructor
public class StudentAttendanceController {

    private final StudentAttendanceService studentAttendanceService;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<List<StudentAttendanceResponseDTO>> getStudentAttendancesByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAttendanceService.getStudentAttendancesByCriteria(id, sort)
        );
    }

    @GetMapping("/attendance")
    public ResponseEntity<List<StudentAttendanceGroupedItemsResponseDTO>> getStudentAttendanceGroupedItems(
            @RequestParam(name = "exerciseId", required = false) Integer exerciseId,
            @RequestParam(name = "studentId", required = false) Integer studentId,
            @RequestParam(name = "current", required = false, defaultValue = "false") boolean current,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAttendanceService.getStudentAttendanceGroupedItems(exerciseId, studentId, current, sort)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<StudentAttendanceResponseDTO>>> addStudentAttendances(
            @Validated(OnCreate.class) @RequestBody StudentAttendanceRequestDTOList request
    ) {
        return responseFactory.created(
                "StudentAttendances created successfully.",
                studentAttendanceService.addStudentAttendances(request.getStudentAttendances())
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAttendanceResponseDTO>> updateStudentAttendanceById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentAttendanceRequestDTO request
    ) {
        return responseFactory.ok(
                "StudentAttendance updated successfully.",
                studentAttendanceService.updateStudentAttendanceById(id, request)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentAttendanceResponseDTO>> deleteStudentAttendanceById(
            @PathVariable Integer id
    ) {
        return responseFactory.ok(
                "StudentAttendance deleted successfully.",
                studentAttendanceService.deleteStudentAttendanceById(id)
        );
    }
}
