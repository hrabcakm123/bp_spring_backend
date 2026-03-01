package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.entity.StudentEntity;
import com.example.bp_spring_backend.domains.entity.StudentExerciseEntity;
import com.example.bp_spring_backend.utils.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class StudentExerciseSpecification {

    public static Specification<StudentExerciseEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<StudentExerciseEntity> containsStudentFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            Join<StudentExerciseEntity, StudentEntity> student = root.join("studentEntity");
            Expression<String> dbValue = cb.function(
                    "unaccent",
                    String.class,
                    cb.lower(student.get("fullName"))
            );
            return cb.like(dbValue, "%" + StringUtils.normalize(fullName) + "%");
        };
    }

    public static Specification<StudentExerciseEntity> containsStudentAisId(String aisId) {
        return (root, query, cb) -> {
            if (aisId == null || aisId.trim().isEmpty()) {
                return cb.conjunction();
            }
            Join<StudentExerciseEntity, StudentEntity> student = root.join("studentEntity");
            return cb.like(cb.toString(student.get("aisId")), "%" + aisId + "%");
        };
    }

    public static Specification<StudentExerciseEntity> hasExerciseId(Integer exerciseId) {
        return (root, query, cb) -> {
            if (exerciseId == null) {
                return cb.conjunction();
            }
            Join<StudentExerciseEntity, ExerciseEntity> exercise = root.join("exerciseEntity");
            return cb.equal(exercise.get("id"), exerciseId);
        };
    }
}
