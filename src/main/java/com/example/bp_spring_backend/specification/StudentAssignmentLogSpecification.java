package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class StudentAssignmentLogSpecification {

    public static Specification<StudentAssignmentLogEntity> containsOriginalUserFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            Join<StudentAssignmentLogEntity, UserEntity> originalUser = root.join("originalUser");
            return cb.like(cb.lower(originalUser.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<StudentAssignmentLogEntity> containsUpdatedByUserFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            Join<StudentAssignmentLogEntity, UserEntity> updatedByUser = root.join("updatedByUser");
            return cb.like(cb.lower(updatedByUser.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }
}
