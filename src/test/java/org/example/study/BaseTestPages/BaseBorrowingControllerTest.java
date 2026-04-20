package org.example.study.BaseTestPages;

import org.example.study.controller.BorrowController;

import org.example.study.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BorrowController.class)
public abstract class BaseBorrowingControllerTest extends BaseTest {

    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected BorrowService service;
}
