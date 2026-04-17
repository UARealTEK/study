package org.example.study;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.Annotations.Smoke;
import org.example.study.BaseTestPages.BaseBorrowingControllerTest;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.testData.PageResolvers.RandomPageResponseDTOResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//TODO: work on it!
@Epic("Borrow Management")
@Feature("Borrowing CRUD Operations")
@Smoke
@ExtendWith(
        RandomPageResponseDTOResolver.class
)
public class CRUDBorrowControllerTests extends BaseBorrowingControllerTest {

    @Test
    @Story(
            "Get all borrow records"
    )
    @Description(
            "Should fetch all available borrowing records at the moment"
    )
    void checkGetAllBorrowRecords(@RandomPageResponseDto PageResponseDTO<BorrowRecordResponseDto> borrowRecords) {
        //given
        when(service.getAllBorrowRecords(any(Pageable.class))).thenReturn(borrowRecords);
        //when
        //then
    }
}
