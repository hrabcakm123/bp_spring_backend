package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import com.example.bp_spring_backend.domains.entity.UserEntity;
import com.example.bp_spring_backend.domains.entity.UserExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExerciseRepository extends JpaRepository<UserExerciseEntity, Integer>, JpaSpecificationExecutor<UserExerciseEntity> {

    @Query("SELECT ue.exerciseEntity FROM UserExerciseEntity ue WHERE ue.userEntity.id = :userId")
    List<ExerciseEntity> findExercisesByUserId(@Param("userId") Integer userId);

    @Query("SELECT ue.userEntity FROM UserExerciseEntity ue WHERE ue.exerciseEntity.id = :exerciseId")
    List<UserEntity> findUsersByExerciseId(@Param("exerciseId") Integer exerciseId);
}
