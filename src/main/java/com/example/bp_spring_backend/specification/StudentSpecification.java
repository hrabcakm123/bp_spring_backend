package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<StudentEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<StudentEntity> hasAisId(Integer providedAisId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("aisId"), providedAisId);
    }

    public static Specification<StudentEntity> containsEmail(String providedEmail) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + providedEmail.toLowerCase() + "%");
    }
}
