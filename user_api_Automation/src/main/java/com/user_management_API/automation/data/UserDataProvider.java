package com.user_management_API.automation.data;

import com.user_management_API.automation.entity.*;
import org.testng.annotations.*;


public class UserDataProvider extends  AbstractDataProvider {

    @DataProvider(name = "UserCruds")
    public static Object[][] UserCruds(){
        User loginUser = getUserData("user.credential");
        return new Object[][] { { loginUser } };
    }
}
