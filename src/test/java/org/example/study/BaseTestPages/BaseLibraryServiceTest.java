package org.example.study.BaseTestPages;

import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.repository.BookRepository;
import org.example.study.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;


public abstract class BaseLibraryServiceTest extends BaseTest {

    @Mock
    protected BookRepository repository;
    protected BookService service;
    @Captor
    protected ArgumentCaptor<Specification<BookEntity>> bookEntitySpecCaptor;

    @BeforeEach
    protected void init() {
        service = new BookService(repository, bookMapper);
    }

    protected void cleanUp() {
        service = null;
        repository = null;
    }
}
