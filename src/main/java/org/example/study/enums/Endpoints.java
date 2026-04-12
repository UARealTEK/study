package org.example.study.enums;

import lombok.Getter;

//TODO: need to look into it to make sure I reuse it both in controllers AND in tests
// Also need to make sure I use different endpoint enum classes for different controllers
@Getter
public enum Endpoints {
    USERS("/users"),
    DUMMY_ENDPOINT("/dummy"); // for Testing purposes

    private final String endpoint;

    Endpoints(String endpoint) {
        this.endpoint = endpoint;
    }
}
