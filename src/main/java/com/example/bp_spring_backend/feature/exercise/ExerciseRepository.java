package com.example.bp_spring_backend.feature.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Integer>, JpaSpecificationExecutor<ExerciseEntity> {

    @Query("""
    SELECT 
        e.id AS id,
        e.firstSessionDate AS firstSessionDate,
        e.startTime AS startTime,
        e.roomEnum AS roomEnum,
        STRING_AGG(u.fullName, ' & ') AS leaderFullName
    FROM ExerciseEntity e
    LEFT JOIN UserExerciseEntity ue 
        ON ue.exerciseEntity = e
        AND ue.isDeleted = false
    LEFT JOIN UserEntity u 
        ON u = ue.userEntity
        AND (u.roleEnum = com.example.bp_spring_backend.feature.enums.RoleEnum.ADMIN
             OR u.roleEnum = com.example.bp_spring_backend.feature.enums.RoleEnum.TEACHER)
    GROUP BY 
        e.id, e.firstSessionDate, e.startTime, e.roomEnum
    ORDER BY 
        e.firstSessionDate ASC,
        e.startTime ASC,
        e.roomEnum ASC
""")
    List<ExerciseLeaderProjection> findExercisesWithLeader();
}
