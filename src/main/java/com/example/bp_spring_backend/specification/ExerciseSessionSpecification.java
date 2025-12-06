package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.ExerciseSessionEntity;
import org.springframework.data.jpa.domain.Specification;

public class ExerciseSessionSpecification {

    public static Specification<ExerciseSessionEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
