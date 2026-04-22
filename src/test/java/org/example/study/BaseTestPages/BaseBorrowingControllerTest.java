package org.example.study.BaseTestPages;

import org.example.study.controller.BorrowController;

import org.example.study.service.BookService;
import org.example.study.service.BorrowService;
import org.example.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BorrowController.class)
public abstract class BaseBorrowingControllerTest extends BaseControllerTest {

    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected BorrowService borrowService;
    @MockitoBean
    protected BookService bookService;
    @MockitoBean
    protected UserService userService;

}
