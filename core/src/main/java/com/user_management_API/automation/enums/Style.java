package com.user_management_API.automation.enums;

public enum Style {


    /** The no style. */
    NO_STYLE("NO_STYLE");

    /** The value. */
    private String value;

    /**
     * Instantiates a new style.
     *
     * @param value the value
     */
    private Style(String value) {

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
