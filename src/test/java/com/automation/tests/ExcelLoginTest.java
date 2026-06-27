package com.automation.tests;

import com.automation.dataproviders.LoginDataProvider;
import com.automation.factory.DriverFactory;
import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExcelLoginTest {

    @Test(
            dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginTest(
            String username,
            String password,
            String expectedResult
    ) {

        DriverFactory.initDriver();

        try {

            DriverFactory.getDriver()
                    .get("https://www.saucedemo.com");

            LoginPage loginPage =
                    new LoginPage(
                            DriverFactory.getDriver()
                    );

            if (expectedResult.equalsIgnoreCase("PASS")) {

                HomePage homePage =
                        loginPage.login(
                                username,
                                password
                        );

                Assert.assertTrue(
                        homePage.isPageLoaded(),
                        "Login should succeed for: "
                                + username
                );

            } else {

                loginPage.loginExpectingFailure(
                        username,
                        password
                );

                Assert.assertTrue(
                        loginPage.isErrorDisplayed(),
                        "Login should fail for: "
                                + username
                );
            }

        } finally {
            DriverFactory.quitDriver();
        }
    }
}