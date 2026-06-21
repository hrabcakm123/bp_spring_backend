package com.example.bp_spring_backend.feature.student;

import com.example.bp_spring_backend.core.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    public StudentEntity getStudentEntityById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(""));
    }

    public List<StudentEntity> getStudentEntitiesByIds(List<Integer> ids) {
        return studentRepository.findAllById(ids);
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

    public void deleteStudentById(Integer id) {
        studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(""));
        studentRepository.deleteById(id);
    }

    public void updateStudentById(Integer id, StudentRequestDTO request) {
        log.info("Updating student id {}", id);
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(""));

        if(request.getAisId() != null) {
            student.setAisId(request.getAisId());
        }
        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }

        student = studentRepository.save(student);
        log.info("Saved student entity id {}", student.getId());
    }

    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }
}
