package org.example.study;

import io.qameta.allure.*;
import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.Annotations.Smoke;
import org.example.study.BaseTestPages.BaseBorrowingServiceTest;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

//todo: start working on it
@Smoke
@Epic("Library Management")
@Feature("Book Service Operations")
@ExtendWith({
        MockitoExtension.class
        //TODO: complete param resolvers here
})
public class BorrowLogicTests extends BaseBorrowingServiceTest {

    @Test
    @Story("Retrieve All records with pagination")
    @Description("Test aimed to check that service is able to fetch all available borrow records ")
    @Severity(SeverityLevel.CRITICAL)
    void checkGetAllBorrowRecords(@RandomPageResponseDto PageResponseDTO<BorrowRecordResponseDto> page) {
        //given
        //when
        //then
    }
}
