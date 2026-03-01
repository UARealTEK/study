package org.example.study.Annotations;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Tag("smoke")
public @interface Smoke {
}
