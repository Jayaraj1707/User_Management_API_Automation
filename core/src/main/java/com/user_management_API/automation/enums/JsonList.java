package com.user_management_API.automation.enums;

public enum JsonList {

    CREATE_USER("jsonFiles/userCreate.json"),

    GET_USERS("jsonFiles/userGet.json"),

    GET_USER_BY_ID("jsonFiles/userGetById.json"),

    UPDATE_USER("jsonFiles/userUpdate.json"),

    DELETE_USER("jsonFiles/userDelete.json");


    /**
     * The jsonPath.
     */
    private String jsonPath;

    /**
     * Instantiates a new JsonList.
     *
     * @param jsonPath
     */
    private JsonList(String jsonPath) {

        this.jsonPath = jsonPath;
    }

    /**
     * Gets the jsonPath.
     *
     * @return the jsonPath
     */
    public String getJsonPath() {

        return jsonPath;
    }
}
