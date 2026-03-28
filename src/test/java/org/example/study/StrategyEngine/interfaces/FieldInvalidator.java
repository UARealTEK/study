package org.example.study.StrategyEngine.interfaces;

import java.lang.reflect.Field;

/**
Invalidator that should support field invalidation based on the Jakarta constraint

 Annotations which are set on the given field
 */

//TODO: complete it!
    // TODO: Think if I really need supports method ? Since Im already having a MAP in
public interface FieldInvalidator {
    void invalidate(Object obj, Field field) throws IllegalAccessException;
}
