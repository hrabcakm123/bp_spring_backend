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

    public List<StudentResponseDTO> getStudentsByCriteria(Integer id, Integer aisId, Sort sort) {
        Specification<StudentEntity> spec = (root, query, builder) -> null;
        if (id != null) {
            spec = spec.and(StudentSpecification.hasId(id));
        }
        if (aisId != null) {
            spec = spec.and(StudentSpecification.hasAisId(aisId));
        }

        return studentRepository.findAll(spec, sort).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public List<StudentResponseDTO> searchStudents(String searchQuery, Sort sort) {
        return studentRepository.findAll(StudentSpecification.search(searchQuery), sort).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public List<StudentEntity> addStudentEntities(List<StudentRequestDTO> request) {
        if (request == null) {
            throw new CustomValidationException("List name is wrong or missing.");
        }
        List<StudentEntity> students = request.stream()
                .map(studentMapper::toEntity)
                .toList();

        return studentRepository.saveAll(students);
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
        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }

        student = studentRepository.save(student);

        return studentMapper.toDTO(student);
    }

    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }
}
