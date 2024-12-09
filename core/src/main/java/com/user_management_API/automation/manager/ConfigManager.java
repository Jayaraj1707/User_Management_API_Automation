package com.user_management_API.automation.manager;

import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;

import java.util.*;

public class ConfigManager {

    /**
     * The config.
     */
    private static Properties config = new Properties();

    /**
     * Gets the value.
     *
     * @param key the key
     * @return the value
     */
    public static String getValue(String key) {

        String value = "";
        if (config.getProperty(key) != null) {
            value = config.getProperty(key);
        } else {
            LogUtil.log("The key " + key + " is not found in property file", LogLevel.HIGH);
        }
        return value;
    }

    public static void userloadurl(String propertyFile) {
        try {
            config.load(ConfigManager.class.getClassLoader().getResourceAsStream(propertyFile));
            LogUtil.log("Loaded the property file: " + propertyFile, LogLevel.HIGH);
        } catch (Exception e) {
            LogUtil.log("No property file found", LogLevel.HIGH);
        }
    }
}
