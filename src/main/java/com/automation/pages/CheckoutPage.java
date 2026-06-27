package com.automation.pages;

import com.automation.bases.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return getPageTitle().contains("Swag Labs")
                && pageTitle.getText().equals("Checkout: Your Information");
    }

    public CheckoutPage enterFirstName(String firstName) {
        type(firstNameField, firstName);
        return this;
    }

    public CheckoutPage enterLastName(String lastName) {
        type(lastNameField, lastName);
        return this;
    }

    public CheckoutPage enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
        return this;
    }

    public CheckoutOverviewPage continueCheckout() {
        click(continueButton);
        return new CheckoutOverviewPage(driver);
    }

    public CheckoutOverviewPage enterCheckoutInformation(
            String firstName,
            String lastName,
            String postalCode) {

        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);

        click(continueButton);

        if (isErrorDisplayed()) {
            return null;
        }

        return new CheckoutOverviewPage(driver);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}