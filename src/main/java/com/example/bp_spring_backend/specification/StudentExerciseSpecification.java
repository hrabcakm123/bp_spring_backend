package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentExerciseEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentExerciseSpecification {

    public static Specification<StudentExerciseEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
