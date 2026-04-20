package org.example.study.enums;

import lombok.Getter;

/**
 * ENUM for testing purposes that holds endpoints for Controller testing
 * FYI -> this might be a redundant component but I've decided to have it anyway since it looks cleaner
 */
@Getter
public enum Endpoints {
    USERS("/users"),
    BORROWS("/borrows"),
    DUMMY_ENDPOINT("/dummy"); // for Testing purposes

    private final String endpoint;

    Endpoints(String endpoint) {
        this.endpoint = endpoint;
    }
}
