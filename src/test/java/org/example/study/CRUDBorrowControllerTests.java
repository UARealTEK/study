package org.example.study;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.example.study.Annotations.*;
import org.example.study.BaseTestPages.BaseBorrowingControllerTest;
import org.example.study.DTOs.BorrowRecordRequestDto;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.enums.Endpoints;
import org.example.study.testData.DTOResolvers.RandomBorrowRecordDtoResolver;
import org.example.study.testData.DTOResolvers.RandomBorrowRecordEntityResolver;
import org.example.study.testData.PageResolvers.RandomPageResponseDTOResolver;
import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Converters.BorrowRecordMapper;
import org.example.study.util.Converters.UserMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: adapt tests to ResponseEntity (since Im returning it in Controller instead of raw DTO)
@Epic("Borrow Management")
@Feature("Borrowing CRUD Operations")
@Smoke
@ExtendWith({
        RandomBorrowRecordDtoResolver.class,
        RandomPageResponseDTOResolver.class,
        RandomBorrowRecordEntityResolver.class
})
@Import({
        UserMapper.class,
        BookMapper.class,
        BorrowRecordMapper.class
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
        when(borrowService.getAllBorrowRecords(any(Pageable.class))).thenReturn(borrowRecords);
        //when
        MvcResult result = mvc.perform(get(Endpoints.BORROWS.getEndpoint()))
                .andExpect(status().isOk()).andReturn();
        BorrowRecordResponseDto dto = mapper.readValue(result.getResponse().getContentAsString(), BorrowRecordResponseDto.class);
        //then

        verify(borrowService).getAllBorrowRecords(any(Pageable.class));
        verifyNoMoreInteractions(borrowService);

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
        when(borrowService.getRecordById(eq(entity.getId()))).thenReturn(dto);
        //when

        MvcResult result = mvc.perform(get(Endpoints.BORROWS.getEndpoint() + entity.getId()))
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
        verify(borrowService).getRecordById(eq(entity.getId()));
        verifyNoMoreInteractions(borrowService);

        assertThat(resultDto).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    @Story(
            "Book borrowing logic"
    )
    @Description(
            "Should successfully borrow an existing book for an existing user"
    )
    void checkBorrowBook(@RandomBookEntity BookEntity book,
                         @RandomUserEntity UserEntity user,
                         @RandomBorrowRecordResponseDTO(isReturned = false) BorrowRecordResponseDto borrowRecord
    ) throws Exception {
        //given
        BorrowRecordRequestDto requestDto = new BorrowRecordRequestDto(book.getId(), user.getId());
        when(borrowService.borrowBook(eq(requestDto.bookId()), eq(requestDto.userId()))).thenReturn(borrowRecord);
        //when
        MvcResult result = mvc.perform(
                post(Endpoints.BORROWS.getEndpoint())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.endsWith("/borrows/" + borrowRecord.getId())))
                .andExpect(jsonPath("$.id").value(borrowRecord.getId()))
                .andExpect(jsonPath("$.book.name").value(borrowRecord.getBook().getName()))
                .andExpect(jsonPath("$.userName").value(borrowRecord.getUserName()))
                .andExpect(jsonPath("$.borrowedAt").value(borrowRecord.getBorrowedAt().toString()))
                .andExpect(jsonPath("$.returnedAt").value(nullValue()))
                .andReturn();
        BorrowRecordResponseDto responseDto = mapper.readValue(result.getResponse().getContentAsString(), BorrowRecordResponseDto.class);
        //then
        assertThat(responseDto)
                .usingRecursiveComparison()
                .isEqualTo(borrowRecord);
        verify(borrowService).borrowBook(eq(requestDto.bookId()), eq(requestDto.userId()));
        verifyNoMoreInteractions(borrowService);
    }

    @Test
    @Story(
            "Book borrowing logic"
    )
    @Description(
            "Should successfully returned an existing book that is borrowed by an existing user"
    )
    void checkReturnBook(@RandomBookEntity BookEntity book,
                         @RandomUserEntity UserEntity user,
            @RandomBorrowRecordResponseDTO(isReturned = true) BorrowRecordResponseDto borrowRecord) throws Exception {
        //given
        BorrowRecordRequestDto requestDto = new BorrowRecordRequestDto(book.getId(), user.getId());
        when(borrowService.returnBook(eq(requestDto.bookId()),eq(requestDto.userId()))).thenReturn(borrowRecord);
        //when

        MvcResult result = mvc.perform(patch(Endpoints.BORROWS.getEndpoint())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.name").value(borrowRecord.getBook().getName()))
                .andExpect(jsonPath("$.userName").value(borrowRecord.getUserName()))
                .andExpect(jsonPath("$.borrowedAt").value(borrowRecord.getBorrowedAt().toString()))
                .andExpect(jsonPath("$.returnedAt").value(borrowRecord.getReturnedAt().toString()))
                .andReturn();

        BorrowRecordResponseDto responseDto = mapper.readValue(result.getResponse().getContentAsString(), BorrowRecordResponseDto.class);
        //then
        verify(borrowService).returnBook(eq(requestDto.bookId()), eq(requestDto.userId()));
        verifyNoMoreInteractions(borrowService);
        assertThat(responseDto).usingRecursiveComparison().isEqualTo(borrowRecord);
    }
}
