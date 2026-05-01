package com.example.bp_spring_backend.feature.exercise;

import org.springframework.data.jpa.domain.Specification;

public class ExerciseSpecification {

    public static Specification<ExerciseEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
