Feature: Hamburger menu functionality

  As a logged in user
  I want to use the navigation menu
  So that I can access application actions

  Background:
    Given the user is logged in with valid credentials

  @smoke @logout
  Scenario: Logout successfully
    When the user opens the hamburger menu
    And the user logs out
    Then the user should be redirected to the login page

  @regression @menu
  Scenario: Reset app state clears the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"

    When the user opens the hamburger menu
    And the user resets the application state
    Then the cart badge should show "0"