package com.user_management_API.automation.testng;


import com.google.common.collect.*;
import com.user_management_API.automation.enums.*;
import com.user_management_API.automation.log.*;
import com.user_management_API.automation.util.*;
import org.joda.time.*;
import org.testng.*;
import org.testng.internal.*;
import org.testng.log4testng.*;
import org.testng.xml.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

public class CustomReport implements IReporter {

    private static final Logger LOG = Logger.getLogger(CustomReport.class);

    protected PrintWriter writer;
    protected PrintWriter summaryWritter;
    protected PrintWriter exceptionWriter;

    protected List<SuiteResult> suiteResults = Lists.newArrayList();

    // Reusable buffer
    private StringBuilder buffer = new StringBuilder();

    /*
     * Method override
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

        try {
            writer = createWriter(outputDirectory);
            summaryWritter = createWriterForSummary(outputDirectory);
            exceptionWriter = createWriterForExceptions(outputDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Unable to create output file", e);
            return;
        }
        for (ISuite suite : suites) {
            suiteResults.add(new SuiteResult(suite));
        }

        writeDocumentExceptionStart();
        writeDocumentStart();
        writeHead();

        writeBody();
        writeDocumentEnd();
        writer.close();

        summaryWritter.close();
        exceptionWriter.print("</body></html>");
        exceptionWriter.close();
    }

    protected PrintWriter createWriterForSummary(String outdir) throws IOException {

        String suiteLevel = System.getProperty("suiteLevel");
        LogUtil.log("Create Suite directory if suite level is true (" + suiteLevel + ")", LogLevel.HIGH);
        if (suiteLevel != null && !suiteLevel.isEmpty() && suiteLevel.equalsIgnoreCase("true")) {
            outdir += "/suites";
        }
        new File(outdir).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "summary.html"))));
    }

    protected PrintWriter createWriterForExceptions(String outdir) throws IOException {

        new File(outdir).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "exception.html"))));
    }

    protected void writeDocumentExceptionStart() {

        exceptionWriter
                .println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        exceptionWriter.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        exceptionWriter.print("<head>");
        exceptionWriter.print("<title>Failure Exception Stacktrace</title>");
        exceptionWriter.print("</head><body><table>");
    }

    /**
     * Creates the writer.
     *
     * @param outdir the outdir
     * @return the prints the writer
     * @throws java.io.IOException Signals that an I/O exception has occurred.
     */
    protected PrintWriter createWriter(String outdir) throws IOException {

        new File(outdir).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "report.html"))));
    }

    /**
     * Write document start.
     */
    protected void writeDocumentStart() {

        writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        writer.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");

        summaryWritter
                .println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        summaryWritter.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    }

    /**
     * Write head.
     */
    protected void writeHead() {

        writer.print("<head>");
        writer.print("<title>TestNG Report</title>");
        writeStylesheet();
        writer.print("</head>");

        summaryWritter.print("<head>");
        summaryWritter.print("<title>TestNG Report</title>");
        summaryWritter.print("</head>");

    }

    /**
     * Write stylesheet.
     */
    protected void writeStylesheet() {

        writer.print("<style type=\"text/css\">");
        writer.print("table {border-collapse:collapse;empty-cells:show}");
        writer.print("th,td {border:1px solid #009;padding:.25em .5em}");
        writer.print("th {vertical-align:bottom}");
        writer.print("td {vertical-align:top}");
        writer.print("table a {font-weight:bold}");
        writer.print(".stripe td {background-color: #ffff88}");
        writer.print(".num {text-align:right}");
        writer.print(".passedodd td {background-color: #3F3}");
        writer.print(".passedeven td {background-color: #0A0}");
        writer.print(".skippedodd td {background-color: #DDD}");
        writer.print(".skippedeven td {background-color: #CCC}");
        writer.print(".failedodd td,.attn {background-color: #F33}");
        writer.print(".failedeven td,.stripe .attn {background-color: #D00}");
        writer.print(".stacktrace {white-space:pre;font-family:monospace}");
        writer.print(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
        writer.print("</style>");
    }

    /**
     * Write body.
     */
    protected void writeBody() {

        writer.print("<body>");
        summaryWritter.print("<body>");
        // writeSuiteSummary();
        writeParamDetails();
        writeResultSummary();
        writeScenarioSummary();
        writeScenarioDetails();
        writer.print("</body>");
        String suiteLevel = System.getProperty("suiteLevel");
        if (suiteLevel == null || suiteLevel.isEmpty() || suiteLevel.equalsIgnoreCase("false")) {
            summaryWritter.print("</body>");
        } else {
            summaryWritter.print("</body>");
        }

    }

    /**
     * Write param details.
     */
    protected void writeParamDetails() {

        long totalDuration = 0;
        for (SuiteResult suiteResult : suiteResults) {
            for (TestResult testResult : suiteResult.getTestResults()) {
                totalDuration += testResult.getDuration();
            }
        }

        long endVal = Calendar.getInstance().getTimeInMillis();
        long startVal = endVal - totalDuration;

        String startTime = DateUtil.formatToZone(startVal, DateTimeZone.forID("America/Los_Angeles"), DateUtil.LARGE_FORMAT);
        String endTime = DateUtil.formatToZone(endVal, DateTimeZone.forID("America/Los_Angeles"), DateUtil.HOUR_FORMAT);
        String resultMessage = "<a style=\"font-weight:bold\" id=\"summary\" name=\"summary\">Results executed on " + startTime
                + " to " + endTime + "</a>";

        String os = SystemUtil.getOSType().toString();
        String browser = SystemUtil.getProperty("browser");

        StringBuilder builder = new StringBuilder();
        writer.print(resultMessage);
        writer.print("<div style=\"display:flex;justify-content: space-between\">");
        writer.print("<div colspan=\"5\" style=\"display:flex;flex-direction:column;width: 35%\">");
        String summary = "<h4 style=\"margin-bottom:10px;text-align:center\"> Setup Summary Test </h4>";
        writer.print(summary);
        builder.append("<table width=\"100%\" style=\"border-collapse:collapse;empty-cells:show\">"
                + "<tr>"
                + "<th style=\"border:1px solid #009;padding:.25em .5em;vertical-align:bottom;background-color:#d8dde4\">OS</th>"
                + "<th style=\"border:1px solid #009;padding:.25em .5em;vertical-align:bottom;background-color:#d8dde4\">Browser</th>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\"> "
                + os
                + "</td>"
                + "<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\">"
                + browser
                + "</td>"
                + "</tr></table>");
        writer.print(builder.toString());
        writer.print("</div>");

        writer.print("<div colspan=\"7\" style=\"display:flex;flex-direction:column;width: 60%\">");

        writer.print("<h4 style=\"morgin-top:40px; margin-bottom:10px; text-align:center\">Result Summary</h4>");
        writer.print("<table width=\"100%\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009;\" border=\"1px\">");
        writer.print("<tr>");
        writer.print("<th style=\"background-color: #d8dde4\">Total Passed</th>");
        writer.print("<th style=\"background-color: #d8dde4\">Total Failed</th>");
        writer.print("<th style=\"background-color: #d8dde4\">Total Skipped</th>");
        writer.print("<th style=\"background-color: #d8dde4\">Total Duration</th>");
        // writer.print("<th>Total Duration(mins)</th>");
        writer.print("</tr>");
        int totalPassedTests = 0;
        int totalFailedTests = 0;
        int totalSkippedTests = 0;
        long totalDurations = 0;

        for (SuiteResult suiteResult : suiteResults) {

            for (TestResult testResult : suiteResult.getTestResults()) {
                int passedTests = testResult.getPassedTestCount();
                int failedTests = testResult.getFailedTestCount();
                int skippedTests = testResult.getSkippedTestCount();
                long duration = testResult.getDuration();

                totalPassedTests += passedTests;
                totalFailedTests += failedTests;
                totalSkippedTests += skippedTests;
                totalDurations += duration;
            }
        }

        String totalTime = calculateTime(totalDurations);
        // int minutes = (int) ((totalDuration / 1000) / 60);
        writer.print("<tr><td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\">" + totalPassedTests
                + "</td>" + "<td  style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\">" + totalFailedTests
                + "</td>" + "<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\">" + totalSkippedTests
                + "</td>" + "<td  style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\">" + totalTime + "</td>"
                + "</tr></table>");
        writer.print("</div>");

        writer.print("</div>");

        summaryWritter.print(resultMessage);
        summaryWritter.print(summary);
        summaryWritter.print(builder.toString());

    }

    private static String getTestCaseId(Properties props, String key) {

        String caseId = null;
        if (props != null && props.get(key) != null) {
            caseId = props != null ? props.get(key).toString() : null;
        }
        return caseId != null ? "C" + caseId : "-";
    }

    /**
     * Write document end.
     */
    protected void writeDocumentEnd() {

        writer.print("</html>");
    }

    /**
     * Write result summary.
     */
    protected void writeResultSummary() {

        writer.print("<div>");
        summaryWritter.print("<h4 style=\"morgin-top:40px; margin-bottom:10px; text-align:center\">Result Summary</h4>");
        summaryWritter
                .print("<table width=\"100%\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009\" border=\"1px\">");
        summaryWritter.print("<tr>");
        summaryWritter.print("</tr>");
        summaryWritter.print("<tr>");
        summaryWritter.print("<th style=\"background-color: #d8dde4\">Total Passed</th>");
        summaryWritter.print("<th style=\"background-color: #d8dde4\">Total Failed</th>");
        summaryWritter.print("<th style=\"background-color: #d8dde4\">Total Skipped</th>");
        summaryWritter.print("<th style=\"background-color: #d8dde4\">Total Duration</th>");
        ;
        summaryWritter.print("</tr>");

        int totalPassedTests = 0;
        int totalFailedTests = 0;
        int totalSkippedTests = 0;
        long totalDuration = 0;

        for (SuiteResult suiteResult : suiteResults) {

            for (TestResult testResult : suiteResult.getTestResults()) {
                int passedTests = testResult.getPassedTestCount();
                int failedTests = testResult.getFailedTestCount();
                int skippedTests = testResult.getSkippedTestCount();
                long duration = testResult.getDuration();

                totalPassedTests += passedTests;
                totalFailedTests += failedTests;
                totalSkippedTests += skippedTests;
                totalDuration += duration;
            }
        }

        String totalTime = calculateTime(totalDuration);

        summaryWritter.print("<tr><td  style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top;text-align:center\">"
                + totalPassedTests + "</td>"
                + "<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top;text-align:center\">"
                + totalFailedTests + "</td>"
                + "<td  style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top;text-align:center\">"
                + totalSkippedTests + "</td>"
                + "<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top;text-align:center\">" + totalTime
                + "</td>" + "</tr></table>");

        writer.print("</div>");

        // +
        // "<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\">"
        // + minutes + "</td>

    }

    /**
     * Write suite summary.
     */
    protected void writeSuiteSummary() {

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormat decimalFormat = NumberFormat.getNumberInstance();

        int totalPassedTests = 0;
        int totalSkippedTests = 0;
        int totalFailedTests = 0;
        long totalDuration = 0;

        writer.print("<table width=\"100%\" style=\"border-collapse:collapse;empty-cells:show\">");
        writer.print("<tr>");
        writer.print("<th>Test</th>");
        writer.print("<th># Passed</th>");
        writer.print("<th># Skipped</th>");
        writer.print("<th># Failed</th>");
        writer.print("<th>Time (ms)</th>");
        writer.print("<th>Included Groups</th>");
        writer.print("<th>Excluded Groups</th>");
        writer.print("</tr>");

        int testIndex = 0;
        for (SuiteResult suiteResult : suiteResults) {
            writer.print("<tr><th colspan=\"7\">");
            writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
            writer.print("</th></tr>");

            for (TestResult testResult : suiteResult.getTestResults()) {
                int passedTests = testResult.getPassedTestCount();
                int skippedTests = testResult.getSkippedTestCount();
                int failedTests = testResult.getFailedTestCount();
                long duration = testResult.getDuration();

                writer.print("<tr");
                if ((testIndex % 2) == 1) {
                    writer.print(" style=\"background-color: #ffff88\"");
                }
                writer.print(">");

                buffer.setLength(0);
                writeTableData(buffer.append("<a style=\"font-weight:bold\" href=\"#t").append(testIndex).append("\">")
                        .append(Utils.escapeHtml(testResult.getTestName())).append("</a>").toString());
                writeTableData(integerFormat.format(passedTests), "text-align:right");
                writeTableData(integerFormat.format(skippedTests), (skippedTests > 0 ? "text-align:right; background-color: #F33"
                        : "text-align:right"));
                writeTableData(integerFormat.format(failedTests), (failedTests > 0 ? "text-align:right; background-color: #F33"
                        : "text-align:right"));
                writeTableData(decimalFormat.format(duration), "text-align:right");
                writeTableData(testResult.getIncludedGroups());
                writeTableData(testResult.getExcludedGroups());

                writer.print("</tr>");

                totalPassedTests += passedTests;
                totalSkippedTests += skippedTests;
                totalFailedTests += failedTests;
                totalDuration += duration;

                testIndex++;
            }
        }

        // Print totals if there was more than one test
        if (testIndex > 1) {
            writer.print("<tr>");
            writer.print("<th style=\"border:1px solid #009;padding:.25em .5em;vertical-align:bottom\">Total</th>");
            writeTableHeader(integerFormat.format(totalPassedTests), "text-align:right;");
            writeTableHeader(integerFormat.format(totalSkippedTests),
                    (totalSkippedTests > 0 ? "text-align:right; background-color: #F33" : "text-align:right;"));
            writeTableHeader(integerFormat.format(totalFailedTests),
                    (totalFailedTests > 0 ? "text-align:right; background-color: #F33" : "text-align:right;"));
            writeTableHeader(decimalFormat.format(totalDuration), "text-align:right;");
            writer.print("<th colspan=\"2\"></th>");
            writer.print("</tr>");
        }

        writer.print("</table>");
    }

    /**
     * Writes a summary of all the test scenarios.
     */
    protected void writeScenarioSummary() {

        writer.print("<table width=\"100%\" border=\"1px\" style=\"margin-top:44px;border-collapse:collapse;empty-cells:show;border:1px solid #009;\">");
        summaryWritter
                .print("<table width=\"100%\" border=\"1px\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009; margin-top:40px\" >");

        int testIndex = 0;
        int scenarioIndex = 0;
        for (SuiteResult suiteResult : suiteResults) {

            writer.print("<tbody>");
            writer.print("</th></tr></tbody>");

            boolean hasFailures = false;
            for (TestResult testResult : suiteResult.getTestResults()) {
                if (testResult.getFailedConfigurationResults().size() > 0 || testResult.getFailedTestResults().size() > 0
                        || testResult.getSkippedConfigurationResults().size() > 0
                        || testResult.getSkippedTestResults().size() > 0) {
                    hasFailures = true;
                }
            }


            for (TestResult testResult : suiteResult.getTestResults()) {
                writer.print("<tbody id=\"t");
                writer.print(testIndex);
                writer.print("\">");

                summaryWritter.print("<tbody id=\"t");
                summaryWritter.print(testIndex);
                summaryWritter.print("\">");

                scenarioIndex += writeScenarioSummary("FAILED (configuration methods)",
                        testResult.getFailedConfigurationResults(), "failed", scenarioIndex);
                scenarioIndex += writeScenarioSummary("FAILED", testResult.getFailedTestResults(), "failed", scenarioIndex);

                writer.print("</tbody>");
                summaryWritter.print("</tbody>");

                testIndex++;
            }
        }

        writer.print("</table>");
        summaryWritter.print("</table>");

        writer.print("<div>");
        writer.print("<table width=\"100%\" border=\"1px\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009;margin-top:40px\">");
        summaryWritter
                .print("<table width=\"100%\" border=\"1px\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009;margin-top:40px\" >");

        int testIndex1 = 0;
        int scenarioIndex1 = 0;
        for (SuiteResult suiteResult : suiteResults) {

            writer.print("<tbody>");
            writer.print("</th></tr></tbody>");

            boolean hasFailures = false;
            for (TestResult testResult : suiteResult.getTestResults()) {
                if (testResult.getFailedConfigurationResults().size() > 0 || testResult.getFailedTestResults().size() > 0
                        || testResult.getSkippedConfigurationResults().size() > 0
                        || testResult.getSkippedTestResults().size() > 0) {
                    hasFailures = true;
                }
            }

            for (TestResult testResult : suiteResult.getTestResults()) {
                writer.print("<tbody id=\"t");
                writer.print(testIndex1);
                writer.print("\">");

                summaryWritter.print("<tbody id=\"t");
                summaryWritter.print(testIndex1);
                summaryWritter.print("\">");

                scenarioIndex1 += writeScenarioSummary("PASSED", testResult.getPassedTestResults(), "passed", scenarioIndex1);

                writer.print("</tbody>");
                summaryWritter.print("</tbody>");

                testIndex1++;
            }
        }

        writer.print("</table>");
        writer.print("</div>");

        writer.print("<div>");
        writer.print("<table width=\"100%\" border=\"1px\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009;margin-top:40px\">");
        summaryWritter
                .print("<table width=\"100%\" border=\"1px\" style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009;margin-top:40px\" >");

        int testIndex2 = 0;
        int scenarioIndex2 = 0;
        for (SuiteResult suiteResult : suiteResults) {

            writer.print("<tbody>");
            writer.print("</th></tr></tbody>");

            boolean hasFailures = false;
            for (TestResult testResult : suiteResult.getTestResults()) {
                if (testResult.getFailedConfigurationResults().size() > 0 || testResult.getFailedTestResults().size() > 0
                        || testResult.getSkippedConfigurationResults().size() > 0
                        || testResult.getSkippedTestResults().size() > 0) {
                    hasFailures = true;
                }
            }

            for (TestResult testResult : suiteResult.getTestResults()) {
                writer.print("<tbody id=\"t");
                writer.print(testIndex2);
                writer.print("\">");

                summaryWritter.print("<tbody id=\"t");
                summaryWritter.print(testIndex2);
                summaryWritter.print("\">");

                scenarioIndex2 += writeScenarioSummary("SKIPPED (configuration methods)",
                        testResult.getSkippedConfigurationResults(), "skipped", scenarioIndex2);
                scenarioIndex2 += writeScenarioSummary("SKIPPED", testResult.getSkippedTestResults(), "skipped", scenarioIndex2);
                writer.print("</tbody>");
                summaryWritter.print("</tbody>");

                testIndex1++;
            }
        }

        writer.print("</table>");
        writer.print("</div>");
    }

    /**
     * Writes the scenario summary for the results of a given state for a single
     * test.
     *
     * @param description           the description
     * @param classResults          the class results
     * @param cssClassPrefix        the css class prefix
     * @param startingScenarioIndex the starting scenario index
     * @return the int
     */
    private int writeScenarioSummary(String description, List<ClassResult> classResults, String cssClassPrefix,
                                     int startingScenarioIndex) {

        int scenarioCount = 0;
        if (!classResults.isEmpty()) {

            String backgroundColor = "";
            if (cssClassPrefix.contains("failed")) {
                backgroundColor = "#d8dde4";
            } else if (cssClassPrefix.contains("passed")) {
                backgroundColor = "#d8dde4";
            } else if (cssClassPrefix.contains("skipped")) {
                backgroundColor = "#d8dde4";
            }

            writer.print("<tr><th style=\"background-color:" + backgroundColor + "\" colspan=\"6\">");
            writer.print("<font color=\"black\">" + description + "</font>");
            writer.print("</th></tr>");

            writer.print("<tr>");
            writer.print("<th>Test Area</th>");
            writer.print("<th>Test Case</th>");
            writer.print("<th>Error</th>");
            writer.print("<th>Screenshot</th>");
            writer.print("<th>Start (pst)</th>");
            writer.print("<th>Time (sec)</th>");
            writer.print("</tr>");

            if (description != null && !description.equalsIgnoreCase("passed")) {
                summaryWritter.print("<tr><th style=\"background-color:" + backgroundColor + "\" colspan=\"6\">");
                summaryWritter.print("<font color=\"black\">" + description + "</font>");
                summaryWritter.print("</th></tr>");

                summaryWritter.print("<tr>");
                summaryWritter.print("<th>Test Area</th>");
                summaryWritter.print("<th>Test Case</th>");
                summaryWritter.print("<th>Error</th>");
                summaryWritter.print("<th>Screenshot</th>");
                summaryWritter.print("<th>Start (pst)</th>");
                summaryWritter.print("<th>Time (sec)</th>");
                summaryWritter.print("</tr>");
            }

            int scenarioIndex = startingScenarioIndex;
            // int classIndex = 0;
            for (ClassResult classResult : classResults) {

                String cssClass = "";
                buffer.setLength(0);

                int scenariosPerClass = 0;
                int methodIndex = 0;
                for (MethodResult methodResult : classResult.getMethodResults()) {
                    List<ITestResult> results = methodResult.getResults();
                    int resultsCount = results.size();
                    assert resultsCount > 0;

                    ITestResult firstResult = results.get(0);
                    String methodName = Utils.escapeHtml(firstResult.getMethod().getMethodName());
                    long start = firstResult.getStartMillis();
                    DateTimeZone zone = DateTimeZone.forID("America/Los_Angeles");
                    String startTime = DateUtil.formatToZone(start, zone, DateUtil.MM_DD_YY);
                    long duration = firstResult.getEndMillis() - start;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

                    // The first method per class shares a row with the class
                    // header
                    if (methodIndex > 0) {
                        buffer.append("<tr style=\"").append(cssClass.toLowerCase()).append("\">");

                    }

                    String testresult = "testresult";
                    String screenshot = "-";

                    if (firstResult.getAttribute("screenshot") != null
                            && !firstResult.getAttribute("screenshot").toString().isEmpty()) {
                        screenshot = (String) firstResult.getAttribute("screenshot");

                        screenshot = "<a style=\"font-weight:bold\"" + " target=\"_blank\" href="
                                + "testresult/screenshots/" + screenshot + ">view</a>";
                    }

                    Throwable throwable = firstResult.getThrowable();
                    String exception = "-";
                    if (throwable != null) {
                        exception = "	<a target=\"_blank\"  href="
                                + testresult + "/exception.html#" + methodName + ">detail</a>";
                    }

                    // Write the timing information with the first scenario per
                    // method
                    buffer.append(
                                    "<td style=\"vertical-align:top;\"><a style=\"font-weight:bold;text-decoration:underline;\" "
                                            + "target=\"_blank\" href="
                                            + testresult + "/report.html#m" + scenarioIndex + "\">")
                            .append(methodName)
                            .append("</a></td>" + "<td style=\"vertical-align:top;text-align:center\">" + exception + "</td>"
                                    + "<td style=\"vertical-align:top;text-align:center\">" + screenshot + "</td>")
                            .append("<td style=\"vertical-align:top;text-align:center\">").append(startTime).append("</td>")
                            .append("<td style=\"vertical-align:top;text-align:center\">").append(seconds).append("</td></tr>");
                    scenarioIndex++;

                    // Write the remaining scenarios for the method
                    for (int i = 1; i < resultsCount; i++) {
                        firstResult = results.get(i);
                        start = firstResult.getStartMillis();
                        zone = DateTimeZone.forID("America/Los_Angeles");
                        startTime = DateUtil.formatToZone(start, zone, DateUtil.MM_DD_YY);
                        buffer.append("<tr style=\"")
                                .append(cssClass.toLowerCase())
                                .append("\">")
                                .append("<td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\"><a style=\"font-weight:bold\" target=\"_blank\" "
                                        + "href=" + testresult + "/report.html#m\"")
                                .append(scenarioIndex)
                                .append("\">")
                                .append(methodName)
                                .append("</a></td> <td style=\"vertical-align:top;text-align:center\">" + exception + "</td>"
                                        + "<td style=\"vertical-align:top;text-align:center\">" + screenshot + "</td>")
                                .append("<td style=\"vertical-align:top;text-align:center\">").append(startTime).append("</td>")
                                .append("<td style=\"vertical-align:top;text-align:center\">").append(seconds)
                                .append("</td></tr>");
                        scenarioIndex++;
                    }

                    scenariosPerClass += resultsCount;
                    methodIndex++;
                }

                // Write the test results for the class
                writer.print("<tr style=\"");
                writer.print(cssClass);
                writer.print("\">");
                writer.print("<td style=\"vertical-align:top\" rowspan=\"");
                writer.print(scenariosPerClass);
                writer.print("\">");
                String cNameSubstring = classResult.getClassName().substring(classResult.getClassName().lastIndexOf(".") + 1,
                        classResult.getClassName().length());
                writer.print(Utils.escapeHtml(cNameSubstring));
                writer.print("</td>");
                writer.print(buffer);

                if (description != null && !description.equalsIgnoreCase("passed")) {
                    summaryWritter.print("<tr style=\"");
                    summaryWritter.print(cssClass);
                    summaryWritter.print("\">");
                    summaryWritter.print("<td style=\"vertical-align:top\" rowspan=\"");
                    summaryWritter.print(scenariosPerClass);
                    summaryWritter.print("\">");
                    summaryWritter.print(Utils.escapeHtml(cNameSubstring));
                    summaryWritter.print("</td>");
                    summaryWritter.print(buffer);
                }

                // classIndex++;
            }
            scenarioCount = scenarioIndex - startingScenarioIndex;
        }
        return scenarioCount;
    }

    /**
     * Writes the details for all test scenarios.
     */
    protected void writeScenarioDetails() {

        int scenarioIndex = 0;
        for (SuiteResult suiteResult : suiteResults) {
            for (TestResult testResult : suiteResult.getTestResults()) {

                scenarioIndex += writeScenarioDetails(testResult.getFailedConfigurationResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getFailedTestResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getSkippedConfigurationResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getSkippedTestResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getPassedTestResults(), scenarioIndex);
            }
        }
    }

    /**
     * Writes the scenario details for the results of a given state for a single
     * test.
     *
     * @param classResults          the class results
     * @param startingScenarioIndex the starting scenario index
     * @return the int
     */
    private int writeScenarioDetails(List<ClassResult> classResults, int startingScenarioIndex) {

        int scenarioIndex = startingScenarioIndex;
        for (ClassResult classResult : classResults) {
            String className = classResult.getClassName();
            for (MethodResult methodResult : classResult.getMethodResults()) {
                List<ITestResult> results = methodResult.getResults();
                assert !results.isEmpty();

                String label = Utils.escapeHtml(className + "#" + results.iterator().next().getMethod().getMethodName());
                for (ITestResult result : results) {
                    writeScenario(scenarioIndex, label, result);
                    scenarioIndex++;
                }
            }
        }
        return scenarioIndex - startingScenarioIndex;
    }

    /**
     * Writes the details for an individual test scenario. Param details
     *
     * @param scenarioIndex the scenario index
     * @param label         the label
     * @param result        the result
     */
    private void writeScenario(int scenarioIndex, String label, ITestResult result) {

        String methodName = result.getMethod().getMethodName();
        writer.print("<br/><br/><table style=\"border-collapse:collapse;empty-cells:show;border:1px solid #009;\" border=\"1px\" width=\"100%\">");

        // Write test parameters (if any)
        Object[] parameters = result.getParameters();
        int parameterCount = (parameters == null ? 0 : parameters.length);
        if (parameterCount > 0) {
            String colspan = "";
            if (parameters.length >= 2) {
                colspan = "colspan=\"" + parameters.length + "\"";
            }
        } else {

            String testresult = "testresult";
            writer.print("<tr>");
            writer.print("<th> <a style=\"font-weight:bold\" id=\"m" + scenarioIndex + "\" name=\"m" + scenarioIndex + "\">"
                    + methodName + "</a>");
            String errorMessage = "";
            if (result.getThrowable() != null) {
                errorMessage = result.getThrowable().getMessage();
            }

            writer.print("</th></tr><tr><td><br/>Test Skipped/Failed due to : <br/>" + errorMessage + "</td></tr>");
            writer.print("<tr><td><a target=\"_blank\" href="
                    + testresult + "/exception.html#" + methodName + ">Exception Details</a></td></tr>");
        }

        // Write reporter messages (if any)
        List<String> reporterMessages = Reporter.getOutput(result);
        if (!reporterMessages.isEmpty()) {
            writer.print("<tr><td style=\"vertical-align:top;text-align:center;background-color:#d8dde4;font-weight:bold\"");
            if (parameterCount > 1) {
                writer.print(" colspan=\"");
                writer.print(parameterCount);
                writer.print("\"");
            }
            writer.print(">Report</td></tr>");

            writer.print("<tr><td style=\"vertical-align:top\"");
            if (parameterCount > 1) {
                writer.print(" colspan=\"");
                writer.print(parameterCount);
                writer.print("\"");
            }
            writer.print(">");
            writeReporterMessages(reporterMessages);
            writer.print("</td></tr>");
        }

        // Write exception (if any)

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            exceptionWriter.print("<table id=" + methodName + "><tr><th>" + methodName + "</th></th><tr><th");
            if (parameterCount > 1) {
                exceptionWriter.print(" colspan=\"");
                exceptionWriter.print(parameterCount);
                exceptionWriter.print("\"");
            }
            exceptionWriter.print(">");
            exceptionWriter.print((result.getStatus() == ITestResult.SUCCESS ? "Expected Exception" : "Exception"));
            exceptionWriter.print("</th></tr>");

            exceptionWriter.print("<tr><td style=\"border:1px solid #009;padding:.25em .5em;vertical-align:top\"");
            if (parameterCount > 1) {
                exceptionWriter.print(" colspan=\"");
                exceptionWriter.print(parameterCount);
                exceptionWriter.print("\"");
            }
            exceptionWriter.print(">");
            writeStackTrace(throwable);
            exceptionWriter.print("</td></tr></table> <br/><br/>");
        }

        writer.print("</table>");
        writer.print("<p style=\"font-size:85%;text-align:center;border-bottom:1px solid #acc1bb\"><a style=\"font-weight:bold\" href=\"#summary\">back to summary</a></p>");
    }

    /**
     * Write reporter messages.
     *
     * @param reporterMessages the reporter messages
     */
    protected void writeReporterMessages(List<String> reporterMessages) {

        StringBuilder content = new StringBuilder();
        String timeContent = "";
        content.append("<div class=\"messages\">");
        Iterator<String> iterator = reporterMessages.iterator();
        assert iterator.hasNext();
        content.append(iterator.next());
        while (iterator.hasNext()) {
            String sValue = iterator.next();
            content.append("<br/>");
            if (!sValue.contains("############")) {
                content.append(sValue);
            } else {
                timeContent = sValue;
            }
        }
        content.append("</div>");

        if (timeContent != null && !timeContent.isEmpty()) {
            String startTime = timeContent.substring(0, timeContent.lastIndexOf("############"));
            String endTime = timeContent.substring(timeContent.lastIndexOf("############") + 12, timeContent.length());
            content.replace(0, 22, "<div class=\"messages\">" + startTime);
            content.append("<br/>" + endTime);
        }
        writer.print(content.toString());
    }

    /**
     * Write stack trace.
     *
     * @param throwable the throwable
     */
    protected void writeStackTrace(Throwable throwable) {

        exceptionWriter.print("<div style=\"white-space:pre;font-family:monospace\">");
        exceptionWriter.print(Utils.longStackTrace(throwable, true));
        exceptionWriter.print("</div>");
    }

    /**
     * Writes a TH element with the specified contents and CSS class names.
     *
     * @param html       the HTML contents
     * @param cssClasses the space-delimited CSS classes or null if there are no
     *                   classes to apply
     */
    protected void writeTableHeader(String html, String cssClasses) {

        writeTag("th", html, cssClasses);
    }

    /**
     * Writes a TD element with the specified contents.
     *
     * @param html the HTML contents
     */
    protected void writeTableData(String html) {

        writeTableData(html, null);
    }

    /**
     * Writes a TD element with the specified contents and CSS class names.
     *
     * @param html       the HTML contents
     * @param cssClasses the space-delimited CSS classes or null if there are no
     *                   classes to apply
     */
    protected void writeTableData(String html, String cssClasses) {

        writeTag("td", html, cssClasses);
    }

    /**
     * Writes an arbitrary HTML element with the specified contents and CSS
     * class names.
     *
     * @param tag        the tag name
     * @param html       the HTML contents
     * @param cssClasses the space-delimited CSS classes or null if there are no
     *                   classes to apply
     */
    protected void writeTag(String tag, String html, String cssClasses) {

        writer.print("<");
        writer.print(tag);
        if (cssClasses != null) {
            writer.print(" style=\"");
            writer.print(cssClasses);
            writer.print("\"");
        }
        writer.print(">");
        writer.print(html);
        writer.print("</");
        writer.print(tag);
        writer.print(">");
    }

    /**
     * Groups {@link com.user_management_API.automation.testng.CustomReport.TestResult}s by suite.
     *
     * @author $Author: vrajan $
     * @version $Rev: 395806 $ $Date: 2014-08-28 14:41:02 +0530 (Thu, 28 Aug
     * 2014) $
     */
    protected static class SuiteResult {

        private final String suiteName;
        private final List<TestResult> testResults = Lists.newArrayList();

        /**
         * Instantiates a new suite result.
         *
         * @param suite the suite
         */
        public SuiteResult(ISuite suite) {

            suiteName = suite.getName();
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                testResults.add(new TestResult(suiteResult.getTestContext()));
            }
        }

        /**
         * Gets the suite name.
         *
         * @return the suite name
         */
        public String getSuiteName() {

            return suiteName;
        }

        /**
         * Gets the test results.
         *
         * @return the test results (possibly empty)
         */
        public List<TestResult> getTestResults() {

            return testResults;
        }
    }

    /**
     * Groups {@link com.user_management_API.automation.testng.CustomReport.ClassResult}s by test, type (configuration or test), and
     * status.
     *
     * @author $Author: vrajan $
     * @version $Rev: 395806 $ $Date: 2014-08-28 14:41:02 +0530 (Thu, 28 Aug
     * 2014) $
     */
    protected static class TestResult {

        /**
         * Orders test results by class name and then by method name (in
         * lexicographic order).
         */
        protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {

            @Override
            public int compare(ITestResult o1, ITestResult o2) {

                int result = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
                if (result == 0) {
                    result = o1.getMethod().getMethodName().compareTo(o2.getMethod().getMethodName());
                }
                return result;
            }
        };

        private final String testName;
        private final List<ClassResult> failedConfigurationResults;
        private final List<ClassResult> failedTestResults;
        private final List<ClassResult> skippedConfigurationResults;
        private final List<ClassResult> skippedTestResults;
        private final List<ClassResult> passedTestResults;
        private final int failedTestCount;
        private final int skippedTestCount;
        private final int passedTestCount;
        private final long duration;
        private final String includedGroups;
        private final String excludedGroups;

        /**
         * Instantiates a new test result.
         *
         * @param context the context
         */
        public TestResult(ITestContext context) {

            testName = context.getName();

            Set<ITestResult> failedConfigurations = context.getFailedConfigurations().getAllResults();
            Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
            Set<ITestResult> skippedConfigurations = context.getSkippedConfigurations().getAllResults();
            Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
            Set<ITestResult> passedTests = context.getPassedTests().getAllResults();

            failedConfigurationResults = groupResults(failedConfigurations);
            failedTestResults = groupResults(failedTests);
            skippedConfigurationResults = groupResults(skippedConfigurations);
            skippedTestResults = groupResults(skippedTests);
            passedTestResults = groupResults(passedTests);

            failedTestCount = failedTests.size();
            skippedTestCount = skippedTests.size();
            passedTestCount = passedTests.size();

            duration = context.getEndDate().getTime() - context.getStartDate().getTime();

            includedGroups = formatGroups(context.getIncludedGroups());
            excludedGroups = formatGroups(context.getExcludedGroups());
        }

        /**
         * Groups test results by method and then by class.
         *
         * @param results the results
         * @return the list
         */
        protected List<ClassResult> groupResults(Set<ITestResult> results) {

            List<ClassResult> classResults = Lists.newArrayList();
            if (!results.isEmpty()) {
                List<MethodResult> resultsPerClass = Lists.newArrayList();
                List<ITestResult> resultsPerMethod = Lists.newArrayList();

                List<ITestResult> resultsList = Lists.newArrayList(results);
                Collections.sort(resultsList, RESULT_COMPARATOR);
                Iterator<ITestResult> resultsIterator = resultsList.iterator();
                assert resultsIterator.hasNext();

                ITestResult result = resultsIterator.next();
                resultsPerMethod.add(result);

                String previousClassName = result.getTestClass().getName();
                String previousMethodName = result.getMethod().getMethodName();
                while (resultsIterator.hasNext()) {
                    result = resultsIterator.next();

                    String className = result.getTestClass().getName();
                    if (!previousClassName.equals(className)) {
                        // Different class implies different method
                        assert !resultsPerMethod.isEmpty();
                        resultsPerClass.add(new MethodResult(resultsPerMethod));
                        resultsPerMethod = Lists.newArrayList();

                        assert !resultsPerClass.isEmpty();
                        classResults.add(new ClassResult(previousClassName, resultsPerClass));
                        resultsPerClass = Lists.newArrayList();

                        previousClassName = className;
                        previousMethodName = result.getMethod().getMethodName();
                    } else {
                        String methodName = result.getMethod().getMethodName();
                        if (!previousMethodName.equals(methodName)) {
                            assert !resultsPerMethod.isEmpty();
                            resultsPerClass.add(new MethodResult(resultsPerMethod));
                            resultsPerMethod = Lists.newArrayList();

                            previousMethodName = methodName;
                        }
                    }
                    resultsPerMethod.add(result);
                }
                assert !resultsPerMethod.isEmpty();
                resultsPerClass.add(new MethodResult(resultsPerMethod));
                assert !resultsPerClass.isEmpty();
                classResults.add(new ClassResult(previousClassName, resultsPerClass));
            }
            return classResults;
        }

        /**
         * Gets the test name.
         *
         * @return the test name
         */
        public String getTestName() {

            return testName;
        }

        /**
         * Gets the failed configuration results.
         *
         * @return the results for failed configurations (possibly empty)
         */
        public List<ClassResult> getFailedConfigurationResults() {

            return failedConfigurationResults;
        }

        /**
         * Gets the failed test results.
         *
         * @return the results for failed tests (possibly empty)
         */
        public List<ClassResult> getFailedTestResults() {

            return failedTestResults;
        }

        /**
         * Gets the skipped configuration results.
         *
         * @return the results for skipped configurations (possibly empty)
         */
        public List<ClassResult> getSkippedConfigurationResults() {

            return skippedConfigurationResults;
        }

        /**
         * Gets the skipped test results.
         *
         * @return the results for skipped tests (possibly empty)
         */
        public List<ClassResult> getSkippedTestResults() {

            return skippedTestResults;
        }

        /**
         * Gets the passed test results.
         *
         * @return the results for passed tests (possibly empty)
         */
        public List<ClassResult> getPassedTestResults() {

            return passedTestResults;
        }

        /**
         * Gets the failed test count.
         *
         * @return the failed test count
         */
        public int getFailedTestCount() {

            return failedTestCount;
        }

        /**
         * Gets the skipped test count.
         *
         * @return the skipped test count
         */
        public int getSkippedTestCount() {

            return skippedTestCount;
        }

        /**
         * Gets the passed test count.
         *
         * @return the passed test count
         */
        public int getPassedTestCount() {

            return passedTestCount;
        }

        /**
         * Gets the duration.
         *
         * @return the duration
         */
        public long getDuration() {

            return duration;
        }

        /**
         * Gets the included groups.
         *
         * @return the included groups
         */
        public String getIncludedGroups() {

            return includedGroups;
        }

        /**
         * Gets the excluded groups.
         *
         * @return the excluded groups
         */
        public String getExcludedGroups() {

            return excludedGroups;
        }

        /**
         * Formats an array of groups for display.
         *
         * @param groups the groups
         * @return the string
         */
        protected String formatGroups(String[] groups) {

            if (groups.length == 0) {
                return "";
            }

            StringBuilder builder = new StringBuilder();
            builder.append(groups[0]);
            for (int i = 1; i < groups.length; i++) {
                builder.append(", ").append(groups[i]);
            }
            return builder.toString();
        }
    }

    /**
     * Groups {@link com.user_management_API.automation.testng.CustomReport.MethodResult}s by class.
     *
     * @author $Author: vrajan $
     * @version $Rev: 395806 $ $Date: 2014-08-28 14:41:02 +0530 (Thu, 28 Aug
     * 2014) $
     */
    protected static class ClassResult {

        private final String className;
        private final List<MethodResult> methodResults;

        /**
         * Instantiates a new class result.
         *
         * @param className     the class name
         * @param methodResults the non-null, non-empty {@link com.user_management_API.automation.testng.CustomReport.MethodResult}
         *                      list
         */
        public ClassResult(String className, List<MethodResult> methodResults) {

            this.className = className;
            this.methodResults = methodResults;
        }

        /**
         * Gets the class name.
         *
         * @return the class name
         */
        public String getClassName() {

            return className;
        }

        /**
         * Gets the method results.
         *
         * @return the non-null, non-empty {@link com.user_management_API.automation.testng.CustomReport.MethodResult} list
         */
        public List<MethodResult> getMethodResults() {

            return methodResults;
        }
    }

    /**
     * Groups test results by method.
     *
     * @author $Author: vrajan $
     * @version $Rev: 395806 $ $Date: 2014-08-28 14:41:02 +0530 (Thu, 28 Aug
     * 2014) $
     */
    protected static class MethodResult {

        private final List<ITestResult> results;

        /**
         * Instantiates a new method result.
         *
         * @param results the non-null, non-empty result list
         */
        public MethodResult(List<ITestResult> results) {

            this.results = results;
        }

        /**
         * Gets the results.
         *
         * @return the non-null, non-empty result list
         */
        public List<ITestResult> getResults() {

            return results;
        }
    }

    /**
     * Calculate time.
     *
     * @param duration the duration
     * @return the string
     */
    private String calculateTime(long duration) {

        StringBuilder result = new StringBuilder();

        long diffSeconds = duration / 1000 % 60;
        long diffMinutes = duration / (60 * 1000) % 60;

        if (diffMinutes > 0) {
            if (diffMinutes > 1) {
                result.append(diffMinutes + "mins");
            } else {
                result.append(diffMinutes + "min");
            }
        }

        if (diffSeconds > 0) {
            if (diffMinutes > 0)
                result.append(" ");

            if (diffSeconds > 1) {
                result.append(diffSeconds + "secs");
            } else {
                result.append(diffSeconds + "sec");
            }
        }

        return result.toString();
    }
}
