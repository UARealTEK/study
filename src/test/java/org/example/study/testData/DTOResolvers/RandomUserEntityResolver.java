package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomUserEntity;
import org.example.study.DTOs.Entities.UserEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static org.example.study.testData.TestData.getSingleValidEntity;

//TODO: look into it
public class RandomUserEntityResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(RandomUserEntity.class) && parameterContext.getParameter().getType().equals(UserEntity.class);
    }

    @Override
    public @Nullable UserEntity resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return getSingleValidEntity();
    }
}
