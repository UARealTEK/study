package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomBorrowRecordEntity;
import org.example.study.Annotations.RandomBorrowRecordResponseDTO;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.ValidStrategyFactory;
import org.example.study.enums.PageStrategyType;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.concurrent.ThreadLocalRandom;

public class RandomBorrowRecordEntityResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return isAnnotatedWith(parameterContext, RandomBorrowRecordEntity.class) &&
                isSuperOf(BorrowRecordEntity.class, parameterContext);
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        ValidStrategyFactory validStrategyFactory = getValidFactory(extensionContext);
        RandomBorrowRecordEntity annotation = parameterContext.getParameter().getAnnotation(RandomBorrowRecordEntity.class);
            boolean isReturned = annotation.isReturned();

            var borrowedAtDateTime = faker.timeAndDate().birthday().atStartOfDay();
            var returnedAtDateTime = borrowedAtDateTime.plusDays(ThreadLocalRandom.current().nextInt(1, 10));

            BorrowRecordEntity obj = validStrategyFactory
                    .getValidDTOGenerationStrategy(PageStrategyType.RANDOM)
                    .generateValidObject(BorrowRecordEntity.class);
            obj.setBorrowedAt(borrowedAtDateTime);

            if (isReturned) {
                obj.setReturnedAt(returnedAtDateTime);
            } else  {
                obj.setReturnedAt(null);
            }

            return obj;
        }
    }
