package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import org.springframework.data.jpa.domain.Specification;

public class AssignmentSpecification {

    public static Specification<AssignmentEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
