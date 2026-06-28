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
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By addToCartBtns = By.cssSelector("[data-test^='add-to-cart']");
    private final By cartBadge = By.className("shopping_cart_badge");

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

    // ──────────────────────────────────────────────
    // Sorting
    // ──────────────────────────────────────────────

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

    // ──────────────────────────────────────────────
    // Product Details
    // ──────────────────────────────────────────────

    public List<String> getAllProductNames() {
        List<String> names = driver.findElements(productNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        log.info("Found {} product names", names.size());
        return names;
    }

    public List<Double> getAllProductPricesAsDouble() {
        List<Double> prices = driver.findElements(productPrices).stream()
                .map(price -> Double.parseDouble(price.getText().replace("$", "")))
                .collect(Collectors.toList());
        log.info("Found {} product prices: {}", prices.size(), prices);
        return prices;
    }

    public int getProductCount() {
        return driver.findElements(productNames).size();
    }

    public MenuPage openMenu() {
        click(menuButton);
        MenuPage menuPage = new MenuPage(driver);
        Assert.assertTrue(menuPage.isPageLoaded(), "Hamburger menu failed to open");
        log.info("Opened hamburger menu");
        return menuPage;
    }

    // ──────────────────────────────────────────────
    // Cart Actions
    // ──────────────────────────────────────────────

    public HomePage addProductToCart(String productName) {
        WebElement addButton = driver.findElement(
                By.cssSelector("[data-test='add-to-cart-" + slugify(productName) + "']"));
        click(addButton);
        log.info("Added product to cart: {}", productName);
        return this;
    }

    public HomePage removeProductFromCart(String productName) {
        WebElement removeButton = driver.findElement(
                By.cssSelector("[data-test='remove-" + slugify(productName) + "']"));
        click(removeButton);
        log.info("Removed product from cart: {}", productName);
        return this;
    }

    public String getCartBadgeCount() {
        try {
            return driver.findElement(cartBadge).getText();
        } catch (Exception e) {
            return "0";
        }
    }

    public boolean isCartEmpty() {
        return getCartBadgeCount().equals("0");
    }

    public CartPage goToCart() {
        click(cartIcon);
        log.info("Navigated to cart page");
        return new CartPage(driver);
    }

    // ──────────────────────────────────────────────
    // Validation Helpers
    // ──────────────────────────────────────────────

    public boolean isProductDisplayed(String productName) {
        return getAllProductNames().contains(productName);
    }

    public boolean isProductAddedToCart(String productName) {
        try {
            driver.findElement(By.cssSelector("[data-test='remove-" + slugify(productName) + "']"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}