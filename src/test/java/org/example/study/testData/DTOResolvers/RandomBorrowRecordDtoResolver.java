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

//TODO: work with it. Need to adjust everything for BorrowRecordDTO
// TestData provider probably does not know how to produce BorrowRecordResponseDto just yet
public class RandomBorrowRecordDtoResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return isAnnotatedWith(parameterContext, RandomBorrowRecordResponseDTO.class) &&
                isSuperOf(BorrowRecordResponseDto.class, parameterContext);
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        ValidStrategyFactory validStrategyFactory = getValidFactory(extensionContext);
        if (isAnnotatedWith(parameterContext, RandomBorrowRecordResponseDTO.class)) {
            return validStrategyFactory
                    .getValidDTOGenerationStrategy(PageStrategyType.RANDOM)
                    .generateValidObject(BorrowRecordResponseDto.class);
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }


}
