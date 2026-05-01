package com.example.bp_spring_backend.feature.studentAssignment;

import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.core.dto.SuccessResponseDTO;
import com.example.bp_spring_backend.core.utils.ResponseFactory;
import com.example.bp_spring_backend.core.validation.OnCreate;
import com.example.bp_spring_backend.core.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-assignment")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;
    private final ResponseFactory responseFactory;
    private final StudentAssignmentManagerService studentAssignmentManagerService;

    @GetMapping
    public ResponseEntity<List<StudentAssignmentResponseDTO>> getStudentAssignmentsByCriteria(
            @RequestParam(name = "id", required = false) Integer id,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAssignmentService.getStudentAssignmentsByCriteria(id, sort)
        );
    }

    @GetMapping("/grading")
    public ResponseEntity<List<StudentAssignmentGroupedItemsResponseDTO>> getStudentAssignmentGroupedItems(
            @RequestParam(name = "blockId") Integer blockId,
            @RequestParam(name = "studentId", required = false) Integer studentId,
            @RequestParam(name = "exerciseId", required = false) Integer exerciseId,
            @RequestParam(name = "studentFullName", required = false) String studentFullName,
            Sort sort
    ) {
        return ResponseEntity.ok(
                studentAssignmentService.getStudentAssignmentGroupedItems(blockId, exerciseId, studentId, studentFullName, sort)
        );
    }

    @GetMapping("/block-points")
    public List<StudentAssignmentGroupedBlockPointsResponseDTO> getStudentBlockPoints(
            @RequestParam(name = "exerciseId", required = false) Integer exerciseId,
            @RequestParam(name = "studentId", required = false) Integer studentId,
            @RequestParam(name = "studentFullName", required = false) String studentFullName
    ) {
        return studentAssignmentService.getStudentAssignmentBlockPoints(exerciseId, studentId, studentFullName);
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Void>> addStudentAssignments(
            @Validated(OnCreate.class) @RequestBody StudentAssignmentRequestDTOList request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        studentAssignmentService.addStudentAssignments(request.getStudentAssignments(), currentUser.getId());
        return responseFactory.created(
                "StudentAssignments created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateStudentAssignmentById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentAssignmentRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        studentAssignmentService.updateStudentAssignmentById(id, request, currentUser.getId());
        return responseFactory.ok(
                "StudentAssignment updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteStudentAssignmentById(
            @PathVariable Integer id
    ) {
        studentAssignmentManagerService.softDeleteStudentAssignmentCascade(id);
        return responseFactory.ok(
                "StudentAssignment deleted successfully."
        );
    }
}
