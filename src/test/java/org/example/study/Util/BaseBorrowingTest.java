
package org.example.study.Util;

import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.repository.BorrowRecordsRepository;
import org.example.study.service.BookService;
import org.example.study.service.BorrowService;
import org.example.study.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;

public abstract class BaseBorrowingTest extends BaseTest {

    @Mock
    protected BorrowRecordsRepository repository;
    @Mock
    protected BookService bookService;
    @Mock
    protected UserService userService;
    protected BorrowService service;
    @Captor
    protected ArgumentCaptor<Specification<BorrowRecordEntity>> borrowEntitySpecCaptor;

    @BeforeEach
    protected void init() {
        service = new BorrowService(bookService, userService,repository,borrowRecordMapper);
    }

    protected void cleanUp() {
        service = null;
    }
}
