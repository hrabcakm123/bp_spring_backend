package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentAssignmentSpecification {

    public static Specification<StudentAssignmentEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
