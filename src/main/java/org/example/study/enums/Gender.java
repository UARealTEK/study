package org.example.study.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.concurrent.ThreadLocalRandom;

public enum Gender {
    MALE,FEMALE;

    private static final Gender[] VALUES = Gender.values();

    @JsonCreator
    public static Gender toGender(String jsonGender) {
        if (jsonGender == null) {
            return null;
        }
        return Gender.valueOf(jsonGender.trim().toUpperCase());
    }

    public static Gender random() {
        return VALUES[ThreadLocalRandom.current().nextInt(VALUES.length)];
    }
}
