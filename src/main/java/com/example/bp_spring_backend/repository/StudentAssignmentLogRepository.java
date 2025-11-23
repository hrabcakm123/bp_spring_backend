package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAssignmentLogRepository extends JpaRepository<StudentAssignmentLogEntity, Integer> {

}
