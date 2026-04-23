package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomBorrowRecordResponseDTO;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.ValidStrategyFactory;
import org.example.study.enums.PageStrategyType;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.concurrent.ThreadLocalRandom;

public class RandomBorrowRecordDtoResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return isAnnotatedWith(parameterContext, RandomBorrowRecordResponseDTO.class) &&
                isSuperOf(BorrowRecordResponseDto.class, parameterContext);
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        ValidStrategyFactory validStrategyFactory = getValidFactory(extensionContext);
            RandomBorrowRecordResponseDTO annotation = parameterContext.getParameter().getAnnotation(RandomBorrowRecordResponseDTO.class);
            boolean isReturned = annotation.isReturned();

            BorrowRecordResponseDto obj = validStrategyFactory
                    .getValidDTOGenerationStrategy(PageStrategyType.RANDOM)
                    .generateValidObject(BorrowRecordResponseDto.class);

            if (!isReturned) {
                obj.setReturnedAt(null);
            }

            return obj;
        }
    }
