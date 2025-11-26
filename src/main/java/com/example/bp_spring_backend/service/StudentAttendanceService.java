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
import com.example.bp_spring_backend.exception.StudentAttendanceNotFoundException;
import com.example.bp_spring_backend.mapper.StudentAttendanceMapper;
import com.example.bp_spring_backend.repository.StudentAttendanceRepository;
import com.example.bp_spring_backend.specification.StudentAttendanceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.*;
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
            boolean current,
            Sort sort
    ) {

        List<StudentAttendanceEntity> attendances = studentAttendanceRepository.findStudentAttendancesByExerciseId(exerciseId, sort);

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
                        sa -> sa.getStudentEntity().getFullName(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> new StudentAttendanceGroupedItemsResponseDTO(
                        entry.getKey(),
                        entry.getValue().stream()
                                .sorted(Comparator.comparing(sa -> sa.getExerciseSessionEntity().getSessionDate()))
                                .map(sa -> new StudentAttendanceItemResponseDTO(
                                        sa.getId(),
                                        sa.getAttendanceEnum().name()
                                ))
                                .toList()
                ))
                .toList();
    }

    public List<StudentAttendanceResponseDTO> addStudentAttendances(List<StudentAttendanceRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<StudentAttendanceEntity> studentAttendances = request.stream()
                .map(dto -> studentAttendanceMapper.toEntity(
                        dto,
                        studentService.getStudentEntityById(dto.getStudentId()),
                        exerciseSessionService.getExerciseSessionEntityById(dto.getExerciseSessionId()),
                        userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
                        LocalDateTime.now(),
                        null,
                        null
                ))
                .toList();

        List<StudentAttendanceEntity> savedStudentAttendances = studentAttendanceRepository.saveAll(studentAttendances);

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

    public StudentAttendanceResponseDTO updateStudentAttendanceById(Integer id, StudentAttendanceRequestDTO request) {
        StudentAttendanceEntity studentAttendance = studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new StudentAttendanceNotFoundException(""));

        if (request.getStudentId() != null) {
            studentAttendance.setStudentEntity(studentService.getStudentEntityById(request.getStudentId()));
        }
        if (request.getExerciseSessionId() != null) {
            studentAttendance.setExerciseSessionEntity(exerciseSessionService.getExerciseSessionEntityById(request.getExerciseSessionId()));
        }
        if (request.getAttendanceEnum() != null) {
            studentAttendance.setAttendanceEnum(request.getAttendanceEnum());
        }

        UserEntity currentUser = userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

        studentAttendance.setUpdatedBy(currentUser);
        studentAttendance.setUpdatedAt(LocalDateTime.now());

        studentAttendance = studentAttendanceRepository.save(studentAttendance);

        /*
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
                    }
                }
            }
        }
        */
        System.out.println("Email sent ...");

        return studentAttendanceMapper.toDTO(studentAttendance);
    }

    public List<StudentAttendanceEntity> getAttendanceEntitiesByStudentIdDesc(Integer studentId) {
        return studentAttendanceRepository.findByStudentEntity_IdOrderByCreatedAtDesc(studentId);
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

        List<StudentAttendanceEntity> studentAttendances = getAttendanceEntitiesByStudentIdDesc(studentId);

        int i = 0;
        for (StudentAttendanceEntity studentAttendance : studentAttendances) {
            studentAttendance.setExerciseSessionEntity(exerciseSessions.get(i));
            i++;
        }

        studentAttendanceRepository.saveAll(studentAttendances);
    }

    @Transactional
    public void softDeleteStudentAttendancesByUserId(Integer userId) {
        studentAttendanceRepository.softDeleteByUserId(userId);
    }

    @Transactional
    public void softDeleteStudentAttendancesByStudentId(Integer studentId) {
        studentAttendanceRepository.softDeleteByStudentId(studentId);
    }

    @Transactional
    public void softDeleteStudentAttendancesByExerciseSessionId(Integer exerciseSessionId) {
        studentAttendanceRepository.softDeleteByExerciseSessionId(exerciseSessionId);
    }

    @Transactional
    public void softDeleteStudentAttendancesByExerciseSessionIds(List<Integer> exerciseSessionIds) {
        if (exerciseSessionIds.isEmpty()) return;
        studentAttendanceRepository.softDeleteByExerciseSessionIds(exerciseSessionIds);
    }
}
