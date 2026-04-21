package org.example.study;

import io.qameta.allure.*;
import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.Annotations.Smoke;
import org.example.study.BaseTestPages.BaseBorrowingServiceTest;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.testData.DTOResolvers.RandomBorrowRecordDtoResolver;
import org.example.study.testData.PageResolvers.RandomPageResponseDTOResolver;
import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Converters.BorrowRecordMapper;
import org.example.study.util.Converters.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

//todo: start working on it
@Smoke
@Epic("Library Management")
@Feature("Book Service Operations")
@ExtendWith({
        MockitoExtension.class,
        RandomPageResponseDTOResolver.class,
        RandomBorrowRecordDtoResolver.class
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
