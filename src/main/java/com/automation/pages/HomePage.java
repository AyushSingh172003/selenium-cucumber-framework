package com.automation.pages;

import com.automation.bases.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            boolean loaded = pageTitle.getText().equalsIgnoreCase("Products");
            log.info("Home page loaded: {}", loaded);
            return loaded;
        } catch (Exception e) {
            log.error("Home page failed to load: {}", e.getMessage());
            return false;
        }
    }

    public String getPageHeading() {
        return getText(pageTitle);
    }
}