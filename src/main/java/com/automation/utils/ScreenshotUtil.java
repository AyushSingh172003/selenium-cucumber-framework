package com.automation.utils;

import com.automation.factory.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String captureScreenshot(String scenarioName, String status) {

        try {

            String timestamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")
                            .format(new Date());

            // Thread ID disambiguates filenames when parallel scenarios
            // finish within the same millisecond AND share an identical
            // sanitized name (common with Scenario Outline rows).
            // Without this, REPLACE_EXISTING can silently overwrite one
            // thread's screenshot with another's.
            long threadId = Thread.currentThread().getId();

            String fileName =
                    status + "_" +
                            scenarioName.replaceAll("[^a-zA-Z0-9]", "_") +
                            "_" + timestamp +
                            "_t" + threadId +
                            ".png";

            String path =
                    ConfigReader.getInstance().getScreenshotPath()
                            + fileName;

            File source = ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.FILE);

            Path destination = Paths.get(path);

            Files.createDirectories(destination.getParent());

            Files.copy(
                    source.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return path;

        } catch (Exception e) {
            throw new RuntimeException("Unable to capture screenshot", e);
        }
    }
}