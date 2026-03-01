package com.example.bp_spring_backend.repository;

import com.example.bp_spring_backend.domains.entity.StudentAssignmentEntity;
import org.springframework.data.domain.Sort;
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
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.createdBy.id = :userId OR sa.updatedBy.id = :userId")
    void softDeleteByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.studentEntity.id = :studentId")
    void softDeleteByStudentId(@Param("studentId") Integer studentId);

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.assignmentEntity.id = :assignmentId")
    void softDeleteByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.assignmentEntity.id = :assignmentId")
    List<Integer> findStudentAssignmentIdsByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.createdBy.id = :userId OR sa.updatedBy.id = :userId")
    List<Integer> findStudentAssignmentIdsByCreatedOrUpdatedByUserId(@Param("userId") Integer userId);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.studentEntity.id = :studentId")
    List<Integer> findStudentAssignmentIdsByStudentEntityId(@Param("studentId") Integer studentId);

    @Modifying
    @Query("UPDATE StudentAssignmentEntity sa SET sa.isDeleted = true WHERE sa.assignmentEntity.id IN :assignmentIds")
    void softDeleteByAssignmentIds(@Param("assignmentIds") List<Integer> assignmentIds);

    @Query("SELECT sa.id FROM StudentAssignmentEntity sa WHERE sa.assignmentEntity.id IN :assignmentIds")
    List<Integer> findStudentAssignmentIdsByAssignmentIds(@Param("assignmentIds") List<Integer> assignmentIds);

    @Query("SELECT sa FROM StudentAssignmentEntity sa JOIN FETCH sa.studentEntity s JOIN FETCH sa.assignmentEntity a JOIN StudentExerciseEntity se ON se.studentEntity = s WHERE a.blockEntity.id = :blockId AND se.exerciseEntity.id = :exerciseId ORDER BY sa.assignmentEntity.name ASC")
    List<StudentAssignmentEntity> findStudentAssignmentsByBlockIdAndExerciseId(@Param("blockId") Integer blockId, @Param("exerciseId") Integer exerciseId, Sort sort);

    @Query(value = "SELECT sa.* FROM student_assignments sa JOIN students s ON s.id = sa.student_id JOIN assignments a ON a.id = sa.assignment_id JOIN student_exercises se ON se.student_id = s.id WHERE a.block_id = :blockId AND se.exercise_id = :exerciseId AND LOWER(unaccent(s.full_name)) LIKE LOWER(unaccent(CONCAT('%', :fullName, '%'))) AND sa.is_deleted = false ORDER BY a.name", nativeQuery = true)
    List<StudentAssignmentEntity> findStudentAssignmentsByBlockIdAndExerciseIdAndStudentFullName(@Param("blockId") Integer blockId, @Param("exerciseId") Integer exerciseId, @Param("fullName") String fullName);

    @Query("SELECT sa FROM StudentAssignmentEntity sa JOIN FETCH sa.studentEntity s JOIN FETCH sa.assignmentEntity a WHERE a.blockEntity.id = :blockId AND s.id = :studentId ORDER BY sa.assignmentEntity.name ASC")
    List<StudentAssignmentEntity> findStudentAssignmentsByBlockIdAndStudentId(@Param("blockId") Integer blockId, @Param("studentId") Integer studentId);

    @Query(value = """
    SELECT 
        s.id AS student_id,
        s.full_name AS student_full_name,
        s.ais_id AS ais_id,
        b.id AS block_id,
        SUM(sa.earned_points) AS block_points
    FROM student_assignments sa
    JOIN students s ON s.id = sa.student_id
    JOIN assignments a ON a.id = sa.assignment_id
    JOIN blocks b ON b.id = a.block_id
    JOIN student_exercises se ON se.student_id = s.id
    WHERE sa.is_deleted = false
      AND (:exerciseId IS NULL OR se.exercise_id = :exerciseId)
      AND (:studentFullName IS NULL OR LOWER(unaccent(s.full_name)) LIKE LOWER(unaccent(CONCAT('%', :studentFullName, '%'))))
    GROUP BY s.id, s.full_name, s.ais_id, b.id
    ORDER BY s.full_name, b.id
    """, nativeQuery = true)
    List<StudentAssignmentBlockPointsProjection> getStudentAssignmentBlockPointsByExerciseId(
            @Param("exerciseId") Integer exerciseId,
            @Param("studentFullName") String studentFullName
    );

    @Query(value = """
    SELECT 
        s.id AS student_id,
        s.full_name AS student_full_name,
        s.ais_id AS ais_id,
        b.id AS block_id,
        SUM(sa.earned_points) AS block_points
    FROM student_assignments sa
    JOIN students s ON s.id = sa.student_id
    JOIN assignments a ON a.id = sa.assignment_id
    JOIN blocks b ON b.id = a.block_id
    WHERE sa.is_deleted = false
      AND s.id = :studentId
    GROUP BY s.id, s.full_name, s.ais_id, b.id
    ORDER BY b.id
    """, nativeQuery = true)
    List<StudentAssignmentBlockPointsProjection> getStudentAssignmentBlockPointsByStudentId(
            @Param("studentId") Integer studentId
    );

}
