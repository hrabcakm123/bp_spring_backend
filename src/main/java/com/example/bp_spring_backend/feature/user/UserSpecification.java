package com.example.bp_spring_backend.feature.user;

import com.example.bp_spring_backend.core.utils.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;

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
            return cb.and(Arrays.stream(StringUtils.normalize(fullName.trim()).split("\\s+"))
                    .map(part -> cb.like(dbValue, "%" + part + "%"))
                    .toArray(Predicate[]::new)
            );
        };
    }
}
