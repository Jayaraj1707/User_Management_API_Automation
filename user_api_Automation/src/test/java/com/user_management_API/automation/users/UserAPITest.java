package com.user_management_API.automation.users;

import com.user_management_API.automation.api.*;
import com.user_management_API.automation.data.*;
import com.user_management_API.automation.entity.*;
import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.listeners.*;
import com.user_management_API.automation.log.*;
import com.user_management_API.automation.setups.*;
import com.user_management_API.automation.testng.*;
import io.restassured.response.*;
import org.testng.*;
import org.testng.annotations.*;

@Listeners({CustomReport.class, AutomationListener.class})
public class UserAPITest extends UserTestBase {

    private UserBase userBase;

    private Object id;

    /**
     * User Create
     *
     * @param user
     * @throws Exception
     */
    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "UserCruds", priority = 1)
    public void userCreate(User user) throws Exception {
        LogUtil.log(Steps.START, "Validate the Create User API");
        userBase = UserBase.getInstance();
        Response response = userBase.createUser(user);
        Assert.assertEquals(response.getStatusCode(), 201, "Invalid Response Code");
    }

    /**
     * User Create Invalid
     *
     * @param user
     * @throws Exception
     */
    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "UserCruds", priority = 2)
    public void userCreateInvalid(User user) throws Exception {
        LogUtil.log(Steps.START, "Validate the Create Invalid User API");
        userBase = UserBase.getInstance();
        Response response = userBase.createUser(user);
        Assert.assertEquals(response.getStatusCode(), 409, "Invalid Response Code");
    }

    /**
     * Get User
     *
     * @param user
     * @throws Exception
     */
    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "UserCruds", priority = 3)
    public void getUser(User user) throws Exception {
        LogUtil.log(Steps.START, "Validate the Get User API");
        userBase = UserBase.getInstance();
        id = userBase.getUser(user);
    }


    /**
     * Update User
     *
     * @param user
     * @param id
     * @throws Exception
     */
    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "UserCruds", priority = 4)
    public void updateUser(User user, Object id) throws Exception {
        LogUtil.log(Steps.START, "Validate the Update User API");
        Response response = userBase.updateUser(user, id);
        Assert.assertEquals(response.getStatusCode(), 204, "Invalid Response Code");

    }

    /**
     * Get User By Id
     *
     * @param user
     * @throws Exception
     */
    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "UserCruds", priority = 5)
    public void getUserById(User user) throws Exception {
        LogUtil.log(Steps.START, "Validate the Get User By Id API");
        Response response = userBase.getUserById(user, id);
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid Response Code");
    }

    /**
     * User Delete
     *
     * @param user
     * @throws Exception
     */
    @Test(dataProviderClass = UserDataProvider.class, dataProvider = "UserCruds", priority = 6)
    public void userDelete(User user) throws Exception {
        LogUtil.log(Steps.START, "Validate the Delete User API");
        Response response = userBase.deleteUser(user, id);
        Assert.assertEquals(response.getStatusCode(), 204, "Invalid Response Code");
    }

}
