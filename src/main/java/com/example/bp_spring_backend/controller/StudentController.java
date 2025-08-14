package com.example.bp_spring_backend.controller;

import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.SuccessResponseDTO;
import com.example.bp_spring_backend.service.StudentService;
import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import com.example.bp_spring_backend.validation.StudentRequestDTOList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/get")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Integer id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/get/email/{email}")
    public ResponseEntity<StudentResponseDTO> getStudentByEmail(@PathVariable String email) {
        StudentResponseDTO student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/get/aisId/{aisId}")
    public ResponseEntity<StudentResponseDTO> getStudentByAisId(@PathVariable Integer aisId) {
        StudentResponseDTO student = studentService.getStudentByAisId(aisId);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/post")
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

    @PatchMapping("/patch/id/{id}")
    public ResponseEntity<SuccessResponseDTO<StudentResponseDTO>> updateStudentById(
            @PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody StudentRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Student updated successfully.")
                        .data(studentService.updateStudentById(id, request))
                        .build());
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<SuccessResponseDTO<StudentResponseDTO>> deleteStudentById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.<StudentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Student deleted successfully.")
                        .data(studentService.deleteStudentById(id))
                        .build());
    }
}
