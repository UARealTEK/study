package org.example.study.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,FEMALE;

    @JsonCreator
    public static Gender toGender(String jsonGender) {
        return Gender.valueOf(jsonGender.toUpperCase());
    }
}
