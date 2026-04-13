package org.example.study.util.Converters;

import org.example.study.enums.Gender;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Used to convert Request params (and other stuff which is not handled by Jackson converters)
 * into Gender variables
 */
@SuppressWarnings("unused")
@Component
public class GenderConverter implements Converter<String,Gender> {

    @Override
    public Gender convert(String source) {
        return Gender.valueOf(source.toUpperCase());
    }
}
