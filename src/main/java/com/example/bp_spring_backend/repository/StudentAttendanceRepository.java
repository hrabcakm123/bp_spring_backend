package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendanceEntity, Integer>, JpaSpecificationExecutor<StudentAttendanceEntity> {

}
