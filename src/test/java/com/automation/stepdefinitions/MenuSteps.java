package com.automation.stepdefinitions;

import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import com.automation.pages.MenuPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class MenuSteps {

    private static final Logger log = LogManager.getLogger(MenuSteps.class);

    private final ProductSteps productSteps;

    private HomePage homePage;
    private MenuPage menuPage;
    private LoginPage loginPage;

    public MenuSteps(ProductSteps productSteps) {
        this.productSteps = productSteps;
    }

    @When("the user opens the hamburger menu")
    public void the_user_opens_the_hamburger_menu() {
        // Reuse the existing HomePage that ProductSteps already
        // navigated to, instead of constructing a new one.
        homePage = productSteps.getHomePage();

        menuPage = homePage.openMenu();

        Assert.assertTrue(menuPage.isPageLoaded(), "Menu did not open");
        log.info("Hamburger menu opened successfully");
    }

    @When("the user resets the application state")
    public void the_user_resets_the_application_state() {
        homePage = menuPage.resetAppState();
        log.info("Application state reset");
    }

    @When("the user logs out")
    public void the_user_logs_out() {
        loginPage = menuPage.logout();
        log.info("Logged out successfully");
    }

    @Then("the user should be redirected to the login page")
    public void the_user_should_be_redirected_to_the_login_page() {
        Assert.assertTrue(
                loginPage.isPageLoaded(),
                "User was not redirected to login page"
        );
        log.info("Verified redirection to login page");
    }
}