package com.example.bp_spring_backend.feature.exerciseSession;

import org.springframework.data.jpa.domain.Specification;

public class ExerciseSessionSpecification {

    public static Specification<ExerciseSessionEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
