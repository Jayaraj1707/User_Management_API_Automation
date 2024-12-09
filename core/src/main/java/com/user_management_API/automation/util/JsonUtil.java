package com.user_management_API.automation.util;

import com.fasterxml.jackson.databind.*;
import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;

import java.io.*;

/**
 * The Class JsonUtil.
 * 
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class JsonUtil {

	/**
	 * Gets the object.
	 * 
	 * @param <T> the
	 * @param jsonString the json string
	 * @param className the class name
	 * @return the object
	 */
	public static <T> T getObject(String jsonString, Class<T> className) {

		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonString, className);
		} catch (NullPointerException ex) {

			LogUtil.log(jsonString == null ? "The File for the environment is not loaded" : "The required data " + jsonString
			        + " is not in property file", LogLevel.MEDIUM);
		} catch (IOException e) {
			LogUtil.log("Exception in JSON parsing. Cause: " + e, LogLevel.MEDIUM);
		}
		return null;
	}
}