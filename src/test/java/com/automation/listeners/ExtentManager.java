package com.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Manually wired ExtentReports setup — bypasses the Cucumber
 * plugin auto-discovery mechanism entirely. We control every
 * step ourselves, so failures are visible, not silent.
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = "test-output/reports/SparkReport.html";

            // Ensure the directory genuinely exists before ExtentReports
            // tries to write into it
            File reportDir = new File("test-output/reports");
            if (!reportDir.exists()) {
                boolean created = reportDir.mkdirs();
                System.out.println("[ExtentManager] Created report dir: " + created);
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Selenium Cucumber BDD Framework");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));

            System.out.println("[ExtentManager] ExtentReports initialized. Output: "
                    + new File(reportPath).getAbsolutePath());
        }
        return extent;
    }

    public static void startTest(String name) {
        ExtentTest test = getInstance().createTest(name);
        currentTest.set(test);
    }

    public static ExtentTest getTest() {
        return currentTest.get();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
            currentTest.remove();

            System.out.println(
                    "[ExtentManager] Report flushed to disk."
            );
        }
    }
}