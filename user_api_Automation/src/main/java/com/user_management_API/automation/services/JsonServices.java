package com.user_management_API.automation.services;

import com.user_management_API.automation.util.*;

/**
 * The Class JsonServices.
 */
public class JsonServices {

    /**
     * Get Entity from payload.
     *
     * @param inputData
     * @param entity
     * @param params
     * @return T
     * @throws Exception
     */
    public static <T> T getEntityFromPayload(String inputData, Class<T> entity, String... params) throws Exception {

        String updatedInput = String.format(inputData, (Object[]) params);
        T request = APIUtil.read(updatedInput, entity);
        return request;
    }
}
