Feature: Product listing and cart functionality on SauceDemo

  Background:
    Given the user is logged in with valid credentials

  @smoke
  Scenario: Verify product count on home page
    Then the product count should be 6

  @regression
  Scenario: Sort products by price low to high
    When the user sorts products by "Price (low to high)"
    Then the product prices should be in ascending order

  @regression
  Scenario: Sort products by name Z to A
    When the user sorts products by "Name (Z to A)"
    Then the first product name should be "Test.allTheThings() T-Shirt (Red)"

  @smoke @regression
  Scenario: Add a single product to cart and verify
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"
    And the cart should contain "Sauce Labs Backpack"

  @regression
  Scenario: Add multiple products to cart
    When the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    Then the cart badge should show "2"