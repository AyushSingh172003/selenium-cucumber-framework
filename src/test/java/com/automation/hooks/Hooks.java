package com.automation.hooks;

import com.automation.factory.DriverFactory;
import com.automation.listeners.ExtentManager;
import com.automation.utils.ConfigReader;
import com.automation.utils.ScreenshotUtil;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        log.info("════════════════════════════════════════════");
        log.info("STARTING SCENARIO: {}", scenario.getName());
        log.info("════════════════════════════════════════════");

        ExtentManager.startTest(scenario.getName());

        WebDriver driver = DriverFactory.initDriver();
        String baseUrl = ConfigReader.getInstance().getBaseUrl();
        driver.get(baseUrl);
        log.info("Navigated to base URL: {}", baseUrl);
    }


    @After
    public void tearDown(Scenario scenario) {
        String screenshotPath;
        if (scenario.isFailed()) {
            screenshotPath = ScreenshotUtil.captureScreenshot(
                    scenario.getName(),
                    "FAILED"
            );

            log.error("SCENARIO FAILED: {}", scenario.getName());

            ExtentManager.getTest()
                    .fail("Scenario failed: " + scenario.getName())
                    .addScreenCaptureFromPath(screenshotPath);

        } else {
            screenshotPath = ScreenshotUtil.captureScreenshot(
                    scenario.getName(),
                    "PASSED"
            );

            log.info("SCENARIO PASSED: {}", scenario.getName());

            ExtentManager.getTest()
                    .pass("Scenario passed: " + scenario.getName())
                    .addScreenCaptureFromPath(screenshotPath);
        }
        DriverFactory.quitDriver();

        log.info("════════════════════════════════════════════\n");
    }

    // Runs ONCE after the entire test run completes — flushes the report
    @AfterAll
    public static void afterAllScenarios() {
        ExtentManager.flush();
    }
}