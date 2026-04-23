package org.example.study.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public abstract class BaseService {

    protected Pageable normalizePageable(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    }

}
