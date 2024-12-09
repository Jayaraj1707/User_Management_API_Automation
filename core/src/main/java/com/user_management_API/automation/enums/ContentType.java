package com.user_management_API.automation.enums;

public enum ContentType {

    /**
     * The json.
     */
    JSON("application/json"),

    /**
     * The xml.
     */
    XML("application/xml"),

    /**
     * The text.
     */
    TEXT("text/xml"),

    /**
     * The form data.
     */
    FORM_DATA("multipart/form-data");

    /**
     * The value.
     */
    private String value;

    /**
     * Instantiates a new content type.
     *
     * @param value the value
     */
    private ContentType(String value) {

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
