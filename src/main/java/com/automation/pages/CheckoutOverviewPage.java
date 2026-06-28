package com.automation.pages;

import com.automation.bases.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutOverviewPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "finish")
    private WebElement finishButton;

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText().equals("Checkout: Overview");
    }

    public CheckoutCompletePage finishOrder() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }
}