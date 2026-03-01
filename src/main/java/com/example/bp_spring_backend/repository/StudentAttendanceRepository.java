package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentAttendanceEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendanceEntity, Integer>, JpaSpecificationExecutor<StudentAttendanceEntity> {

    List<StudentAttendanceEntity> findByStudentEntity_IdOrderByExerciseSessionEntity_SessionDateDesc(Integer studentId);

    @Modifying
    @Query("UPDATE StudentAttendanceEntity sa SET sa.isDeleted = true WHERE sa.createdBy.id = :userId OR sa.updatedBy.id = :userId")
    void softDeleteByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE StudentAttendanceEntity sa SET sa.isDeleted = true WHERE sa.studentEntity.id = :studentId")
    void softDeleteByStudentId(@Param("studentId") Integer studentId);

    @Modifying
    @Query("UPDATE StudentAttendanceEntity sa SET sa.isDeleted = true WHERE sa.exerciseSessionEntity.id = :exerciseSessionId")
    void softDeleteByExerciseSessionId(@Param("exerciseSessionId") Integer exerciseSessionId);

    @Modifying
    @Query("UPDATE StudentAttendanceEntity sa SET sa.isDeleted = true WHERE sa.exerciseSessionEntity.id IN :exerciseSessionIds")
    void softDeleteByExerciseSessionIds(@Param("exerciseSessionIds") List<Integer> exerciseSessionIds);

    @Query("SELECT sa FROM StudentAttendanceEntity sa JOIN FETCH sa.studentEntity s JOIN FETCH sa.exerciseSessionEntity es WHERE es.exerciseEntity.id = :exerciseId ORDER BY es.sessionDate ASC")
    List<StudentAttendanceEntity> findStudentAttendancesByExerciseId(@Param("exerciseId") Integer exerciseId, Sort sort);

    @Query("SELECT sa FROM StudentAttendanceEntity sa JOIN FETCH sa.studentEntity s JOIN FETCH sa.exerciseSessionEntity es WHERE s.id = :studentId ORDER BY es.sessionDate ASC")
    List<StudentAttendanceEntity> findStudentAttendancesByStudentId(@Param("studentId") Integer studentId, Sort sort);
}
