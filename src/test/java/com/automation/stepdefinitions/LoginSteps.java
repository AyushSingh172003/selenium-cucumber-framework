package com.automation.stepdefinitions;

import com.automation.factory.DriverFactory;
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

public class LoginSteps {

    private static final Logger log = LogManager.getLogger(LoginSteps.class);

    private LoginPage loginPage;
    private HomePage homePage;

    // ── BACKGROUND STEP ────────────────────────────────────────
    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page did not load");
        log.info("Navigated to and verified login page");
    }

    // ── ACTIONS ────────────────────────────────────────────────
    @When("the user enters username {string} and password {string}")
    public void the_user_enters_username_and_password(String username, String password) {
        loginPage.enterUsername(username).enterPassword(password);
        log.info("Entered credentials — username: {}", username);
    }

    @And("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        homePage = loginPage.clickLoginButton();
        log.info("Clicked login button");
    }

    // ── ASSERTIONS (positive flow) ─────────────────────────────
    @Then("the user should be navigated to the home page")
    public void the_user_should_be_navigated_to_the_home_page() {
        Assert.assertTrue(homePage.isPageLoaded(), "Home page did not load after login");
        log.info("Verified home page loaded");
    }

    @And("the page heading should be {string}")
    public void the_page_heading_should_be(String expectedHeading) {
        String actualHeading = homePage.getPageHeading();
        Assert.assertEquals(actualHeading, expectedHeading,
                "Page heading mismatch");
        log.info("Verified page heading: {}", actualHeading);
    }

    // ── ASSERTIONS (negative flow) ─────────────────────────────
    @Then("an error message should be displayed")
    public void an_error_message_should_be_displayed() {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message was not displayed");
        log.info("Verified error message is displayed");
    }

    @And("the error message should contain {string}")
    public void the_error_message_should_contain(String expectedSubstring) {
        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.toLowerCase().contains(expectedSubstring.toLowerCase()),
                "Expected error to contain '" + expectedSubstring + "' but was: " + actualError);
        log.info("Verified error message contains: {}", expectedSubstring);
    }
}