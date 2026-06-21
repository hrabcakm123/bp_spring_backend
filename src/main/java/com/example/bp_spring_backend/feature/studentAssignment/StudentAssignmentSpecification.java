package com.example.bp_spring_backend.feature.studentAssignment;

import org.springframework.data.jpa.domain.Specification;

public class StudentAssignmentSpecification {

    public static Specification<StudentAssignmentEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
