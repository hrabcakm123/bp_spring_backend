package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAssignmentRepository extends JpaRepository<StudentAssignmentEntity, Integer>, JpaSpecificationExecutor<StudentAssignmentEntity> {

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.createdBy.id = :userId")
    void softDeleteByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.studentEntity.id = :studentId")
    void softDeleteByStudentId(@Param("studentId") Integer studentId);

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.assignmentEntity.id = :assignmentId")
    void softDeleteByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.assignmentEntity.id = :assignmentId")
    List<Integer> findStudentAssignmentIdsByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.studentEntity.id = :userId")
    List<Integer> findStudentAssignmentIdsByStudentEntityUserId(@Param("userId") Integer userId);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.studentEntity.id = :studentId")
    List<Integer> findStudentAssignmentIdsByStudentEntityId(@Param("studentId") Integer studentId);

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.assignmentEntity.id IN :assignmentIds")
    void softDeleteByAssignmentIds(@Param("assignmentIds") List<Integer> assignmentIds);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.assignmentEntity.id IN :assignmentIds")
    List<Integer> findStudentAssignmentIdsByAssignmentIds(@Param("assignmentIds") List<Integer> assignmentIds);
}
