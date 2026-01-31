package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.domains.inputDTO.StudentAssignmentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentGroupedItemsResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentItemResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAssignmentResponseDTO;
import com.example.bp_spring_backend.exception.AssignmentNotFoundException;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.StudentAssignmentNotFoundException;
import com.example.bp_spring_backend.exception.StudentNotFoundException;
import com.example.bp_spring_backend.mapper.AssignmentMapper;
import com.example.bp_spring_backend.mapper.StudentAssignmentMapper;
import com.example.bp_spring_backend.mapper.StudentMapper;
import com.example.bp_spring_backend.repository.StudentAssignmentRepository;
import com.example.bp_spring_backend.specification.StudentAssignmentSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAssignmentService {

    private final StudentAssignmentRepository studentAssignmentRepository;
    private final StudentAssignmentMapper studentAssignmentMapper;
    private final AssignmentService assignmentService;
    private final StudentService studentService;
    private final UserService userService;
    private final StudentAssignmentLogService studentAssignmentLogService;
    private static final Logger log = LoggerFactory.getLogger(StudentAssignmentService.class);
    private final AssignmentMapper assignmentMapper;
    private final StudentMapper studentMapper;

    public List<StudentAssignmentResponseDTO> getStudentAssignmentsByCriteria(Integer id, Sort sort) {
        Specification<StudentAssignmentEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentAssignmentSpecification.hasId(id));
        }

        return studentAssignmentRepository.findAll(spec, sort).stream()
                .map(studentAssignmentMapper::toDTO)
                .toList();
    }

    public List<StudentAssignmentGroupedItemsResponseDTO> getStudentAssignmentGroupedItems(
            Integer blockId,
            Integer exerciseId,
            Integer studentId,
            String studentFullName,
            Sort sort
    ) {
        if (studentId != null) {
            return getStudentAssignmentGroupedItemsByBlockIdAndStudentId(studentId, blockId);
        } else if (exerciseId != null) {
            return getStudentAssignmentGroupedItemsByBlockIdAndExerciseId(blockId, exerciseId, studentFullName, sort);
        } else {
            throw new CustomValidationException("Either studentId or exerciseId must be provided");
        }
    }

    private List<StudentAssignmentGroupedItemsResponseDTO> getStudentAssignmentGroupedItemsByBlockIdAndStudentId(Integer studentId, Integer blockId) {
        List<StudentAssignmentEntity> rows = studentAssignmentRepository.findStudentAssignmentsByBlockIdAndStudentId(blockId, studentId);
        if (rows.isEmpty()) {
            return Collections.emptyList();
        }
        StudentEntity student = rows.get(0).getStudentEntity();

        return List.of(new StudentAssignmentGroupedItemsResponseDTO(
                student.getFullName(),
                student.getAisId(),
                rows.stream()
                        .map(sa -> new StudentAssignmentItemResponseDTO(
                                sa.getId(),
                                sa.getEarnedPoints(),
                                sa.getNote()
                        ))
                        .toList()
        ));
    }

    private List<StudentAssignmentGroupedItemsResponseDTO> getStudentAssignmentGroupedItemsByBlockIdAndExerciseId(
            Integer blockId,
            Integer exerciseId,
            String studentFullName,
            Sort sort
    ) {
        List<StudentAssignmentEntity> rows;

        if (studentFullName == null || studentFullName.trim().isEmpty()) {
            rows = studentAssignmentRepository.findStudentAssignmentsByBlockIdAndExerciseId(blockId, exerciseId, sort);
        } else {
            rows = studentAssignmentRepository.findStudentAssignmentsByBlockIdAndExerciseIdAndStudentFullName(blockId, exerciseId, studentFullName);
        }

        return rows.stream()
                .collect(Collectors.groupingBy(
                        sa -> sa.getStudentEntity().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(group -> {
                    StudentEntity student = group.get(0).getStudentEntity();
                    return new StudentAssignmentGroupedItemsResponseDTO(
                            student.getFullName(),
                            student.getAisId(),
                            group.stream()
                                    .map(sa -> new StudentAssignmentItemResponseDTO(
                                            sa.getId(),
                                            sa.getEarnedPoints(),
                                            sa.getNote()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }

    public List<StudentAssignmentResponseDTO> addStudentAssignments(List<StudentAssignmentRequestDTO> request, Integer currentUserId) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        log.info("Adding {} student assignments by user id {}", request.size(), currentUserId);
        UserEntity currentUser = userService.getUserEntityById(currentUserId);

        List<Integer> assignmentIds = request.stream()
                .map(StudentAssignmentRequestDTO::getAssignmentId)
                .distinct()
                .toList();

        List<Integer> studentIds = request.stream()
                .map(StudentAssignmentRequestDTO::getStudentId)
                .distinct()
                .toList();

        List<AssignmentEntity> assignments = assignmentService.getAssignmentEntitiesByIds(assignmentIds);
        Map<Integer, AssignmentEntity> assignmentMap = assignments.stream()
                .collect(Collectors.toMap(AssignmentEntity::getId, Function.identity()));

        List<StudentEntity> students = studentService.getStudentEntitiesByIds(studentIds);
        Map<Integer, StudentEntity> studentMap = students.stream()
                .collect(Collectors.toMap(StudentEntity::getId, Function.identity()));

        List<StudentAssignmentEntity> studentAssignments = request.stream()
                .map(dto -> {
                    AssignmentEntity assignment = assignmentMap.get(dto.getAssignmentId());
                    StudentEntity student = studentMap.get(dto.getStudentId());

                    if (assignment == null) {
                        throw new AssignmentNotFoundException("");
                    }
                    if (student == null) {
                        throw new StudentNotFoundException("");
                    }

                    return studentAssignmentMapper.toEntity(
                            dto,
                            assignment,
                            student,
                            currentUser,
                            LocalDateTime.now(),
                            null,
                            null
                    );
                })
                .toList();

        List<StudentAssignmentEntity> savedStudentAssignments = studentAssignmentRepository.saveAll(studentAssignments);
        log.info("Saved {} student assignments to DB", savedStudentAssignments.size());
        return savedStudentAssignments.stream()
                .map(studentAssignmentMapper::toDTO)
                .toList();
    }

    public StudentAssignmentResponseDTO deleteStudentAssignmentById(Integer id) {
        StudentAssignmentEntity studentAssignment = studentAssignmentRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentNotFoundException(""));
        studentAssignmentRepository.deleteById(id);
        return studentAssignmentMapper.toDTO(studentAssignment);
    }

    @Transactional
    public StudentAssignmentResponseDTO updateStudentAssignmentById(Integer id, StudentAssignmentRequestDTO request, Integer currentUserId) {
        log.info("Updating student assignment id {} by user id {}", id, currentUserId);
        StudentAssignmentEntity studentAssignment = studentAssignmentRepository.findById(id)
                .orElseThrow(() -> new StudentAssignmentNotFoundException(""));

        UserEntity currentUser = userService.getUserEntityById(currentUserId);

        Double oldPoints = studentAssignment.getEarnedPoints();

        StudentAssignmentResponseDTO response = new StudentAssignmentResponseDTO();

        // ADMIN can update assignmentId and studentId.
        // TEACHER and HELPER can call this PUT endpoint
        // but cannot change these fields, so this if restricts updates to ADMIN only.

        if (currentUser.getRoleEnum().equals(RoleEnum.ADMIN)) {
            if (request.getAssignmentId() != null) {
                AssignmentEntity assignment = assignmentService.getAssignmentEntityById(request.getAssignmentId());
                studentAssignment.setAssignmentEntity(assignment);
                response.setAssignment(assignmentMapper.toDTO(assignment));
            }
            if (request.getStudentId() != null) {
                StudentEntity student = studentService.getStudentEntityById(request.getStudentId());
                studentAssignment.setStudentEntity(student);
                response.setStudent(studentMapper.toDTO(student));
            }
        }
        if (request.getEarnedPoints() != null && !request.getEarnedPoints().equals(oldPoints)) {
            studentAssignment.setEarnedPoints(request.getEarnedPoints());
            response.setEarnedPoints(request.getEarnedPoints());
        }
        if (request.getNote() != null && !request.getNote().equals(studentAssignment.getNote())) {
            studentAssignment.setNote(request.getNote());
            response.setNote(request.getNote());
        }

        if (request.getEarnedPoints() != null
                && !request.getEarnedPoints().equals(oldPoints)
                && currentUser.getRoleEnum() == RoleEnum.HELPER
                && studentAssignment.getUpdatedBy() != null
                && (studentAssignment.getUpdatedBy().getRoleEnum() == RoleEnum.ADMIN
                || studentAssignment.getUpdatedBy().getRoleEnum() == RoleEnum.TEACHER)) {

            studentAssignmentLogService.createLog(studentAssignment, oldPoints, request.getEarnedPoints(), currentUser);
            log.info("Created log for student assignment id {}: oldPoints={}, newPoints={}, helperUserId={}", id, oldPoints, request.getEarnedPoints(), currentUserId);
        }

        studentAssignment.setUpdatedBy(currentUser);
        studentAssignment.setUpdatedAt(LocalDateTime.now());

        studentAssignment = studentAssignmentRepository.save(studentAssignment);
        log.info("Saved student assignment entity id {}", studentAssignment.getId());

        return response;
    }

    public void createAssignmentsForStudents(
            List<StudentEntity> students,
            List<AssignmentEntity> assignments
    ) {

        UserEntity systemUser = userService.getSystemUser("SYSTEM");

        List<StudentAssignmentEntity> toSave =  new ArrayList<>();

        for (StudentEntity student : students) {
            for (AssignmentEntity assignment : assignments) {
                toSave.add(StudentAssignmentEntity.builder()
                        .studentEntity(student)
                        .assignmentEntity(assignment)
                        .earnedPoints(0.0)
                        .note(null)
                        .createdBy(systemUser)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }

        studentAssignmentRepository.saveAll(toSave);
    }

    public List<Integer> softDeleteStudentAssignmentsByUserId(Integer userId) {

        List<Integer> studentAssignmentIds = studentAssignmentRepository.findStudentAssignmentIdsByCreatedOrUpdatedByUserId(userId);

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentRepository.softDeleteByUserId(userId);
        }

        return studentAssignmentIds;
    }

    public List<Integer> softDeleteStudentAssignmentsByStudentId(Integer studentId) {

        List<Integer> studentAssignmentIds = studentAssignmentRepository.findStudentAssignmentIdsByStudentEntityId(studentId);

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentRepository.softDeleteByStudentId(studentId);
        }

        return studentAssignmentIds;
    }

    public List<Integer> softDeleteStudentAssignmentsByAssignmentId(Integer assignmentId) {

        List<Integer> studentAssignmentIds = studentAssignmentRepository.findStudentAssignmentIdsByAssignmentId(assignmentId);

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentRepository.softDeleteByAssignmentId(assignmentId);
        }

        return studentAssignmentIds;
    }

    public List<Integer> softDeleteStudentAssignmentsByAssignmentIds(List<Integer> assignmentIds) {

        if (assignmentIds == null || assignmentIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> studentAssignmentIds = studentAssignmentRepository.findStudentAssignmentIdsByAssignmentIds(assignmentIds);

        studentAssignmentRepository.softDeleteByAssignmentIds(assignmentIds);

        return studentAssignmentIds;
    }
}
