package org.example.study.Annotations;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomInvalidUserDto {

    String fieldName() default "";
    Class<? extends Annotation> constraintToBreak() default NoConstraint.class;
}
