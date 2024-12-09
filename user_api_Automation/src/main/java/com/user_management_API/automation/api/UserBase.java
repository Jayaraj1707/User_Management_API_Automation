package com.user_management_API.automation.api;

import com.user_management_API.automation.entity.*;
import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;
import com.user_management_API.automation.services.*;
import com.user_management_API.automation.util.*;
import io.restassured.*;
import io.restassured.path.json.*;
import io.restassured.response.*;
import org.testng.*;

import java.net.*;

public class UserBase {

    private static UserBase instance;

    private UserBase() {
    }


    public static UserBase getInstance() {

        if (instance == null) {
            instance = new UserBase();
        }
        return instance;
    }

    /**
     * Create User
     *
     * @param user
     * @return
     * @throws Exception
     */
    public Response createUser(User user) throws Exception {

        LogUtil.log("Creating User", LogLevel.LOW);
        String inputData = FileUtil.getStringData(JsonList.CREATE_USER.getJsonPath());
        RequestEntity request = JsonServices.getEntityFromPayload(inputData, RequestEntity.class, user.getUrl());
        LogUtil.log("URL: " + request.getUrl(), LogLevel.LOW);
        LogUtil.log("parameters" + request.getParameters(), LogLevel.LOW);
        Response response = RestAssured.given()
                .header("apikey", user.getApikey())
                .header("Authorization", "Bearer " + user.getApikey())
                .header("Content-Type", ContentType.JSON.getValue())
                .body(request.getParameters())
                .when().post(new URI(request.getUrl()));
        Assert.assertNotNull(response.getBody(), "Response body is null");
        String responseBody = response.getBody().asString();
        LogUtil.log("Response Code :" + response.getStatusCode(), LogLevel.LOW);
        LogUtil.log("Response Message: " + responseBody, LogLevel.LOW);
        return response;
    }


    /**
     * Get User
     *
     * @param user
     * @return
     * @throws Exception
     */
    public Object getUser(User user) throws Exception {

        LogUtil.log("Get User", LogLevel.LOW);
        String inputData = FileUtil.getStringData(JsonList.GET_USERS.getJsonPath());
        RequestEntity request = JsonServices.getEntityFromPayload(inputData, RequestEntity.class, user.getUrl());
        LogUtil.log("URL: " + request.getUrl(), LogLevel.LOW);
        Response response = RestAssured.given()
                .header("apikey", user.getApikey())
                .header("Authorization", "Bearer " + user.getApikey())
                .header("Content-Type", ContentType.JSON.getValue())
                .when().get(new URI(request.getUrl()));
        Assert.assertNotNull(response.getBody(), "Response body is null");
        String responseBody = response.getBody().asString();
        LogUtil.log("Response Code :" + response.getStatusCode(), LogLevel.LOW);
        LogUtil.log("Response Message: " + responseBody, LogLevel.LOW);
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid Response Code");
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getList("id").get(0);
    }

    /**
     * Get User By Id
     *
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    public Response getUserById(User user, Object id) throws Exception {

        LogUtil.log("Get User By Id", LogLevel.LOW);
        String inputData = FileUtil.getStringData(JsonList.GET_USER_BY_ID.getJsonPath());
        RequestEntity request = JsonServices.getEntityFromPayload(inputData, RequestEntity.class, user.getUrl());
        LogUtil.log("URL: " + request.getUrl(), LogLevel.LOW);
        Response response = RestAssured.given()
                .header("apikey", user.getApikey())
                .header("Authorization", "Bearer " + user.getApikey())
                .header("Content-Type", ContentType.JSON.getValue())
                .when().get(new URI(request.getUrl() + id));
        Assert.assertNotNull(response.getBody(), "Response body is null");
        String responseBody = response.getBody().asString();
        LogUtil.log("Response Code :" + response.getStatusCode(), LogLevel.LOW);
        LogUtil.log("Response Message: " + responseBody, LogLevel.LOW);
        return response;
    }

    /**
     * Update User
     *
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    public Response updateUser(User user, Object id) throws Exception {

        LogUtil.log("Delete User", LogLevel.LOW);
        String inputData = FileUtil.getStringData(JsonList.UPDATE_USER.getJsonPath());
        RequestEntity request = JsonServices.getEntityFromPayload(inputData, RequestEntity.class, user.getUrl());
        LogUtil.log("URL: " + request.getUrl(), LogLevel.LOW);
        Response response = RestAssured.given()
                .header("apikey", user.getApikey())
                .header("Authorization", "Bearer " + user.getApikey())
                .header("Content-Type", ContentType.JSON.getValue())
                .when().patch(new URI(request.getUrl() + id));
        Assert.assertNotNull(response.getBody(), "Response body is null");
        String responseBody = response.getBody().asString();
        LogUtil.log("Response Code :" + response.getStatusCode(), LogLevel.LOW);
        LogUtil.log("Response Message: " + responseBody, LogLevel.LOW);
        return response;
    }

    /**
     * Delete User
     *
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    public Response deleteUser(User user, Object id) throws Exception {

        LogUtil.log("Delete User", LogLevel.LOW);
        String inputData = FileUtil.getStringData(JsonList.DELETE_USER.getJsonPath());
        RequestEntity request = JsonServices.getEntityFromPayload(inputData, RequestEntity.class, user.getUrl());
        LogUtil.log("URL: " + request.getUrl(), LogLevel.LOW);
        Response response = RestAssured.given()
                .header("apikey", user.getApikey())
                .header("Authorization", "Bearer " + user.getApikey())
                .header("Content-Type", ContentType.JSON.getValue())
                .when().delete(new URI(request.getUrl() + id));
        Assert.assertNotNull(response.getBody(), "Response body is null");
        String responseBody = response.getBody().asString();
        LogUtil.log("Response Code :" + response.getStatusCode(), LogLevel.LOW);
        LogUtil.log("Response Message: " + responseBody, LogLevel.LOW);
        return response;
    }
}
