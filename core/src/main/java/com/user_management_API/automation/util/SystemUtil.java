package com.user_management_API.automation.util;

public class SystemUtil {

    /**
     * Gets the property.
     *
     * @param property the property
     * @return the property
     */
    public static String getProperty(String property) {

        String value = System.getProperty(property);
        return value == null || value.isEmpty() ? "-" : value;
    }

    /**
     * Gets the system os.
     *
     * @return the system os
     */
    public static String getSystemOs() {

        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * Gets the system os type as an enum.
     *
     * @return the system os type
     */
    public static DesktopOSType getOSType() {

        if (getSystemOs().contains("mac")) {
            return DesktopOSType.MAC;
        } else if (getSystemOs().contains("nux")) {
            return DesktopOSType.LINUX;
        }
        return DesktopOSType.WINDOWS;
    }

    /**
     * The Enum DesktopOSType.
     *
     * @author $Author:$
     * @version $Rev:$ $Date:$
     */
    public enum DesktopOSType {

        WINDOWS, LINUX, MAC
    }
}
