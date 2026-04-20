package org.example.study;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.example.study.Annotations.RandomBorrowRecordEntity;
import org.example.study.Annotations.RandomBorrowRecordResponseDTO;
import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.Annotations.Smoke;
import org.example.study.BaseTestPages.BaseBorrowingControllerTest;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.testData.DTOResolvers.RandomBorrowRecordDtoResolver;
import org.example.study.testData.DTOResolvers.RandomBorrowRecordEntityResolver;
import org.example.study.testData.PageResolvers.RandomPageResponseDTOResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: work on it!
@Epic("Borrow Management")
@Feature("Borrowing CRUD Operations")
@Smoke
@ExtendWith({
        RandomBorrowRecordDtoResolver.class,
        RandomPageResponseDTOResolver.class,
        RandomBorrowRecordEntityResolver.class
})
public class CRUDBorrowControllerTests extends BaseBorrowingControllerTest {

    @Test
    @Story(
            "Get all borrow records"
    )
    @Description(
            "Should fetch all available borrowing records at the moment"
    )
    void checkGetAllBorrowRecords(@RandomPageResponseDto(totalElements = 5) PageResponseDTO<BorrowRecordResponseDto> borrowRecords) throws Exception {
        //given
        when(service.getAllBorrowRecords(any(Pageable.class))).thenReturn(borrowRecords);
        //when
        MvcResult result = mvc.perform(get("/borrows"))
                .andExpect(status().isOk()).andReturn();
        BorrowRecordResponseDto dto = mapper.readValue(result.getResponse().getContentAsString(), BorrowRecordResponseDto.class);
        //then

        verify(service).getAllBorrowRecords(any(Pageable.class));
        verifyNoMoreInteractions(service);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(borrowRecords);
    }

    @Test
    @Story(
            "Get Record by ID"
    )
    @Description(
            "Should fetch proper borrow record using provided ID"
    )
    void checkGetRecordById(@RandomBorrowRecordEntity(isReturned = false) BorrowRecordEntity entity) throws Exception {
        //given
        BorrowRecordResponseDto dto = borrowRecordMapper.toDto(entity);
        when(service.getRecordById(eq(entity.getId()))).thenReturn(dto);
        //when

        MvcResult result = mvc.perform(get("/borrows/" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book").exists())
                .andExpect(jsonPath("$.book.name").value(dto.getBook().getName()))
                .andExpect(jsonPath("$.book.author").value(dto.getBook().getAuthor()))
                .andExpect(jsonPath("$.userName").value(dto.getUserName()))
                .andExpect(jsonPath("$.borrowedAt").value(dto.getBorrowedAt().toString()))
                .andExpect(jsonPath("$.returnedAt").value(nullValue()))
                .andReturn();

        BorrowRecordResponseDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), BorrowRecordResponseDto.class);

        //then
        verify(service).getRecordById(eq(entity.getId()));
        verifyNoMoreInteractions(service);

        assertThat(resultDto).usingRecursiveComparison().isEqualTo(dto);
    }
}
