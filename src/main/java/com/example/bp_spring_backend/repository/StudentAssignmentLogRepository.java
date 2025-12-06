package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAssignmentLogRepository extends JpaRepository<StudentAssignmentLogEntity, Integer> {

    @Modifying
    @Query("UPDATE StudentAssignmentLogEntity sal SET sal.isDeleted = true WHERE sal.originalUser.id = :userId")
    void softDeleteByOriginalUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE StudentAssignmentLogEntity sal SET sal.isDeleted = true WHERE sal.studentAssignment.id = :studentAssignmentId")
    void softDeleteByStudentAssignmentId(@Param("studentAssignmentId") Integer studentAssignmentId);

    @Modifying
    @Query("UPDATE StudentAssignmentLogEntity sal SET sal.isDeleted = true WHERE sal.studentAssignment.id IN :studentAssignmentIds")
    void softDeleteByStudentAssignmentIds(@Param("studentAssignmentIds") List<Integer> studentAssignmentIds);
}
