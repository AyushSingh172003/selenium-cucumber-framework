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
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {

        log.info("════════════════════════════════════════════");
        log.info("STARTING SCENARIO: {}", scenario.getName());
        log.info("════════════════════════════════════════════");

        ExtentManager.startTest(scenario.getName());

        WebDriver driver = DriverFactory.initDriver();

        String baseUrl =
                ConfigReader.getInstance()
                        .getBaseUrl();

        driver.get(baseUrl);

        log.info(
                "Navigated to base URL: {}",
                baseUrl
        );
    }

    @After
    public void tearDown(Scenario scenario) {

        String screenshotPath;

        try {

            screenshotPath =
                    ScreenshotUtil.captureScreenshot(
                            scenario.getName(),
                            scenario.isFailed()
                                    ? "FAILED"
                                    : "PASSED"
                    );

            // Attach screenshot to Allure report
            Allure.addAttachment(
                    scenario.getName(),
                    new ByteArrayInputStream(
                            ((TakesScreenshot)
                                    DriverFactory.getDriver())
                                    .getScreenshotAs(
                                            OutputType.BYTES
                                    )
                    )
            );

            if (scenario.isFailed()) {

                log.error(
                        "SCENARIO FAILED: {}",
                        scenario.getName()
                );

                ExtentManager.getTest()
                        .fail(
                                "Scenario failed: "
                                        + scenario.getName()
                        )
                        .addScreenCaptureFromPath(
                                screenshotPath
                        );

            } else {

                log.info(
                        "SCENARIO PASSED: {}",
                        scenario.getName()
                );

                ExtentManager.getTest()
                        .pass(
                                "Scenario passed: "
                                        + scenario.getName()
                        )
                        .addScreenCaptureFromPath(
                                screenshotPath
                        );
            }

        } finally {

            DriverFactory.quitDriver();

            log.info(
                    "════════════════════════════════════════════\n"
            );
        }
    }

    @AfterAll
    public static void afterAllScenarios() {

        ExtentManager.flush();
    }
}