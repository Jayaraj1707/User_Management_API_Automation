package com.user_management_API.automation.util;

import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;
import org.slf4j.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUtil {

    /**
     * Gets the string data.
     *
     * @param fileName the file name
     * @return the string data
     * @throws FileNotFoundException
     */
    public static String getStringData(String fileName) {

        InputStream input = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            try {
                throw new FileNotFoundException(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        String theString = getStringFromInputStream(input);

        return theString;
    }

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }


    /**
     * Gets the method name of property.
     *
     * @param testClassName the test class name
     * @return the method name of property
     */
    public Properties getMethodNameOfProperty(final String testClassName) {
        Properties mProperties = new Properties();
        InputStream in = getClass().getResourceAsStream("/testdata/suite/properties/" + testClassName + ".properties");
        if (in != null) {
            try {
                mProperties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mProperties;
    }

    /**
     * Creates the directory.
     *
     * @param dirPath the dir path
     */
    public static void createDirectory(final String dirPath) {

        Path directoryPath = Paths.get(dirPath);
        try {
            if (!directoryPath.toFile().exists()) {
                Files.createDirectory(directoryPath);
                LogUtil.log("Directory created :" + directoryPath, LogLevel.HIGH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
