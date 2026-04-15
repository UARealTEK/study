package org.example.study.BaseTestPages;

import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.FieldInvalidatorRegistry;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.StrategyFactory;
import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;
import org.example.study.repository.BookRepository;
import org.example.study.service.BookService;
import org.example.study.testData.DTOResolvers.RandomBookDtoResolver;
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
    protected RandomBookDtoResolver resolver;
    protected StrategyFactory validFactory;
    protected StrategyFactory invalidFactory;
    protected FieldInvalidationService invalidationService;
    protected FieldInvalidatorRegistry registry;

    @BeforeEach
    protected void init() {
        service = new BookService(repository, bookMapper);
        registry = new FieldInvalidatorRegistry();
        invalidationService = new FieldInvalidationService(registry);
        validFactory = new StrategyFactory();
        invalidFactory = new StrategyFactory(invalidationService);
    }

    protected void cleanUp() {
        service = null;
    }
}
