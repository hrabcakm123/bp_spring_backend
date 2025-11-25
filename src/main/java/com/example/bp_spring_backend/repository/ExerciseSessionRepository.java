package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.ExerciseSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseSessionRepository extends JpaRepository<ExerciseSessionEntity, Integer>, JpaSpecificationExecutor<ExerciseSessionEntity> {

    List<ExerciseSessionEntity> findByExerciseEntityIdOrderBySessionDateDesc(Integer exerciseId);

    @Modifying
    @Query("UPDATE ExerciseSessionEntity s SET s.isDeleted = true WHERE s.createdBy.id = :userId")
    void softDeleteByCreatedByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE ExerciseSessionEntity s SET s.isDeleted = true WHERE s.exerciseEntity.id = :exerciseId")
    void softDeleteByExerciseId(@Param("exerciseId") Integer exerciseId);

    @Query("SELECT es.id FROM ExerciseSessionEntity es WHERE es.exerciseEntity.id = :exerciseId")
    List<Integer> findExerciseSessionIdsByExerciseId(@Param("exerciseId") Integer exerciseId);

    @Query("SELECT es.id FROM ExerciseSessionEntity es WHERE es.createdBy.id = :userId")
    List<Integer> findExerciseSessionIdsByCreatedByUserId(@Param("userId") Integer userId);
}
