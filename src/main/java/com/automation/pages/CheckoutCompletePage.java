package com.automation.pages;

import com.automation.bases.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "complete-header")
    private WebElement successMessage;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText().equals("Checkout: Complete!");
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }

    public boolean isOrderSuccessful() {
        return getSuccessMessage()
                .equalsIgnoreCase("Thank you for your order!");
    }
}