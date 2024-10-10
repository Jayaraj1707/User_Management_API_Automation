package com.user_management_API.automation.browser;

import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;

import java.io.*;
import java.util.*;

public class ChromeBrowser {


    public static WebDriver createDriver() {

        String downloadFilepath = getFilePath();
        HashMap<String, Object> preferences = new HashMap<String, Object>();
        preferences.put("plugins.always_open_pdf_externally", "true");
        preferences.put("download.default_directory", downloadFilepath);
        preferences.put("profile.default_content_settings.popups", 0);
        preferences.put("profile.default_content_setting_values.notifications", 1);
        preferences.put("download.prompt_for_download", "false");
        preferences.put("browser.helperApps.neverAsk.saveToDisk",
                "text/csv,application/x-msexcel,application/excel,application/pdf,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/octet-stream");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", preferences);
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");

        WebDriver driver;
        driver = new ChromeDriver(chromeOptions);

        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserVersion = (String) cap.getCapability("browserVersion");
        LogUtil.log("Browser Version : " + browserVersion, LogLevel.HIGH);

        return driver;
    }

    public static String getFilePath() {

        String downloadPath = "";

        try {
            File downloadFile = new File("");
            downloadPath = downloadFile.getAbsolutePath();
            downloadPath = downloadPath + File.separator + "testresult";
            LogUtil.log("Download File Path :" + downloadPath, LogLevel.HIGH);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.log("Error in reading file path ", LogLevel.HIGH);
        }
        return downloadPath;
    }
}
