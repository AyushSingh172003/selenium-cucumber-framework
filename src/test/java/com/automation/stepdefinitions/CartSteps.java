package com.automation.stepdefinitions;

import com.automation.pages.CartPage;
import com.automation.pages.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * Owns all cart-navigation state (cartPage). Previously this state
 * was duplicated across ProductSteps and CheckoutSteps independently,
 * which worked only by step-ordering coincidence. Now there is a
 * single source of truth, injected wherever cart state is needed.
 */
public class CartSteps {

    private static final Logger log = LogManager.getLogger(CartSteps.class);

    private final ProductSteps productSteps;
    private CartPage cartPage;

    public CartSteps(ProductSteps productSteps) {
        this.productSteps = productSteps;
    }

    public CartPage getCartPage() {
        return cartPage;
    }

    @And("the user navigates to the cart")
    public void the_user_navigates_to_the_cart() {
        HomePage homePage = productSteps.getHomePage();
        cartPage = homePage.goToCart();

        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page did not load");
        log.info("Navigated to cart page");
    }

    @Then("the cart should contain {string}")
    public void the_cart_should_contain(String productName) {
        if (cartPage == null) {
            // Be forgiving if the scenario didn't explicitly navigate
            // to the cart first — navigate now rather than NPE.
            the_user_navigates_to_the_cart();
        }

        Assert.assertTrue(
                cartPage.isProductInCart(productName),
                productName + " not found in cart"
        );
        log.info("Verified cart contains: {}", productName);
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
}