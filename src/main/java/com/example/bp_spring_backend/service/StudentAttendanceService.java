package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.*;
import com.example.bp_spring_backend.domains.enums.AttendanceEnum;
import com.example.bp_spring_backend.domains.inputDTO.StudentAttendanceRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentAttendanceResponseDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentAttendanceService {

    private final StudentAttendanceRepository studentAttendanceRepository;
    private final StudentAttendanceMapper studentAttendanceMapper;
    private final StudentService studentService;
    private final ExerciseSessionService exerciseSessionService;
    private final UserService userService;

    public List<StudentAttendanceResponseDTO> getStudentAttendancesByCriteria(Integer id, Sort sort) {
        Specification<StudentAttendanceEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentAttendanceSpecification.hasId(id));
        }

        return studentAttendanceRepository.findAll(spec, sort).stream()
                .map(studentAttendanceMapper::toDTO)
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

        studentAttendance.setUpdatedBy(userService.getUserEntityById(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        studentAttendance.setUpdatedAt(LocalDateTime.now());

        studentAttendance = studentAttendanceRepository.save(studentAttendance);

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
}
