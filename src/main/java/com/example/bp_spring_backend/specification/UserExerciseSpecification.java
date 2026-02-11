package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.UserExerciseEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserExerciseSpecification {

    public static Specification<UserExerciseEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<UserExerciseEntity> hasUserId(Integer userId) {
        return (root, query, cb) ->
                userId == null
                        ? cb.conjunction()
                        : cb.equal(root.get("userEntity").get("id"), userId);
    }
}
