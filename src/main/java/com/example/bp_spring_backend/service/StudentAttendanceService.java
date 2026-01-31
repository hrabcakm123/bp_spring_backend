package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.enums.AttendanceEnum;
import com.example.bp_spring_backend.domains.enums.RoleEnum;
import com.example.bp_spring_backend.domains.inputDTO.StudentAttendanceRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAttendanceGroupedItemsResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAttendanceItemResponseDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAttendanceResponseDTO;
import com.example.bp_spring_backend.email.EmailSenderService;
import com.example.bp_spring_backend.email.EmailTemplateBuilder;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.ExerciseSessionNotFoundException;
import com.example.bp_spring_backend.exception.StudentAttendanceNotFoundException;
import com.example.bp_spring_backend.exception.StudentNotFoundException;
import com.example.bp_spring_backend.mapper.ExerciseSessionMapper;
import com.example.bp_spring_backend.mapper.StudentAttendanceMapper;
import com.example.bp_spring_backend.mapper.StudentMapper;
import com.example.bp_spring_backend.repository.StudentAttendanceRepository;
import com.example.bp_spring_backend.specification.StudentAttendanceSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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
    private final UserExerciseService userExerciseService;
    private final EmailSenderService emailSenderService;
    private final EmailTemplateBuilder emailTemplateBuilder;
    private static final Logger log = LoggerFactory.getLogger(StudentAttendanceService.class);
    private final StudentMapper studentMapper;
    private final ExerciseSessionMapper exerciseSessionMapper;

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

        List<StudentAttendanceEntity> attendances;

        if (studentId != null && exerciseId == null) {
            attendances = studentAttendanceRepository.findStudentAttendancesByStudentId(studentId, sort);
        } else if (exerciseId != null && studentId == null) {
            attendances = studentAttendanceRepository.findStudentAttendancesByExerciseId(exerciseId, sort);
        } else {
            throw new CustomValidationException("Either exerciseId or studentId must be provided (but not both at the same time).");
        }

        if (current) {
            LocalDate now = LocalDate.now();
            int currentWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int currentYear = now.getYear();

            attendances = attendances.stream()
                    .filter(sa -> {
                        LocalDate sessionDate = sa.getExerciseSessionEntity().getSessionDate();
                        int week = sessionDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                        int year = sessionDate.getYear();
                        return week == currentWeek && year == currentYear;
                    })
                    .toList();
        }

        return attendances.stream()
                .collect(Collectors.groupingBy(
                        sa -> sa.getStudentEntity().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(group -> {
                    StudentEntity student = group.get(0).getStudentEntity();
                    return new StudentAttendanceGroupedItemsResponseDTO(
                            student.getFullName(),
                            student.getAisId(),
                            group.stream()
                                    .sorted(Comparator.comparing(sa -> sa.getExerciseSessionEntity().getSessionDate()))
                                    .map(sa -> new StudentAttendanceItemResponseDTO(
                                            sa.getId(),
                                            sa.getAttendanceEnum().name()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }

    public List<StudentAttendanceResponseDTO> addStudentAttendances(List<StudentAttendanceRequestDTO> request, Integer currentUserId) {
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

        List<StudentAttendanceEntity> savedStudentAttendances = studentAttendanceRepository.saveAll(studentAttendances);
        log.info("Saved {} student attendances to DB", savedStudentAttendances.size());
        return savedStudentAttendances.stream()
                .map(studentAttendanceMapper::toDTO)
                .toList();
    }

    public StudentAttendanceResponseDTO deleteStudentAttendanceById(Integer id) {
        StudentAttendanceEntity studentAttendance = studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new StudentAttendanceNotFoundException(""));
        studentAttendanceRepository.deleteById(id);
        return studentAttendanceMapper.toDTO(studentAttendance);
    }

    public StudentAttendanceResponseDTO updateStudentAttendanceById(Integer id, StudentAttendanceRequestDTO request, Integer currentUserId) {
        log.info("Updating student attendance id {} by user id {}", id, currentUserId);
        StudentAttendanceEntity studentAttendance = studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new StudentAttendanceNotFoundException(""));

        UserEntity currentUser = userService.getUserEntityById(currentUserId);

        StudentAttendanceResponseDTO response = new  StudentAttendanceResponseDTO();

        // ADMIN can update studentId and exerciseSessionId.
        // TEACHER and HELPER can call this PUT endpoint
        // but cannot change these fields, so this if restricts updates to ADMIN only.

        if (currentUser.getRoleEnum().equals(RoleEnum.ADMIN)) {
            if (request.getStudentId() != null) {
                StudentEntity student = studentService.getStudentEntityById(request.getStudentId());
                studentAttendance.setStudentEntity(student);
                response.setStudent(studentMapper.toDTO(student));
            }
            if (request.getExerciseSessionId() != null) {
                ExerciseSessionEntity exerciseSession = exerciseSessionService.getExerciseSessionEntityById(request.getExerciseSessionId());
                studentAttendance.setExerciseSessionEntity(exerciseSession);
                response.setExerciseSession(exerciseSessionMapper.toDTO(exerciseSession));
            }
        }
        if (request.getAttendanceEnum() != null) {
            studentAttendance.setAttendanceEnum(request.getAttendanceEnum());
            response.setAttendanceEnum(request.getAttendanceEnum());
        }

        studentAttendance.setUpdatedBy(currentUser);
        studentAttendance.setUpdatedAt(LocalDateTime.now());

        studentAttendance = studentAttendanceRepository.save(studentAttendance);
        log.info("Saved student attendance entity id {}", studentAttendance.getId());


        if (request.getAttendanceEnum() == AttendanceEnum.SUBSTITUTED) {
            List<UserEntity> users = userExerciseService.getUsersForExercise(studentAttendance.getExerciseSessionEntity().getExerciseEntity().getId());
            if (!users.contains(currentUser)) {
                for (UserEntity user : users) {
                    if (user.getRoleEnum() == RoleEnum.TEACHER || user.getRoleEnum() == RoleEnum.ADMIN) {
                        emailSenderService.sendEmail(
                                user.getEmail(),
                                "[AP] Oznámenie o náhrade cvičenia",
                                emailTemplateBuilder.buildSubstitutionInfoEmail(
                                        user.getFullName(),
                                        studentAttendance.getStudentEntity().getFullName(),
                                        studentAttendance.getStudentEntity().getAisId().toString(),
                                        studentAttendance.getExerciseSessionEntity().getExerciseEntity().getFirstSessionDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("sk", "SK")),
                                        studentAttendance.getExerciseSessionEntity().getExerciseEntity().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                                        studentAttendance.getExerciseSessionEntity().getSessionDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")),
                                        currentUser.getFullName()
                                )
                        );
                        log.info("Sent substitution emails for student attendance id {}", id);
                    }
                }
            }
        }

        //System.out.println("Email sent ...");

        return response;
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
