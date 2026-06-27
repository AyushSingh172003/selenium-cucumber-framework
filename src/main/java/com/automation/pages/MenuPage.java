package com.automation.pages;

import com.automation.bases.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MenuPage extends BasePage {

    private static final Logger log = LogManager.getLogger(MenuPage.class);

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;

    public MenuPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logoutLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginPage logout() {

        click(logoutLink);

        LoginPage loginPage = new LoginPage(driver);

        if (!loginPage.isPageLoaded()) {
            throw new RuntimeException("Logout failed - Login page was not displayed");
        }

        log.info("User logged out successfully");

        return loginPage;
    }

    public HomePage resetAppState() {

        click(resetAppStateLink);

        log.info("Reset application state");

        return new HomePage(driver);
    }

    public void clickAbout() {

        click(aboutLink);

        log.info("Clicked About");
    }
}