package com.user_management_API.automation.util;

import com.fasterxml.jackson.databind.*;
import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;

import java.util.*;

/**
 * The Class APIUtil.
 */
public final class APIUtil {

    /** The Object mapper. */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Instantiates a new API util.
     */
    private APIUtil() {

    }

    /**
     * Read.
     *
     * @param input
     * @param clazz
     * @param <T>
     * @return T
     * @throws Exception
     */
    public static <T> T read(String input, Class<T> clazz) throws Exception {

        T result = null;

        try {
            result = mapper.readValue(input, clazz);
        } catch (Exception e) {
            throw new Exception(e);
        }

        return result;
    }
}
