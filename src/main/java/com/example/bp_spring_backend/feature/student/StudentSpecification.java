package com.example.bp_spring_backend.feature.student;

import com.example.bp_spring_backend.core.utils.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentSpecification {

    public static Specification<StudentEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<StudentEntity> hasAisId(Integer providedAisId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("aisId"), providedAisId);
    }

    public static Specification<StudentEntity> search(String searchQuery) {
        return (root, query, cb) -> {

            if (searchQuery == null || searchQuery.trim().isEmpty()) return cb.conjunction();

            String normalizedQuery = searchQuery.trim();

            String letters = normalizedQuery.replaceAll("[^\\p{L}\\s]", "").trim();
            String numbers = normalizedQuery.replaceAll("\\D+", "").trim();

            Expression<String> fullName = cb.function(
                    "unaccent",
                    String.class,
                    cb.lower(root.get("fullName"))
            );

            List<Predicate> predicates = new ArrayList<>();

            if (!letters.isEmpty()) {
                Arrays.stream(StringUtils.normalize(letters).split("\\s+"))
                        .map(word -> cb.like(fullName, "%" + word + "%"))
                        .forEach(predicates::add);
            }

            if (!numbers.isEmpty()) {
                predicates.add(cb.like(cb.toString(root.get("aisId")), numbers + "%"));
            }

            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
