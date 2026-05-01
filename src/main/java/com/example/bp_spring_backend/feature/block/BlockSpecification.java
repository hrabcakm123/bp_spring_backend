package com.example.bp_spring_backend.feature.block;

import org.springframework.data.jpa.domain.Specification;

public class BlockSpecification {

    public static Specification<BlockEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
