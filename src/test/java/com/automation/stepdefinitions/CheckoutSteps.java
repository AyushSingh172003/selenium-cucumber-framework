package com.automation.stepdefinitions;

import com.automation.pages.CartPage;
import com.automation.pages.CheckoutCompletePage;
import com.automation.pages.CheckoutOverviewPage;
import com.automation.pages.CheckoutPage;
import com.automation.pages.HomePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.Map;

public class CheckoutSteps {

    private static final Logger log = LogManager.getLogger(CheckoutSteps.class);

    private final ProductSteps productSteps;

    private HomePage homePage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;

    public CheckoutSteps(ProductSteps productSteps) {
        this.productSteps = productSteps;
    }

    @And("the user navigates to the cart")
    public void the_user_navigates_to_the_cart() {
        homePage = productSteps.getHomePage();

        cartPage = homePage.goToCart();

        Assert.assertTrue(
                cartPage.isPageLoaded(),
                "Cart page did not load"
        );

        log.info("Navigated to cart page");
    }

    @And("the user proceeds to checkout")
    public void the_user_proceeds_to_checkout() {
        checkoutPage = cartPage.clickCheckout();

        Assert.assertTrue(
                checkoutPage.isPageLoaded(),
                "Checkout page did not load"
        );

        log.info("Navigated to checkout information page");
    }

    @When("the user enters checkout information:")
    public void the_user_enters_checkout_information(DataTable dataTable) {

        Map<String, String> data =
                dataTable.asMap(String.class, String.class);

        checkoutOverviewPage =
                checkoutPage.enterCheckoutInformation(
                        data.get("firstName"),
                        data.get("lastName"),
                        data.get("postalCode")
                );

        log.info(
                "Entered checkout information successfully"
        );
    }

    @And("the user completes the checkout")
    public void the_user_completes_the_checkout() {

        checkoutCompletePage =
                checkoutOverviewPage.finishOrder();

        Assert.assertTrue(
                checkoutCompletePage.isPageLoaded(),
                "Checkout complete page did not load"
        );

        log.info("Order completed successfully");
    }

    @Then("the order should be placed successfully")
    public void the_order_should_be_placed_successfully() {

        Assert.assertTrue(
                checkoutCompletePage.isOrderSuccessful(),
                "Order completion message mismatch"
        );

        log.info(
                "Verified successful order placement"
        );
    }

    @Then("a checkout error message should be displayed")
    public void a_checkout_error_message_should_be_displayed() {

        Assert.assertTrue(
                checkoutPage.isErrorDisplayed(),
                "Checkout error message was not displayed"
        );

        log.info(
                "Verified checkout error message is displayed"
        );
    }

    @Then("the checkout error message should contain {string}")
    public void the_checkout_error_message_should_contain(
            String expectedMessage
    ) {

        String actualMessage =
                checkoutPage.getErrorMessage();

        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected error message to contain: "
                        + expectedMessage
                        + " but actual message was: "
                        + actualMessage
        );

        log.info(
                "Verified checkout error message contains: {}",
                expectedMessage
        );
    }
}