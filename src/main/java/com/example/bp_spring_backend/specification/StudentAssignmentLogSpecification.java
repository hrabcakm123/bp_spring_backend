package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.utils.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

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
            return cb.like(dbValue, "%" + StringUtils.normalize(fullName) + "%");
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
            return cb.like(dbValue, "%" + StringUtils.normalize(fullName) + "%");
        };
    }
}
