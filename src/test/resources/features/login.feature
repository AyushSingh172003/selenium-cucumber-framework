Feature: Login functionality on SauceDemo

  As a registered user
  I want to log into the SauceDemo application
  So that I can access the products page

  Background:
    Given the user is on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be navigated to the home page
    And the page heading should be "Products"

  @regression @negative
  Scenario: Login fails with locked out user
    When the user enters username "locked_out_user" and password "secret_sauce"
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "locked out"

  @regression @negative
  Scenario Outline: Login fails with invalid credentials
    When the user enters username "<username>" and password "<password>"
    And the user clicks the login button
    Then an error message should be displayed

    Examples:
      | username        | password     |
      | wrong_user      | wrongpass    |
      | standard_user   |              |
      |                 | secret_sauce |