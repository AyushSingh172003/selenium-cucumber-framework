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
                    new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date());

            String fileName =
                    status + "_" +
                            scenarioName.replaceAll("[^a-zA-Z0-9]", "_") +
                            "_" + timestamp + ".png";

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