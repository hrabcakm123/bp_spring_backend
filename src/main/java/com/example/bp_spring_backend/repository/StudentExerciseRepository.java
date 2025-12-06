package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentExerciseRepository extends JpaRepository<StudentExerciseEntity, Integer>, JpaSpecificationExecutor<StudentExerciseEntity> {

    @Modifying
    @Query("UPDATE StudentExerciseEntity s SET s.isDeleted = true WHERE s.studentEntity.id = :studentId")
    void softDeleteByStudentId(@Param("studentId") Integer studentId);

    @Modifying
    @Query("UPDATE StudentExerciseEntity s SET s.isDeleted = true WHERE s.exerciseEntity.id = :exerciseId")
    void softDeleteByExerciseId(@Param("exerciseId") Integer exerciseId);
}
