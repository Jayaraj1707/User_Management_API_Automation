package com.user_management_API.automation.enums;

public enum Color {

    /** The foreground red. */
    FOREGROUND_RED("\033[31m");

    /** The value. */
    private String value;

    /**
     * Instantiates a new color.
     *
     * @param value the value
     */
    private Color(String value) {

        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {

        return value;
    }
}
