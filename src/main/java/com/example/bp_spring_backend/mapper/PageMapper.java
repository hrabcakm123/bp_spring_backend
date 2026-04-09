package com.example.bp_spring_backend.mapper;

import com.example.bp_spring_backend.domains.outputDTO.PageMetaDTO;
import com.example.bp_spring_backend.domains.outputDTO.PageResponseDTO;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PageMapper {

    private PageMapper() {}

    public static <E, D> PageResponseDTO<D> toResponse(Page<E> page, Function<E, D> mapper) {
        Page<D> dtoPage = page.map(mapper);

        return PageResponseDTO.<D>builder()
                .data(dtoPage.getContent())
                .meta(PageMetaDTO.builder()
                        .page(dtoPage.getNumber())
                        .totalPages(dtoPage.getTotalPages())
                        .build())
                .build();
    }
}
