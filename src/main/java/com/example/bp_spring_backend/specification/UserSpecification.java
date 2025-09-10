package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<UserEntity> containsEmail(String providedEmail) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + providedEmail.toLowerCase() + "%");
    }
}
