package org.example.study.Util;

import org.example.study.repository.BookRepository;
import org.example.study.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public abstract class BaseLibraryServiceTest extends BaseTest {

    @Mock
    protected BookRepository repository;
    protected BookService service;

    @BeforeEach
    protected void init() {
        service = new BookService(repository, bookMapper);
    }

    protected void cleanUp() {
        service = null;
    }
}
