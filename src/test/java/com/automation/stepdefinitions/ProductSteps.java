package com.automation.stepdefinitions;

import com.automation.factory.DriverFactory;
import com.automation.pages.CartPage;
import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class ProductSteps {

    private static final Logger log = LogManager.getLogger(ProductSteps.class);

    private HomePage homePage;
    private CartPage cartPage;

    public HomePage getHomePage() {
        return homePage;
    }

    @Given("the user is logged in with valid credentials")
    public void the_user_is_logged_in_with_valid_credentials() {
        ConfigReader config = ConfigReader.getInstance();

        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());

        homePage = loginPage.login(
                config.getValidUsername(),
                config.getValidPassword()
        );

        Assert.assertTrue(
                homePage.isPageLoaded(),
                "Home page did not load after login"
        );

        log.info("User logged in successfully");
    }

    @Then("the product count should be {int}")
    public void the_product_count_should_be(int expectedCount) {
        int actual = homePage.getProductCount();

        Assert.assertEquals(
                actual,
                expectedCount,
                "Product count mismatch"
        );

        log.info("Verified product count: {}", actual);
    }

    @When("the user sorts products by {string}")
    public void the_user_sorts_products_by(String sortOption) {
        homePage.sortProductsBy(sortOption);

        log.info("Sorted products by: {}", sortOption);
    }

    @Then("the product prices should be in ascending order")
    public void the_product_prices_should_be_in_ascending_order() {
        List<Double> prices = homePage.getAllProductPricesAsDouble();
        List<Double> sortedCopy = prices.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(
                prices,
                sortedCopy,
                "Prices are not in ascending order: " + prices
        );

        log.info("Verified ascending price order: {}", prices);
    }

    @Then("the first product name should be {string}")
    public void the_first_product_name_should_be(String expectedName) {
        List<String> names = homePage.getAllProductNames();

        Assert.assertEquals(
                names.get(0),
                expectedName,
                "First product name mismatch"
        );

        log.info("Verified first product name: {}", names.get(0));
    }

    @When("the user adds {string} to the cart")
    public void the_user_adds_to_the_cart(String productName) {
        homePage.addProductToCart(productName);

        log.info("Added product to cart: {}", productName);
    }

    @When("the user removes {string} from the cart")
    public void the_user_removes_from_the_cart(String productName) {
        homePage.removeProductFromCart(productName);

        log.info("Removed product from cart: {}", productName);
    }

    @Then("the cart badge should show {string}")
    public void the_cart_badge_should_show(String expectedCount) {
        String actual = homePage.getCartBadgeCount();

        Assert.assertEquals(
                actual,
                expectedCount,
                "Cart badge count mismatch"
        );

        log.info("Verified cart badge count: {}", actual);
    }

    @When("the user removes {string} from the cart page")
    public void the_user_removes_from_the_cart_page(String productName) {
        cartPage.removeProduct(productName);

        log.info("Removed product from cart page: {}", productName);
    }

    @Then("the cart should be empty")
    public void the_cart_should_be_empty() {
        Assert.assertEquals(
                cartPage.getCartItemCount(),
                0,
                "Cart is not empty"
        );

        log.info("Verified cart is empty");
    }

    @And("the cart should contain {string}")
    public void the_cart_should_contain(String productName) {
        cartPage = homePage.goToCart();

        Assert.assertTrue(
                cartPage.isProductInCart(productName),
                productName + " not found in cart"
        );

        log.info("Verified cart contains: {}", productName);
    }
}