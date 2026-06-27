Feature: Cart functionality on SauceDemo

  As a customer
  I want to manage products in my shopping cart
  So that I can purchase only the products I need

  Background:
    Given the user is logged in with valid credentials

  @smoke @cart @positive
  Scenario: Add a product to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"
    And the cart should contain "Sauce Labs Backpack"

  @smoke @cart @positive
  Scenario: Remove a product from the inventory page
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"

    When the user removes "Sauce Labs Backpack" from the cart
    Then the cart badge should show "0"

  @regression @cart @positive
  Scenario: Remove a product from the cart page
    When the user adds "Sauce Labs Backpack" to the cart
    And the user navigates to the cart
    Then the cart should contain "Sauce Labs Backpack"

    When the user removes "Sauce Labs Backpack" from the cart page
    Then the cart should be empty

  @regression @cart @positive
  Scenario: Add multiple products to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    Then the cart badge should show "2"

    And the user navigates to the cart
    And the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"