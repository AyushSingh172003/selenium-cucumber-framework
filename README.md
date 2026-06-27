````md
# 🚀 Selenium Cucumber BDD Automation Framework

![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4-green)
![Cucumber](https://img.shields.io/badge/Cucumber-7-brightgreen)
![TestNG](https://img.shields.io/badge/TestNG-7-red)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Build](https://img.shields.io/badge/Build-Passing-success)

---

# 📖 Project Overview

An enterprise-grade **Selenium Automation Framework** built using modern industry practices and scalable design principles.

This project was developed to simulate how automation frameworks are structured in real-world organizations and enterprise environments. The primary objective was not just to automate test cases, but to build a framework that is:

- Scalable as test suites continue to grow
- Maintainable through proper separation of concerns
- Reusable across multiple applications and modules
- Stable during CI/CD execution
- Easy for teams to understand and extend

The framework follows industry best practices such as **Page Object Model (POM)**, **Dependency Injection**, **ThreadLocal WebDriver Management**, **Externalized Configuration**, and **Data Driven Testing**.

The project currently automates the complete end-to-end user journey of the SauceDemo application including authentication, product selection, cart management, checkout workflow and menu functionalities.

---

# 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| ☕ Java 17 | Programming Language |
| 🌐 Selenium WebDriver 4 | Browser Automation |
| 🥒 Cucumber BDD | Behavior Driven Development |
| 🧪 TestNG | Test Execution Framework |
| 📦 Maven | Dependency Management |
| 📊 Extent Reports | HTML Reporting |
| 📈 Allure Reports | Rich Reporting & Trends |
| 📄 Apache POI | Excel Data Driven Testing |
| 📝 Log4j2 | Logging Framework |
| 🚗 WebDriverManager | Driver Binary Management |
| 🔗 PicoContainer | Dependency Injection |

---

# 🏗️ Framework Architecture

```text
Feature Files
      ↓
Step Definitions
      ↓
Page Objects
      ↓
BasePage Utilities
      ↓
Selenium WebDriver
````

### 🥒 Feature Files

Contain business-readable scenarios written in Gherkin syntax.

### ⚙️ Step Definitions

Translate business actions into executable automation code.

### 📄 Page Objects

Encapsulate page-specific locators and actions.

### 🛠️ BasePage

Provides reusable browser interactions such as clicking, typing, waiting, scrolling and utility methods.

### 🚗 Driver Factory

Handles browser initialization and ThreadLocal WebDriver management.

---

# ✨ Framework Features

## 🏗️ Architecture Features

* Page Object Model (POM)
* PageFactory Implementation
* Factory Design Pattern
* Singleton Pattern
* Dependency Injection using PicoContainer
* Thread-safe Driver Management using ThreadLocal
* Reusable BasePage abstraction
* Centralized configuration management

## ⚡ Execution Features

* Parallel execution support
* Cross-browser execution support
* Headless browser execution
* Tag-based execution
* Maven CLI support
* Jenkins-ready project structure

## 📊 Reporting Features

* Extent Spark Reports
* Allure Reports with screenshots
* Cucumber HTML Reports
* Cucumber JSON Reports
* Log4j2 execution logs
* Automatic screenshot capture
* Environment information in reports

## 📂 Data Management Features

* Configuration-driven execution
* Excel-driven testing using Apache POI
* TestNG Data Providers
* Externalized test data

---

# 🧪 Automated Test Coverage

## 🔐 Login Module

* Successful Login Validation
* Invalid Credentials Validation
* Locked User Validation
* Logout Functionality
* Error Message Verification

## 🛒 Product Module

* Product Listing Verification
* Product Sorting Validation
* Add Single Product to Cart
* Add Multiple Products to Cart
* Cart Badge Verification

## 🛍️ Cart Module

* Cart Contents Validation
* Continue Shopping Workflow
* Checkout Navigation Validation
* Cart Badge Synchronization

## 💳 Checkout Module

* Successful Checkout Flow
* First Name Validation
* Last Name Validation
* Postal Code Validation
* Order Confirmation Verification

## 🍔 Hamburger Menu Module

* Logout Workflow
* Reset Application State
* Cart Reset Verification

---

# 📂 Project Structure

```text
selenium-cucumber-framework
│
├── src
│   ├── main
│   │   └── java/com.automation
│   │       ├── bases
│   │       ├── factory
│   │       ├── pages
│   │       └── utils
│   │
│   └── test
│       ├── java/com.automation
│       │   ├── dataproviders
│       │   ├── hooks
│       │   ├── listeners
│       │   ├── runners
│       │   ├── stepdefinitions
│       │   └── tests
│       │
│       └── resources
│           ├── config
│           ├── features
│           └── testdata
│
├── pom.xml
└── README.md
```

---

# 📊 Reports Generated

The framework automatically generates:

* 📈 Allure Reports
* 📊 Extent Spark Reports
* 📄 Cucumber HTML Reports
* 📑 Cucumber JSON Reports
* 📝 Execution Logs
* 📸 Screenshots

---

# ⚙️ Configuration

Configuration is managed centrally using:

```text
src/test/resources/config/config.properties
```

Example:

```properties
browser=chrome
headless=false
baseUrl=https://www.saucedemo.com
implicitWait=5
explicitWait=20
pageLoadTimeout=30
```

---

# ▶️ Running Tests

## 🚀 Run Complete Suite

```bash
mvn clean test
```

## 👻 Run Headless Mode

```bash
mvn clean test -Dheadless=true
```

## 🌐 Run on Firefox

```bash
mvn clean test -Dbrowser=firefox
```

## 🏷️ Run Smoke Tests

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

## 🔥 Run Regression Tests

```bash
mvn clean test -Dcucumber.filter.tags="@regression"
```

## 📄 Run Excel Driven Tests

```bash
mvn test -Dtest=ExcelLoginTest
```

---

# 📈 Current Framework Statistics

📌 20 Cucumber Scenarios
📌 5 Excel Driven Test Cases
📌 5 Feature Files
📌 9 Page Objects
📌 100% Passing Test Suite
📌 Parallel Execution Enabled
📌 Allure Reporting Enabled
📌 Extent Reporting Enabled
📌 ThreadLocal WebDriver Enabled

---

# 🎯 Design Patterns Used

🏗️ Page Object Model (POM)
🏭 Factory Pattern
🔒 Singleton Pattern
🔗 Dependency Injection Pattern

---

# 🚀 Future Enhancements

🤖 Jenkins CI/CD Integration
🐳 Docker Support
🌍 Selenium Grid Execution
🔌 RestAssured API Automation
🗄️ Database Validation
📱 Appium Mobile Automation

---

# 👨‍💻 Author

## Ayush Singh

💻 Java Automation Engineer
🧪 Selenium | Cucumber | TestNG | Maven | Java
☕ Passionate about Automation Framework Development

⭐ If you found this project useful, don't forget to star the repository!

```
```
