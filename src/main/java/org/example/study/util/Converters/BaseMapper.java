package org.example.study.util.Converters;

import org.example.study.DTOs.PageResponseDTO;
import org.springframework.data.domain.Page;

import java.util.function.Function;

//TODO: make sure that my mappers are consistent
// Use SPRING everywhere instead of working separately with Spring / static instances
@SuppressWarnings("unused")
public interface BaseMapper {
    default <T> PageResponseDTO<T> toPageResponse(Page<T> page){
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    default <T,U> PageResponseDTO<T> convertData(PageResponseDTO<U> pageResponseDTO, Function<U,T> mapper){
        return new PageResponseDTO<>(
                pageResponseDTO.content().stream().map(mapper).toList(),
                pageResponseDTO.number(),
                pageResponseDTO.size(),
                pageResponseDTO.totalElements(),
                pageResponseDTO.totalPages()
        );
    }
}
