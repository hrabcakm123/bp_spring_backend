package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Integer>, JpaSpecificationExecutor<BlockEntity> {

}
