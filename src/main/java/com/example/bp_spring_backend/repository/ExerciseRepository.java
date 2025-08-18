package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Integer> {

}
