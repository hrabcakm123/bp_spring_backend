package com.example.bp_spring_backend.feature.studentAttendance;

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
    public ResponseEntity<SuccessResponseDTO<Void>> addStudentAttendances(
            @Validated(OnCreate.class) @RequestBody StudentAttendanceRequestDTOList request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        studentAttendanceService.addStudentAttendances(request.getStudentAttendances(), currentUser.getId());
        return responseFactory.created(
                "StudentAttendances created successfully."
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> updateStudentAttendanceById(
            @PathVariable Integer id,
            @Validated(OnUpdate.class) @RequestBody StudentAttendanceRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        studentAttendanceService.updateStudentAttendanceById(id, request, currentUser.getId());
        return responseFactory.ok(
                "StudentAttendance updated successfully."
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponseDTO<Void>> deleteStudentAttendanceById(
            @PathVariable Integer id
    ) {
        studentAttendanceService.deleteStudentAttendanceById(id);
        return responseFactory.ok(
                "StudentAttendance deleted successfully."
        );
    }
}
