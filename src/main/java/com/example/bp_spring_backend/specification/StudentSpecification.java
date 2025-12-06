package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
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
        return (root, query, criteriaBuilder) -> {

            if (searchQuery == null || searchQuery.trim().isEmpty()) return criteriaBuilder.conjunction();

            String normalizedQuery = searchQuery.trim();

            String letters = normalizedQuery.replaceAll("[^a-zA-Zá-žá-Ž\\s]", "").trim();
            String numbers = normalizedQuery.replaceAll("\\D+", "").trim();

            List<Predicate> predicates = new ArrayList<>();

            if (!letters.isEmpty()) {
                String pattern = "%" + letters.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), pattern));
            }

            if (!numbers.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.toString(root.get("aisId")), numbers + "%"));
            }

            if (predicates.size() == 2) return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

            return predicates.get(0);
        };
    }
}
