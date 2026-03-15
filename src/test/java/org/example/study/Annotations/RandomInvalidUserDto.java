package org.example.study.Annotations;

import org.example.study.enums.UserDTOInvalidFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomInvalidUserDto {
    UserDTOInvalidFlag invalidFlag() default UserDTOInvalidFlag.RANDOM_INVALID;
}
