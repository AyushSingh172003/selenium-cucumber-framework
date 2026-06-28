package com.automation.pages;

import com.automation.bases.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText().equals("Checkout: Your Information");
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

    /**
     * Attempts to submit checkout information.
     * Returns true if validation passed (page navigated forward).
     * Returns false if validation failed (error message shown, still
     * on this page). Callers MUST check the return value — this no
     * longer silently returns null, which previously caused a
     * NullPointerException risk on any caller chaining a method
     * straight off the result.
     */
    public boolean enterCheckoutInformation(
            String firstName,
            String lastName,
            String postalCode) {

        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);

        click(continueButton);

        return !isErrorDisplayed();
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}