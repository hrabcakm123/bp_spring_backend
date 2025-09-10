package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.BlockEntity;
import org.springframework.data.jpa.domain.Specification;

public class BlockSpecification {

    public static Specification<BlockEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
