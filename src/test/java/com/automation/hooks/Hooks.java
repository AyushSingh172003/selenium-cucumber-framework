package com.automation.hooks;

import com.automation.factory.DriverFactory;
import com.automation.listeners.ExtentManager;
import com.automation.utils.ConfigReader;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
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

    @AfterStep
    public void afterEachStep(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());

            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            if (scenario.isFailed()) {
                ExtentManager.getTest().log(Status.FAIL, "Step failed")
                        .addScreenCaptureFromBase64String(base64);
                log.error("Step failed in scenario: {}", scenario.getName());
            } else {
                ExtentManager.getTest().log(Status.PASS, "Step passed")
                        .addScreenCaptureFromBase64String(base64);
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("SCENARIO FAILED: {}", scenario.getName());
            ExtentManager.getTest().fail("Scenario failed: " + scenario.getName());
        } else {
            log.info("SCENARIO PASSED: {}", scenario.getName());
            ExtentManager.getTest().pass("Scenario passed: " + scenario.getName());
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