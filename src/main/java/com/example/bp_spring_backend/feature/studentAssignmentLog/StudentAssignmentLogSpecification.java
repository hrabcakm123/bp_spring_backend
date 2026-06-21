package com.example.bp_spring_backend.feature.studentAssignmentLog;

import com.example.bp_spring_backend.feature.user.UserEntity;
import com.example.bp_spring_backend.core.utils.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;

public class StudentAssignmentLogSpecification {

    public static Specification<StudentAssignmentLogEntity> containsOriginalUserFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            Join<StudentAssignmentLogEntity, UserEntity> originalUser = root.join("originalUser");
            Expression<String> dbValue = cb.function(
                    "unaccent",
                    String.class,
                    cb.lower(originalUser.get("fullName"))
            );
            return cb.and(Arrays.stream(StringUtils.normalize(fullName.trim()).split("\\s+"))
                    .map(part -> cb.like(dbValue, "%" + part + "%"))
                    .toArray(Predicate[]::new)
            );
        };
    }

    public static Specification<StudentAssignmentLogEntity> containsUpdatedByUserFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            Join<StudentAssignmentLogEntity, UserEntity> updatedByUser = root.join("updatedByUser");
            Expression<String> dbValue = cb.function(
                    "unaccent",
                    String.class,
                    cb.lower(updatedByUser.get("fullName"))
            );
            return cb.and(Arrays.stream(StringUtils.normalize(fullName.trim()).split("\\s+"))
                    .map(part -> cb.like(dbValue, "%" + part + "%"))
                    .toArray(Predicate[]::new)
            );
        };
    }
}
