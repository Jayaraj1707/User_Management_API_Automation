package com.user_management_API.automation.setups;

import com.user_management_API.automation.log.*;
import org.testng.*;
import org.testng.annotations.*;

import static com.user_management_API.automation.manager.ConfigManager.*;


public class UserTestBase {

    private static String propertyFile;


    @BeforeTest(alwaysRun = true)
    public void setUpMethod(final ITestContext context) throws Exception {

        String name = context.getCurrentXmlTest().getClasses().stream().findFirst().get().getName();
        System.setProperty("testclassName", name.substring(name.lastIndexOf(".") + 1));
        propertyFile = "user_API.properties";
        userloadurl(propertyFile);
        TestCaseLogUtil.printAllTestcases(context);
    }
}
