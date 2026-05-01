package com.example.bp_spring_backend.feature.block;

import com.example.bp_spring_backend.feature.assignment.AssignmentService;
import com.example.bp_spring_backend.feature.studentAssignment.StudentAssignmentService;
import com.example.bp_spring_backend.feature.studentAssignmentLog.StudentAssignmentLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockManagerService {
    private final BlockService blockService;
    private final AssignmentService assignmentService;
    private final StudentAssignmentService studentAssignmentService;
    private final StudentAssignmentLogService studentAssignmentLogService;
    private static final Logger log = LoggerFactory.getLogger(BlockManagerService.class);

    @Transactional
    public void softDeleteBlockCascade(Integer blockId) {
        log.info("Starting soft delete cascade for block id {}", blockId);
        blockService.deleteBlockById(blockId);
        log.info("Soft deleted block entity");
        List<Integer> assignmentIds = assignmentService.softDeleteAssignmentsByBlockId(blockId);
        log.info("Soft deleted {} assignments for block id {}", assignmentIds.size(), blockId);
        List<Integer> studentAssignmentIds = new ArrayList<>();

        if (!assignmentIds.isEmpty()) {
            studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByAssignmentIds(assignmentIds);
            log.info("Soft deleted {} student assignments for block id {}", studentAssignmentIds.size(), blockId);
        } else {
            log.info("No assignments found to soft delete for block id {}", blockId);
        }

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
            log.info("Soft deleted student assignment logs for {} student assignments", studentAssignmentIds.size());
        } else {
            log.info("No student assignment logs to soft delete for block id {}", blockId);
        }
        log.info("Completed soft delete cascade for block id {}", blockId);
    }
}
