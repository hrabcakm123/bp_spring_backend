package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentResponseDTO;
import com.example.bp_spring_backend.exception.CustomValidationException;
import com.example.bp_spring_backend.exception.StudentNotFoundException;
import com.example.bp_spring_backend.mapper.StudentMapper;
import com.example.bp_spring_backend.repository.StudentRepository;
import com.example.bp_spring_backend.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentEntity getStudentEntityById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(""));
    }

    public List<StudentResponseDTO> getStudentsByCriteria(Integer id, Integer aisId, String email, Sort sort) {
        Specification<StudentEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentSpecification.hasId(id));
        }
        if (aisId != null) {
            spec = spec.and(StudentSpecification.hasAisId(aisId));
        }
        if (email != null) {
            spec = spec.and(StudentSpecification.containsEmail(email));
        }

        return studentRepository.findAll(spec, sort).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public List<StudentResponseDTO> addStudents(List<StudentRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<StudentEntity> students = request.stream()
                .map(studentMapper::toEntity)
                .toList();

        List<StudentEntity> savedStudents = studentRepository.saveAll(students);

        return savedStudents.stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public StudentResponseDTO deleteStudentById(Integer id) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(""));
        studentRepository.deleteById(id);
        return studentMapper.toDTO(student);
    }

    public StudentResponseDTO updateStudentById(Integer id, StudentRequestDTO request) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(""));

        if(request.getAisId() != null) {
            student.setAisId(request.getAisId());
        }
        if (request.getFirstname() != null) {
            student.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            student.setLastname(request.getLastname());
        }
        if (request.getEmail() != null) {
            student.setEmail(request.getEmail());
        }

        student = studentRepository.save(student);

        return studentMapper.toDTO(student);
    }
}
