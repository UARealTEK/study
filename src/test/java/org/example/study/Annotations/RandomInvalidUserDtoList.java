package org.example.study.Annotations;

import org.example.study.enums.PageStrategyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomInvalidUserDtoList {

    int count() default 5; // Default count of UserDto objects to generate
}
