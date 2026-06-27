Feature: Checkout functionality on SauceDemo

  As a customer
  I want to purchase products from SauceDemo
  So that I can complete my order successfully

  Background:
    Given the user is logged in with valid credentials
    And the user adds "Sauce Labs Backpack" to the cart
    And the user navigates to the cart

  @smoke @positive
  Scenario: Successfully complete checkout with valid information
    When the user proceeds to checkout
    And the user enters checkout information:
      | firstName  | Ayush  |
      | lastName   | Singh  |
      | postalCode | 401208 |
    And the user completes the checkout
    Then the order should be placed successfully

  @regression @negative
  Scenario Outline: Checkout validation errors
    When the user proceeds to checkout
    And the user enters checkout information:
      | firstName  | <firstName> |
      | lastName   | <lastName> |
      | postalCode | <postalCode> |
    Then a checkout error message should be displayed
    And the checkout error message should contain "<errorMessage>"

    Examples:
      | firstName | lastName | postalCode | errorMessage            |
      |           | Singh    | 401208     | First Name is required  |
      | Ayush     |          | 401208     | Last Name is required   |
      | Ayush     | Singh    |            | Postal Code is required |