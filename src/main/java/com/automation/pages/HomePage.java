package com.automation.pages;

import com.automation.bases.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    // Standard HTML <select> — Select class applies here
    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    // By locators for repeated/dynamic elements — found fresh each call
    private final By productNames  = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By addToCartBtns = By.cssSelector("[data-test^='add-to-cart']");
    private final By cartBadge     = By.className("shopping_cart_badge");

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

    // ── DROPDOWN — Select class ────────────────────────────────
    public HomePage sortProductsBy(String visibleText) {
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(visibleText);
        log.info("Sorted products by: {}", visibleText);
        return this;
    }

    public String getCurrentSortOption() {
        Select select = new Select(sortDropdown);
        return select.getFirstSelectedOption().getText();
    }

    // ── LIST OF WEBELEMENTS — read all product names/prices ────
    public List<String> getAllProductNames() {
        List<String> names = driver.findElements(productNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        log.info("Found {} product names", names.size());
        return names;
    }

    public List<Double> getAllProductPricesAsDouble() {
        List<Double> prices = driver.findElements(productPrices).stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .collect(Collectors.toList());
        log.info("Found {} product prices: {}", prices.size(), prices);
        return prices;
    }

    public int getProductCount() {
        return driver.findElements(productNames).size();
    }

    // ── ADD TO CART by product name — dynamic locator ──────────
    public HomePage addProductToCart(String productName) {
        String dataTestSuffix = productName.toLowerCase()
                .replace(" ", "-")
                .replace("(", "")
                .replace(")", "")
                .replace(".", "");
        WebElement addButton = driver.findElement(
                By.cssSelector("[data-test='add-to-cart-" + dataTestSuffix + "']"));
        click(addButton);
        log.info("Added to cart: {}", productName);
        return this;
    }

    public String getCartBadgeCount() {
        try {
            return driver.findElement(cartBadge).getText();
        } catch (Exception e) {
            return "0"; // badge doesn't exist when cart is empty
        }
    }

    public CartPage goToCart() {
        click(cartIcon);
        return new CartPage(driver);
    }
}