package com.automation.factory;

import com.automation.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates and configures the WebDriver instance.
 * ThreadLocal so that future parallel execution (multiple Cucumber
 * scenarios at once) never lets two threads share one browser.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    // Each thread gets its own driver instance
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverFactory() {
        // Utility class
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static WebDriver initDriver() {

        String browser = ConfigReader.getInstance().getBrowser();
        boolean headless = ConfigReader.getInstance().isHeadless();

        log.info("Initializing browser: {} | Headless: {}", browser, headless);

        WebDriver driver = createDriver(browser, headless);

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInstance().getImplicitWait()));

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInstance().getPageLoadTimeout()));

        driverThread.set(driver);

        log.info("Driver initialized and stored for thread: {}",
                Thread.currentThread().getId());

        return driver;
    }

    private static WebDriver createDriver(String browser, boolean headless) {

        switch (browser.toLowerCase().trim()) {

            case "chrome":

                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                // Existing options
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");

                // Disable password manager and password leak warning popup
                Map<String, Object> prefs = new HashMap<>();

                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);

                chromeOptions.setExperimentalOption("prefs", prefs);

                chromeOptions.addArguments(
                        "--disable-features=PasswordLeakDetection");

                // Headless mode
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--disable-gpu");
                }

                return new ChromeDriver(chromeOptions);

            case "firefox":

                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }

                return new FirefoxDriver(firefoxOptions);

            case "edge":

                WebDriverManager.edgedriver().setup();

                return new EdgeDriver();

            default:

                log.error("Unsupported browser requested: {}", browser);

                throw new IllegalArgumentException(
                        "Browser not supported: "
                                + browser
                                + ". Use chrome / firefox / edge");
        }
    }

    public static void quitDriver() {

        WebDriver driver = getDriver();

        if (driver != null) {
            driver.quit();
            driverThread.remove();

            log.info(
                    "Driver quit and removed for thread: {}",
                    Thread.currentThread().getId()
            );
        }
    }
}