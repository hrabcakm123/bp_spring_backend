package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.utils.StringUtils;
import jakarta.persistence.criteria.Expression;
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

    public static Specification<UserEntity> containsFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            Expression<String> dbValue = cb.function(
                    "unaccent",
                    String.class,
                    cb.lower(root.get("fullName"))
            );
            return cb.like(dbValue, "%" + StringUtils.normalize(fullName) + "%");
        };
    }
}
