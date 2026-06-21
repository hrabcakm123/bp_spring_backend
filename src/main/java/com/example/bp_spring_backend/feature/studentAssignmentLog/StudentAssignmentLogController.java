package com.example.bp_spring_backend.feature.studentAssignmentLog;

import com.example.bp_spring_backend.core.dto.SuccessResponseDTO;
import com.example.bp_spring_backend.core.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-assignment-log")
@RequiredArgsConstructor
public class StudentAssignmentLogController {

    private final StudentAssignmentLogService studentAssignmentLogService;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<PageResponseDTO<StudentAssignmentLogResponseDTO>> getStudentAssignmentLogsByCriteria(
            @RequestParam(name = "originalUserFullName", required = false) String originalUserFullName,
            @RequestParam(name = "updatedByUserFullName", required = false) String updatedByUserFullName,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                studentAssignmentLogService.getStudentAssignmentLogsByCriteria(
                        originalUserFullName,
                        updatedByUserFullName,
                        pageable
                )
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteStudentAssignmentLogById(
            @PathVariable Integer id
    ) {
        studentAssignmentLogService.deleteStudentAssignmentLogById(id);
        return responseFactory.ok(
                "StudentAssignmentLog deleted successfully."
        );
    }
}
