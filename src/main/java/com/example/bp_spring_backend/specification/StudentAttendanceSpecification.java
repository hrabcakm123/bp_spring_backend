package com.example.bp_spring_backend.specification;

import com.example.bp_spring_backend.domains.entity.StudentAttendanceEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentAttendanceSpecification {

    public static Specification<StudentAttendanceEntity> hasId(Integer providedId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("id"), providedId);
    }
}
