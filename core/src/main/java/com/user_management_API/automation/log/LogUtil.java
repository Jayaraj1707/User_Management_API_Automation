package com.user_management_API.automation.log;


import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.util.*;
import org.slf4j.*;
import org.testng.*;

public class LogUtil {

    private static ThreadLocal<Integer> noOfSections = new ThreadLocal<Integer>();
    private static ThreadLocal<Integer> noOfSteps = new ThreadLocal<Integer>();

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
    private static ThreadLocal<Boolean> stepsRunning = new ThreadLocal<Boolean>();

    static {
        clear();
    }

    /**
     * Log.
     *
     * @param s the s
     */
    public static void log(String s, final LogLevel logLevel) {

        if (ParamUtil.getLogLevel().getValue() >= logLevel.getValue()) {
            printSections(null, s);
            commonConsoleLog(s);
        }
    }

    /**
     * Log.
     *
     * @param logSection the log section
     * @param s the s
     */
    public static void log(LogSection logSection, String s) {

        printSections(logSection, s);
    }

    public static void log(Steps steps, String s) {

        printSteps(steps, s);
    }

    /**
     * Gets the no of sections.
     *
     * @return the no of sections
     */
    public static int getNoOfSections() {

        if (noOfSections.get() == null) {
            noOfSections.set(0);
        }
        return noOfSections.get();
    }

    /**
     * Sets the no of sections.
     *
     * @param sections the new no of sections
     */
    public static void setNoOfSections(int sections) {

        noOfSections.set(sections);
    }

    public static Integer getNoOfSteps() {

        if (noOfSteps.get() == null) {
            noOfSteps.set(0);
        }
        return noOfSteps.get();
    }

    public static void setNoOfSteps(Integer steps) {

        noOfSteps.set(steps);
    }

    private static void printSteps(Steps steps, String log) {

        String space = StringUtil.addIndent(getNoOfSections() + 1);
        if (steps == Steps.START) {
            int currentStep = getNoOfSteps() + 1;
            stepsRunning.set(true);
            LOGGER.info(new StringBuilder(space).append("STEP  " + currentStep + " :" + log).toString());
            space = space.replaceAll(" ", "&nbsp;");
            Reporter.log(new StringBuilder(space).append(
                    "<font style=\"background-color:#ffff88\">STEP  " + currentStep + " :" + log + "</font>").toString());
            space = StringUtil.addIndent(getNoOfSections() + 2);
            space = space.replaceAll(" ", "&nbsp;");
            setNoOfSteps(getNoOfSteps() + 1);
        } else if (steps == Steps.END) {
            stepsRunning.set(false);
            LOGGER.info(new StringBuilder(space).append(log).toString());
            LOGGER.info("");
            space = space.replaceAll(" ", "&nbsp;");
            Reporter.log(new StringBuilder(space).append(log).toString());
            Reporter.log("");
        }
    }

    public static void clear() {

        noOfSections.set(0);
        noOfSteps.set(0);
        stepsRunning.set(false);
    }

    /**
     * Prints the in html.
     *
     * @param log the log
     */
    private static void printSections(LogSection logSection, String log) {

        String space = StringUtil.addIndent(getNoOfSections());

        if (logSection == LogSection.START) {
            LOGGER.info(new StringBuilder(space).append(log).toString());
            LOGGER.info(new StringBuilder(space).append(StringUtil.addUnderline(log.length())).toString());
            space = space.replaceAll(" ", "&nbsp;");
            Reporter.log(new StringBuilder(space).append("<b>" + log + "</b>").toString(), false);
            Reporter.log(new StringBuilder(space).append(StringUtil.addUnderline(log.length())).toString(), false);
            setNoOfSections(getNoOfSections() + 1);
        } else if (logSection == LogSection.END) {
            setNoOfSections(getNoOfSections() - 1);
            space = StringUtil.addIndent(getNoOfSections());
            LOGGER.info(new StringBuilder(space).append(log).toString());
            LOGGER.info(new StringBuilder(space).append(StringUtil.addUnderline(log.length())).toString());
            space = space.replaceAll(" ", "&nbsp;");
            Reporter.log(new StringBuilder(space).append("<b>" + log + "</b>").toString(), false);
            Reporter.log(new StringBuilder(space).append(StringUtil.addUnderline(log.length())).toString(), false);
        }
    }

    public static void commonConsoleLog(String log) {

        if (stepsRunning == null || stepsRunning.get() == null) {
            stepsRunning.set(false);
        }
        String space = StringUtil.addIndent(stepsRunning.get() ? getNoOfSections() + 2 : getNoOfSections());
        LOGGER.info(new StringBuilder(space).append(StringUtil.changeHtmlToPlain(log)).toString());
        space = space.replaceAll(" ", "&nbsp;");
        Reporter.log(new StringBuilder(space).append(log).toString(), false);
    }

    public static void commonEndConsoleLog(String log) {

        LOGGER.info(new StringBuilder(StringUtil.changeHtmlToPlain(log)).toString());
    }
}
