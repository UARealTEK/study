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
// make sure Im correctly working with my annotation markers
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
            RandomBorrowRecordResponseDTO annotation = parameterContext.getParameter().getAnnotation(RandomBorrowRecordResponseDTO.class);
            boolean isBorrowed = annotation.isBorrowed();
            boolean isReturned = annotation.isReturned();

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
