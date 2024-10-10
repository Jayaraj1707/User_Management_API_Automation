package com.user_management_API.automation.util;

import com.user_management_API.automation.enums.*;

public class ColorUtil {

    /**
     * Style.
     *
     * @param value the value
     * @param colorType the color type
     * @param styleType the style type
     * @return the string
     */
    public static String style(String value, Color colorType, Style styleType) {

        if (!colorType.getValue().equals("NO_COLOR"))
            value = colorType.getValue() + value + "\033[0m";

        if (!styleType.getValue().equals("NO_STYLE")) {
            String styleValues[] = styleType.getValue().split("#");
            value = styleValues[0] + value + styleValues[1];
        }

        return value;
    }
}
