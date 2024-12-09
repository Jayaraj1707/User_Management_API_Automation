package com.user_management_API.automation.data;

import com.user_management_API.automation.entity.*;
import com.user_management_API.automation.util.*;

import static com.user_management_API.automation.manager.ConfigManager.*;

public class AbstractDataProvider {


    public static User getUserData(String loginData) {

        User loginUser = new User();
        if (loginData != null) {
            loginUser = JsonUtil.getObject(getValue(loginData), User.class);
        }
        return loginUser;
    }

}
