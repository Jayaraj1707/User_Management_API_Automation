package com.user_management_API.automation.util;

import com.user_management_API.automation.enums.*;

import java.util.*;

public class ParamUtil {

    public static String getBrowser(){
        return System.getProperty("browser");
    }

    /**
     * Checks for browser param.
     *
     * @return true, if successful
     */
    public static boolean hasBrowserParam() {

        return getBrowser() != null && !getBrowser().isEmpty();
    }

    /**
     * Gets the test suite name.
     *
     * @return the test suite name
     */
    public static String getTestSuiteName() {

        String tests = System.getProperty("tests");
        if (tests == null || tests.isEmpty()) {
            String javaCommand = System.getProperty("sun.java.command");
            if (javaCommand != null && !javaCommand.isEmpty()) {
                boolean iterateAndGet = false;
                String xmlPath = "";
                String[] arayValues = javaCommand.split(" ");
                for (String value : arayValues) {
                    if (iterateAndGet) {
                        xmlPath = value;
                        break;
                    }
                    if (value != null && value.equalsIgnoreCase("-xmlpathinjar")) {
                        iterateAndGet = true;
                    }
                }
                if (xmlPath.contains("/")) {
                    int slashIndex = xmlPath.lastIndexOf("/");
                    xmlPath = xmlPath.substring(slashIndex + 1, xmlPath.length() - 4);
                    tests = xmlPath;
                }
            }
        }
        return tests;
    }

    /**
     * Gets the log level.
     *
     * @return the log level
     */
    public static LogLevel getLogLevel() {

        final String logLevel = System.getProperty("logLevel");
        return logLevel == null ? LogLevel.HIGH : LogLevel.valueOf(logLevel.toUpperCase(Locale.getDefault()));
    }
}
