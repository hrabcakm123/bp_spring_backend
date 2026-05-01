package com.example.bp_spring_backend.feature.assignment;

import org.springframework.data.jpa.domain.Specification;

public class AssignmentSpecification {

    public static Specification<AssignmentEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<AssignmentEntity> hasBlockId(Integer blockId) {
        return (root, query, cb) ->
                cb.equal(root.get("blockEntity").get("id"), blockId);
    }
}
