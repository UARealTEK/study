package org.example.study.util.Converters;

import org.example.study.DTOs.PageResponseDTO;
import org.springframework.data.domain.Page;

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
}
