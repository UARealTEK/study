package org.example.study;

import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.Util.BaseLibraryServiceTest;
import org.example.study.testData.PageResolvers.RandomPageImplResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;

//TODO: work on it
@ExtendWith(
        RandomPageImplResolver.class
)
public class LibraryServiceTests extends BaseLibraryServiceTest {

    @Test
    void checkGetAllBooks(@RandomPageImplObj Page<BookEntity> entityPage) {
        //given
        //when
        //then
    }
}
