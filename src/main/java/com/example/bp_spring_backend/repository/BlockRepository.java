package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Integer> {

    Optional<BlockEntity> findByName(String name);
}
