package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Integer>, JpaSpecificationExecutor<AssignmentEntity> {

    @Modifying
    @Query("UPDATE AssignmentEntity a SET a.isDeleted = true WHERE a.blockEntity.id = :blockId")
    void softDeleteByBlockId(@Param("blockId") Integer blockId);

    @Query("SELECT a.id FROM AssignmentEntity a WHERE a.blockEntity.id = :blockId")
    List<Integer> findAssignmentIdsByBlockId(@Param("blockId") Integer blockId);
}
