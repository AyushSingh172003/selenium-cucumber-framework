package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton — reads config.properties exactly ONCE.
 * Every other class asks ConfigReader.getInstance() for values
 * instead of reading the file itself.
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);

    // The single shared instance
    private static ConfigReader instance;

    private final Properties props;

    // Private constructor — nobody outside this class can call "new ConfigReader()"
    private ConfigReader() {
        props = new Properties();
        String path = "src/test/resources/config/config.properties";

        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
            log.info("Config loaded successfully from: {}", path);
        } catch (IOException e) {
            log.error("FATAL: Could not load config.properties from: {}", path, e);
            throw new RuntimeException("Missing config.properties at: " + path, e);
        }
    }

    // Thread-safe lazy singleton accessor
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    // ── GETTERS ────────────────────────────────────────────────

    public String getBrowser() {
        // System property (from Maven -Dbrowser=) OVERRIDES the file value.
        // This is how Jenkins controls the browser without editing files.
        return System.getProperty("browser", props.getProperty("browser", "chrome"));
    }

    public boolean isHeadless() {
        String value = System.getProperty("headless", props.getProperty("headless", "false"));
        return Boolean.parseBoolean(value);
    }

    public String getBaseUrl() {
        return getRequired("baseUrl");
    }

    public int getImplicitWait() {
        return getInt("implicitWait", 5);
    }

    public int getExplicitWait() {
        return getInt("explicitWait", 20);
    }

    public int getPageLoadTimeout() {
        return getInt("pageLoadTimeout", 30);
    }

    public String getValidUsername() {
        return getRequired("validUsername");
    }

    public String getValidPassword() {
        return getRequired("validPassword");
    }

    public String getLockedUsername() {
        return props.getProperty("lockedUsername", "");
    }

    public String getScreenshotPath() {
        return props.getProperty("screenshotPath", "test-output/screenshots/");
    }

    public String getReportPath() {
        return props.getProperty("reportPath", "test-output/reports/");
    }

    public String getExcelPath() {
        return props.getProperty("excelPath", "testdata/TestData.xlsx");
    }

    // ── PRIVATE HELPERS ────────────────────────────────────────

    private String getRequired(String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing required config key: " + key);
        }
        return value.trim();
    }

    private int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid integer for key '{}', using default {}", key, defaultValue);
            return defaultValue;
        }
    }
}