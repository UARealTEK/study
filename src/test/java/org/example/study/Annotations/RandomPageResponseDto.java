package org.example.study.Annotations;

import org.example.study.enums.PageStrategyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@BaseParameterResolverAnnotation
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface RandomPageResponseDto {
    int size() default 20;
    int page() default 0;
    int totalElements() default 0;
    PageStrategyType strategy() default PageStrategyType.RANDOM;
}
