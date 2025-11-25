package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.outputDTO.BlockResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockManagerService {
    private final BlockService blockService;
    private final AssignmentService assignmentService;
    private final StudentAssignmentService studentAssignmentService;
    private final StudentAssignmentLogService studentAssignmentLogService;

    public BlockResponseDTO softDeleteBlockCascade(Integer blockId) {

        BlockResponseDTO response = blockService.deleteBlockById(blockId);

        List<Integer> assignmentIds = assignmentService.softDeleteAssignmentsByBlockId(blockId);

        List<Integer> studentAssignmentIds = new ArrayList<>();

        if (!assignmentIds.isEmpty()) {
            studentAssignmentIds = studentAssignmentService.softDeleteStudentAssignmentsByAssignmentIds(assignmentIds);
        }

        if (!studentAssignmentIds.isEmpty()) {
            studentAssignmentLogService.softDeleteStudentAssignmentLogsByStudentAssignmentIds(studentAssignmentIds);
        }

        return response;
    }
}
