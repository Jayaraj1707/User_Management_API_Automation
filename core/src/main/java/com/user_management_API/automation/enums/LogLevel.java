package com.user_management_API.automation.enums;

public enum LogLevel {

    LOW(0), MEDIUM(1), HIGH(2);

    /**
     * The value.
     */
    private final int value;

    /**
     * Instantiates a new custom log level.
     *
     * @param value the value
     */
    private LogLevel(final int value) {

        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {

        return value;
    }
}
