package org.example.study.Util;

import org.example.study.repository.BookRepository;
import org.example.study.service.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public abstract class BaseLibraryTest extends BaseTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    protected void init() {
        service = new BookService(repository, bookMapper);
    }

    protected void cleanUp() {
        service = null;
    }
}
