package org.example.study.StrategyEngine.interfaces;

import java.lang.reflect.Field;

/**
Invalidator that should support field invalidation based on the Jakarta constraint Annotations
 which are set on the given field
 */

//TODO: complete it!
public interface FieldInvalidator {
    boolean supports(Field field);
    void invalidate(Object obj, Field field);
}
