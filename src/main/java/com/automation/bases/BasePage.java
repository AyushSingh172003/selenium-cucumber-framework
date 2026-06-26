package com.automation.bases;

import com.automation.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Every Page Object extends this class.
 * Provides PageFactory init, explicit wait, and reusable safe actions
 * so individual page classes never call raw Selenium methods directly.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getInstance().getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    // Every page must define how to verify it has loaded
    public abstract boolean isPageLoaded();

    // ── CORE ACTIONS ──────────────────────────────────────────

    protected void click(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            log.info("Clicked element: {}", describe(element));
        } catch (ElementClickInterceptedException e) {
            log.warn("Click intercepted — falling back to JS click on: {}", describe(element));
            jsClick(element);
        }
    }

    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
        log.info("Typed '{}' into: {}", text, describe(element));
    }

    protected String getText(WebElement element) {
        String text = wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
        log.info("Read text '{}' from: {}", text, describe(element));
        return text;
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected List<String> getAllTexts(By locator) {
        return driver.findElements(locator).stream()
                .map(el -> el.getText().trim())
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());
    }

    // ── JAVASCRIPT FALLBACKS ──────────────────────────────────

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        log.info("JS-clicked element: {}", describe(element));
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    // ── UTILITY ────────────────────────────────────────────────

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    private String describe(WebElement element) {
        try {

            String id = element.getAttribute("id");
            if (id != null && !id.isBlank()) {
                return "id=" + id;
            }

            String name = element.getAttribute("name");
            if (name != null && !name.isBlank()) {
                return "name=" + name;
            }

            String className = element.getAttribute("class");
            if (className != null && !className.isBlank()) {
                return "class=" + className;
            }

            String text = element.getText();
            if (text != null && !text.isBlank()) {
                return "text=" + text;
            }
            return "tag=" + element.getTagName();

        } catch (StaleElementReferenceException e) {
            return "stale-element";
        } catch (Exception e) {
            return "unknown-element";
        }
    }
}