package com.example.bp_spring_backend.feature.studentExercise;

import com.example.bp_spring_backend.core.dto.SuccessResponseDTO;
import com.example.bp_spring_backend.core.utils.ResponseFactory;
import com.example.bp_spring_backend.core.validation.OnCreate;
import com.example.bp_spring_backend.core.validation.OnUpdate;
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

    @GetMapping("/exercise-for-student")
    public ResponseEntity<ExerciseForStudentResponseDTO> getExerciseForStudent(
            @RequestParam(name = "aisId") String aisId
    ) {
        return ResponseEntity.ok(
                studentExerciseService.getExerciseForStudent(aisId)
        );
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addStudentExercises(
            @Validated(OnCreate.class) @RequestBody StudentExerciseRequestDTOList request
    ) {
        studentExerciseService.addStudentExercises(request.getStudentExercises());
        return responseFactory.created(
                "StudentExercises created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateStudentExerciseById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentExerciseRequestDTO request
    ) {
        studentExerciseManagerService.updateStudentExerciseWithStudentAttendances(id, request);
        return responseFactory.ok(
                "StudentExercise updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteStudentExerciseById(
            @PathVariable Integer id
    ) {
        studentExerciseService.deleteStudentExerciseById(id);
        return responseFactory.ok(
                "StudentExercise deleted successfully."
        );
    }
}
