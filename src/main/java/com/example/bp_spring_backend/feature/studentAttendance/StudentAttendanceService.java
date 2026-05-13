package com.example.bp_spring_backend.feature.studentAttendance;

import com.example.bp_spring_backend.feature.enums.AttendanceEnum;
import com.example.bp_spring_backend.feature.enums.RoleEnum;
import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionEntity;
import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionService;
import com.example.bp_spring_backend.feature.student.StudentEntity;
import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentService;
import com.example.bp_spring_backend.core.exception.CustomValidationException;
import com.example.bp_spring_backend.feature.exerciseSession.ExerciseSessionNotFoundException;
import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentBlockPointsResponseDTO;
import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentGroupedBlockPointsResponseDTO;
import com.example.bp_spring_backend.feature.student.StudentNotFoundException;
import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.feature.student.StudentService;
import com.example.bp_spring_backend.feature.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAttendanceService {

    private final StudentAttendanceRepository studentAttendanceRepository;
    private final StudentAttendanceMapper studentAttendanceMapper;
    private final StudentService studentService;
    private final ExerciseSessionService exerciseSessionService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(StudentAttendanceService.class);
    private final StudentAssignmentService studentAssignmentService;

    public List<StudentAttendanceResponseDTO> getStudentAttendancesByCriteria(Integer id, Sort sort) {
        Specification<StudentAttendanceEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentAttendanceSpecification.hasId(id));
        }

        return studentAttendanceRepository.findAll(spec, sort).stream()
                .map(studentAttendanceMapper::toDTO)
                .toList();
    }

    public List<StudentAttendanceGroupedItemsResponseDTO> getStudentAttendanceGroupedItems(
            Integer exerciseId,
            Integer studentId,
            boolean current,
            Sort sort
    ) {

        List<StudentAttendanceEntity> attendances = loadAttendances(exerciseId, studentId, sort);

        if (current) {
            attendances = filterCurrentWeek(attendances);
        }

        List<StudentAssignmentGroupedBlockPointsResponseDTO> blockPointsDTOs =
                studentAssignmentService.getStudentAssignmentBlockPoints(exerciseId, studentId, null);

        Map<Integer, List<StudentAssignmentBlockPointsResponseDTO>> blockPointsByStudent =
                blockPointsDTOs.stream()
                        .collect(Collectors.toMap(
                                StudentAssignmentGroupedBlockPointsResponseDTO::getAisId,
                                StudentAssignmentGroupedBlockPointsResponseDTO::getAllBlockPoints
                        ));

        return attendances.stream()
                .collect(Collectors.groupingBy(
                        sa -> sa.getStudentEntity().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(group -> mapStudentAttendance(group, blockPointsByStudent))
                .toList();
    }

    private List<StudentAttendanceEntity> loadAttendances(Integer exerciseId, Integer studentId, Sort sort) {
        if (studentId != null && exerciseId == null) {
            return studentAttendanceRepository.findStudentAttendancesByStudentId(studentId, sort);
        }
        if (exerciseId != null && studentId == null) {
            return studentAttendanceRepository.findStudentAttendancesByExerciseId(exerciseId, sort);
        }
        throw new CustomValidationException(
                "Either exerciseId or studentId must be provided (but not both at the same time)."
        );
    }

    private List<StudentAttendanceEntity> filterCurrentWeek(List<StudentAttendanceEntity> attendances) {
        LocalDate now = LocalDate.now();
        int currentWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int currentYear = now.getYear();

        return attendances.stream()
                .filter(sa -> {
                    LocalDate sessionDate = sa.getExerciseSessionEntity().getSessionDate();
                    return sessionDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == currentWeek
                            && sessionDate.getYear() == currentYear;
                })
                .toList();
    }

    private StudentAttendanceGroupedItemsResponseDTO mapStudentAttendance(
            List<StudentAttendanceEntity> group,
            Map<Integer, List<StudentAssignmentBlockPointsResponseDTO>> blockPointsByStudent
    ) {

        StudentEntity student = group.get(0).getStudentEntity();

        List<StudentAssignmentBlockPointsResponseDTO> allBlockPoints =
                blockPointsByStudent.getOrDefault(student.getAisId(), List.of());

        List<StudentAttendanceItemResponseDTO> attendances =
                group.stream()
                        .sorted(Comparator.comparing(sa -> sa.getExerciseSessionEntity().getSessionDate()))
                        .map(sa -> new StudentAttendanceItemResponseDTO(
                                sa.getId(),
                                sa.getAttendanceEnum().name()
                        ))
                        .toList();

        return new StudentAttendanceGroupedItemsResponseDTO(
                student.getFullName(),
                student.getAisId(),
                allBlockPoints,
                attendances
        );
    }

    public void addStudentAttendances(List<StudentAttendanceRequestDTO> request, Integer currentUserId) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        log.info("Adding {} student attendances by user id {}", request.size(), currentUserId);
        UserEntity currentUser = userService.getUserEntityById(currentUserId);

        List<Integer> studentIds = request.stream()
                .map(StudentAttendanceRequestDTO::getStudentId)
                .distinct()
                .toList();

        List<Integer> sessionIds = request.stream()
                .map(StudentAttendanceRequestDTO::getExerciseSessionId)
                .distinct()
                .toList();

        List<StudentEntity> students = studentService.getStudentEntitiesByIds(studentIds);
        Map<Integer, StudentEntity> studentMap = students.stream()
                .collect(Collectors.toMap(StudentEntity::getId, Function.identity()));

        List<ExerciseSessionEntity> sessions = exerciseSessionService.getExerciseSessionEntitiesByIds(sessionIds);
        Map<Integer, ExerciseSessionEntity> sessionMap = sessions.stream()
                .collect(Collectors.toMap(ExerciseSessionEntity::getId, Function.identity()));

        List<StudentAttendanceEntity> studentAttendances = request.stream()
                .map(dto -> {
                    StudentEntity student = studentMap.get(dto.getStudentId());
                    ExerciseSessionEntity session = sessionMap.get(dto.getExerciseSessionId());

                    if (student == null) {
                        throw new StudentNotFoundException("");
                    }
                    if (session == null) {
                        throw new ExerciseSessionNotFoundException("");
                    }

                    return studentAttendanceMapper.toEntity(
                            dto,
                            student,
                            session,
                            currentUser,
                            LocalDateTime.now(),
                            null,
                            null
                    );
                })
                .toList();

        studentAttendanceRepository.saveAll(studentAttendances);
        log.info("Saved student attendances to DB");
    }

    public void deleteStudentAttendanceById(Integer id) {
        studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new StudentAttendanceNotFoundException(""));
        studentAttendanceRepository.deleteById(id);
    }

    public void updateStudentAttendanceById(Integer id, StudentAttendanceRequestDTO request, Integer currentUserId) {
        log.info("Updating student attendance id {} by user id {}", id, currentUserId);
        StudentAttendanceEntity studentAttendance = studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new StudentAttendanceNotFoundException(""));

        UserEntity currentUser = userService.getUserEntityById(currentUserId);

        // ADMIN can update studentId and exerciseSessionId.
        // TEACHER and HELPER can call this PUT endpoint
        // but cannot change these fields, so this if restricts updates to ADMIN only.

        if (currentUser.getRoleEnum().equals(RoleEnum.ADMIN)) {
            if (request.getStudentId() != null) {
                StudentEntity student = studentService.getStudentEntityById(request.getStudentId());
                studentAttendance.setStudentEntity(student);
            }
            if (request.getExerciseSessionId() != null) {
                ExerciseSessionEntity exerciseSession = exerciseSessionService.getExerciseSessionEntityById(request.getExerciseSessionId());
                studentAttendance.setExerciseSessionEntity(exerciseSession);
            }
        }
        if (request.getAttendanceEnum() != null) {
            studentAttendance.setAttendanceEnum(request.getAttendanceEnum());
        }

        studentAttendance.setUpdatedBy(currentUser);
        studentAttendance.setUpdatedAt(LocalDateTime.now());

        studentAttendance = studentAttendanceRepository.save(studentAttendance);
        log.info("Saved student attendance entity id {}", studentAttendance.getId());
    }

    public void addInitialAttendancesForStudents(List<StudentEntity> students, Integer exerciseId) {

        UserEntity systemUser = userService.getSystemUser("SYSTEM");

        List<StudentAttendanceEntity> studentAttendances = new ArrayList<>();

        List<ExerciseSessionEntity> exerciseSessions = exerciseSessionService.getSessionsForExerciseDesc(exerciseId);

        for (ExerciseSessionEntity exerciseSession : exerciseSessions) {
            for (StudentEntity student : students) {
                studentAttendances.add(StudentAttendanceEntity.builder()
                        .studentEntity(student)
                        .exerciseSessionEntity(exerciseSession)
                        .attendanceEnum(AttendanceEnum.ABSENT)
                        .createdBy(systemUser)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }

        studentAttendanceRepository.saveAll(studentAttendances);
    }
    
    public void updateAttendancesForStudent(Integer studentId, Integer exerciseId) {

        List<ExerciseSessionEntity> exerciseSessions = exerciseSessionService.getSessionsForExerciseDesc(exerciseId);

        List<StudentAttendanceEntity> studentAttendances = studentAttendanceRepository.findByStudentEntity_IdOrderByExerciseSessionEntity_SessionDateDesc(studentId);

        int i = 0;
        for (StudentAttendanceEntity studentAttendance : studentAttendances) {
            studentAttendance.setExerciseSessionEntity(exerciseSessions.get(i));
            i++;
        }

        studentAttendanceRepository.saveAll(studentAttendances);
    }

    public void softDeleteStudentAttendancesByUserId(Integer userId) {
        studentAttendanceRepository.softDeleteByUserId(userId);
    }

    public void softDeleteStudentAttendancesByStudentId(Integer studentId) {
        studentAttendanceRepository.softDeleteByStudentId(studentId);
    }

    public void softDeleteStudentAttendancesByExerciseSessionId(Integer exerciseSessionId) {
        studentAttendanceRepository.softDeleteByExerciseSessionId(exerciseSessionId);
    }

    public void softDeleteStudentAttendancesByExerciseSessionIds(List<Integer> exerciseSessionIds) {
        if (exerciseSessionIds.isEmpty()) return;
        studentAttendanceRepository.softDeleteByExerciseSessionIds(exerciseSessionIds);
    }
}
