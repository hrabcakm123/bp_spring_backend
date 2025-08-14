package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.inputDTO.StudentRequestDTO;
import com.example.bp_spring_backend.domains.outputDTO.StudentResponseDTO;
import com.example.bp_spring_backend.exception.StudentNotFoundException;
import com.example.bp_spring_backend.mapper.StudentMapper;
import com.example.bp_spring_backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentResponseDTO> getAllStudents() {
        List<StudentEntity> students = studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public StudentResponseDTO getStudentById(Integer id) {
        StudentEntity student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(""));
        return studentMapper.toDTO(student);
    }

    public StudentResponseDTO getStudentByAisId(Integer aisId) {
        StudentEntity student = studentRepository.findByAisId(aisId).orElseThrow(() -> new StudentNotFoundException(""));
        return studentMapper.toDTO(student);
    }

    public StudentResponseDTO getStudentByEmail(String email) {
        StudentEntity student = studentRepository.findByEmail(email).orElseThrow(() -> new StudentNotFoundException(""));
        return studentMapper.toDTO(student);
    }

    public List<StudentResponseDTO> addStudents(List<StudentRequestDTO> request) {
        List<StudentEntity> students = request.stream()
                .map(studentMapper::toEntity)
                .toList();

        List<StudentEntity> savedStudents = studentRepository.saveAll(students);

        return savedStudents.stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public StudentResponseDTO deleteStudentById(Integer id) {
        StudentEntity student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(""));
        studentRepository.deleteById(id);
        return studentMapper.toDTO(student);
    }

    public StudentResponseDTO updateStudentById(Integer id, StudentRequestDTO request) {
        StudentEntity student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(""));

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
