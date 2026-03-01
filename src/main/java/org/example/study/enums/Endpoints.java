package org.example.study.enums;

import lombok.Getter;

@Getter
public enum Endpoints {
    USERS("/users"),
    DUMMY_ENDPOINT("/dummy");

    private final String endpoint;

    Endpoints(String endpoint) {
        this.endpoint = endpoint;
    }
}
