package com.automation.pages;

import com.automation.bases.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CartPage.class);

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    private final By cartItemNames = By.className("inventory_item_name");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            boolean loaded = pageTitle.getText().equalsIgnoreCase("Your Cart");
            log.info("Cart page loaded: {}", loaded);
            return loaded;
        } catch (Exception e) {
            log.error("Cart page failed to load: {}", e.getMessage());
            return false;
        }
    }

    public List<String> getCartItemNames() {
        return driver.findElements(cartItemNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int getCartItemCount() {
        return driver.findElements(cartItemNames).size();
    }

    public boolean isProductInCart(String productName) {
        boolean present = getCartItemNames().contains(productName);
        log.info("Product '{}' present in cart: {}", productName, present);
        return present;
    }

    public CartPage removeProduct(String productName) {
        WebElement removeButton = driver.findElement(
                By.cssSelector("[data-test='remove-" + slugify(productName) + "']"));
        click(removeButton);
        log.info("Removed product from cart page: {}", productName);
        return this;
    }

    public CheckoutPage clickCheckout() {
        click(checkoutButton);
        log.info("Clicked checkout button");
        return new CheckoutPage(driver);
    }

    public HomePage clickContinueShopping() {
        click(continueShoppingButton);
        log.info("Clicked continue shopping button");
        return new HomePage(driver);
    }
}