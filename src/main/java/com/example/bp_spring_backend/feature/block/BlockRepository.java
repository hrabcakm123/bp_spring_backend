package com.example.bp_spring_backend.feature.block;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Integer>, JpaSpecificationExecutor<BlockEntity> {

}
