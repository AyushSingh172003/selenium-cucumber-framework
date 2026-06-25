package com.automation.pages;

import com.automation.bases.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    // ── LOCATORS (saucedemo.com) ───────────────────────────────
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginButton));
            log.info("Login page loaded successfully");
            return loginButton.isDisplayed();
        } catch (Exception e) {
            log.error("Login page failed to load: {}", e.getMessage());
            return false;
        }
    }

    // ── ACTIONS ────────────────────────────────────────────────

    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public HomePage clickLoginButton() {
        click(loginButton);
        return new HomePage(driver);
    }

    // Fluent login — returns HomePage (Page Chaining)
    public HomePage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }

    // For negative-test scenarios — stays on LoginPage so the test
    // can assert the error message
    public LoginPage loginExpectingFailure(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
        return this;
    }

    public String getErrorMessage() {
        String error = getText(errorMessage);
        log.warn("Login error displayed: {}", error);
        return error;
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}