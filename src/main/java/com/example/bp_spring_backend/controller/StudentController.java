package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentRequestDTOList;
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
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "aisId", required = false) Integer aisId,
            @RequestParam(name = "email", required = false) String email,
            Sort sort
    ) {
        Map<String, Object> searchCriteria  = new HashMap<>();
        if (id != null) {
            searchCriteria.put("id", id);
        }
        if (aisId != null) {
            searchCriteria.put("aisId", aisId);
        }
        if (email != null) {
            searchCriteria.put("email", email);
        }
        List<StudentResponseDTO> students = studentService.getStudentsByCriteria(searchCriteria, sort);
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<List<StudentResponseDTO>>> addStudents(
            @Validated(OnCreate.class) @RequestBody StudentRequestDTOList request
    ) {
        List<StudentResponseDTO> addedStudents = studentService.addStudents(request.getStudents());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.<List<StudentResponseDTO>>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Students created successfully.")
                        .data(addedStudents)
                        .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentResponseDTO>> updateStudentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Student updated successfully.")
                        .data(studentService.updateStudentById(id, request))
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<StudentResponseDTO>> deleteStudentById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Student deleted successfully.")
                        .data(studentService.deleteStudentById(id))
                        .build());
    }
}
