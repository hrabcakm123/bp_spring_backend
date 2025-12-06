package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import org.springframework.data.jpa.domain.Specification;

public class ExerciseSpecification {

    public static Specification<ExerciseEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
