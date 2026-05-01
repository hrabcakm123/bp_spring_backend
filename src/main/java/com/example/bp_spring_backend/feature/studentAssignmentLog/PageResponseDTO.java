package com.example.bp_spring_backend.feature.studentAssignmentLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO<T> {

    private List<T> data;
    private PageMetaDTO meta;
}
