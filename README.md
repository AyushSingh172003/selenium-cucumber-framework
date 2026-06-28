<div align="center">

# 🥒 Selenium Cucumber BDD Automation Framework

### Enterprise-grade Selenium + Cucumber + TestNG framework for SauceDemo, built and verified end-to-end

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.21.0-43B02A?style=for-the-badge&logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.14.0-23D96C?style=for-the-badge&logo=cucumber&logoColor=white)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-EE3939?style=for-the-badge&logo=testng&logoColor=white)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

[![Build](https://img.shields.io/badge/Last%20Run-25%2F25%20Passing-success?style=flat-square)](#-verified-test-run)
[![Reports](https://img.shields.io/badge/Reports-Extent%20%2B%20Allure-orange?style=flat-square)](#-reporting)
[![Parallel](https://img.shields.io/badge/Execution-Parallel%20(3%20threads)-blue?style=flat-square)](#-execution-modes)

</div>

---

## 📖 Overview

This is a **Selenium WebDriver automation framework** built around **Cucumber BDD** and run through **TestNG** (not JUnit), targeting [SauceDemo](https://www.saucedemo.com) as the application under test. It was built incrementally, layer by layer — Maven setup, Page Object Model, Cucumber-TestNG bridge, reporting, then expanded with checkout, cart, and hamburger-menu coverage — and every layer was independently verified by actually running it, not just written and assumed correct.

The goal wasn't just "automate some test cases." It was to build something that reflects how real teams structure automation:

- **Page Object Model** with a shared `BasePage` — no raw Selenium calls inside step definitions
- **Cucumber running under TestNG**, not JUnit — via `AbstractTestNGCucumberTests` + `cucumber-testng`
- **ThreadLocal WebDriver** management — verified safe under real parallel execution, not just claimed
- **Dual data-driven approaches** — Cucumber Scenario Outlines *and* Excel via Apache POI, both genuinely wired into the same `mvn clean test` run
- **Dual reporting** — a manually-wired ExtentReports Spark report (bypassing a third-party adapter that silently failed to fire) plus Allure attachments
- **Externalized configuration** — browser, headless mode, and tags are all overridable from the command line without touching code

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Browser Automation | Selenium WebDriver | 4.21.0 |
| BDD Engine | Cucumber | 7.14.0 |
| Test Runner | TestNG | 7.10.2 |
| Build Tool | Maven | Surefire 3.2.5 |
| Reporting | ExtentReports (Spark) | 5.1.1 |
| Reporting | Allure (TestNG + Cucumber7) | 2.29.0 |
| Logging | Log4j2 | 2.23.1 |
| Data-Driven Testing | Apache POI | 5.2.5 |
| Driver Management | WebDriverManager | 6.1.0 |
| Dependency Injection | Cucumber PicoContainer | 7.14.0 |

---

## 🏗️ Architecture

```
                    ┌─────────────────────┐
                    │   .feature files     │   Gherkin — business-readable
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │  Step Definitions    │   Java glue code, one class per domain
                    │  (Login/Product/     │   (LoginSteps, ProductSteps, CartSteps,
                    │   Cart/Checkout/Menu)│    CheckoutSteps, MenuSteps)
                    └──────────┬──────────┘
                               │  PicoContainer-injected,
                               │  shared state across classes
                    ┌──────────▼──────────┐
                    │    Page Objects      │   LoginPage, HomePage, CartPage,
                    │  (extends BasePage)  │   CheckoutPage, CheckoutOverviewPage,
                    └──────────┬──────────┘   CheckoutCompletePage, MenuPage
                               │
                    ┌──────────▼──────────┐
                    │      BasePage         │   PageFactory init, explicit waits,
                    │   (abstract class)    │   click/type/getText, slugify(),
                    └──────────┬──────────┘   JS-click fallback
                               │
                    ┌──────────▼──────────┐
                    │   Selenium WebDriver  │   via DriverFactory (ThreadLocal)
                    └──────────────────────┘
```

### Step definition ownership — who holds what state

A real lesson from building this: splitting cart-related steps across two classes that each kept their *own* copy of `CartPage` worked **by coincidence of step ordering**, until it didn't. The fix was a single `CartSteps` class that owns `cartPage` as the one source of truth, injected into anything that needs it:

```
ProductSteps   ──(injected into)──▶   CartSteps   ──(injected into)──▶   CheckoutSteps
   owns: homePage                       owns: cartPage                      owns: checkoutPage,
                                                                             checkoutOverviewPage,
                                                                             checkoutCompletePage

MenuSteps  ──(injected: ProductSteps)──▶  reuses homePage, doesn't recreate it
```

---

## ✨ Framework Features

### Architecture
- Page Object Model with `PageFactory` (`@FindBy`)
- Abstract `BasePage` — every page implements `isPageLoaded()` with an explicit wait, no exceptions
- Singleton `ConfigReader` — config file read once, command-line system properties override it
- `DriverFactory` — ThreadLocal-scoped WebDriver, safe under parallel execution
- Shared `slugify()` helper in `BasePage` — one place to convert product names into SauceDemo's `data-test` attribute format, used by every page that needs it

### Execution
- Parallel execution (`parallel="methods"`, `threadCount=3` in Surefire) — verified with 10 concurrent TestNG thread-pool workers across 20 Cucumber scenarios with zero shared-state bleed
- Cross-browser support: Chrome, Firefox, Edge
- Headless mode, fully overridable: `-Dheadless=true|false`
- Tag-based execution via `-Dcucumber.filter.tags`
- Chrome configured to suppress the password-leak-detection popup that otherwise interrupts headed runs

### Reporting
- **ExtentReports (Spark)** — manually wired via a custom `ExtentManager` (see [Reporting](#-reporting) for why)
- **Allure** — screenshot attachments per scenario via `Allure.addAttachment()`
- **Cucumber native HTML + JSON** reports
- **Log4j2** — console output during execution, rolling file logs on disk
- Screenshots captured on every scenario teardown, filenames disambiguated by timestamp **and thread ID** to prevent collisions under parallel execution

### Data Management
- **Cucumber Scenario Outlines** for inline tabular test data
- **Excel-driven testing** via Apache POI — `LoginData.xlsx` read through `ExcelReader` + `LoginDataProvider`, registered as its own TestNG `<test>` block so it actually runs as part of `mvn clean test` (not just a standalone class that looks wired in but never executes)

---

## 🧪 Verified Test Coverage

Every item below was exercised in the test run summarized at the bottom of this README — not aspirational, not "should work."

| Module | Scenarios |
|---|---|
| 🔐 **Login** | Valid login · Invalid credentials (3 data rows via Scenario Outline) · Locked-out user |
| 🛒 **Product** | Product count verification · Sort by price ascending · Sort by name Z→A · Add single product · Add multiple products |
| 🛍️ **Cart** | Add to cart · Remove from inventory page · Remove from cart page · Add multiple products and verify both present |
| 💳 **Checkout** | Full successful checkout flow · First/Last Name/Postal Code validation errors (3 negative scenarios) |
| 🍔 **Menu** | Logout → redirected to login · Reset app state clears the cart |
| 📊 **Excel-Driven Login** | 5 rows from `LoginData.xlsx` — standard_user, locked_out_user, problem_user, wrong_user, and a wrong-password case |

**Total: 20 Cucumber scenarios + 5 Excel-driven TestNG tests = 25 tests**

---

## 📂 Project Structure

```
selenium-cucumber-framework/
├── src/
│   ├── main/java/com/automation/
│   │   ├── bases/
│   │   │   └── BasePage.java              # PageFactory, waits, click/type/getText, slugify()
│   │   ├── factory/
│   │   │   └── DriverFactory.java         # ThreadLocal WebDriver, browser-specific options
│   │   ├── pages/
│   │   │   ├── LoginPage.java
│   │   │   ├── HomePage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutPage.java
│   │   │   ├── CheckoutOverviewPage.java
│   │   │   ├── CheckoutCompletePage.java
│   │   │   └── MenuPage.java
│   │   └── utils/
│   │       ├── ConfigReader.java          # Singleton, file + system property override
│   │       ├── ExcelReader.java           # Apache POI, try-with-resources
│   │       └── ScreenshotUtil.java        # Timestamp + thread-ID disambiguated filenames
│   │
│   └── test/
│       ├── java/com/automation/
│       │   ├── dataproviders/
│       │   │   └── LoginDataProvider.java # @DataProvider reading LoginData.xlsx
│       │   ├── hooks/
│       │   │   └── Hooks.java             # @Before/@After/@AfterAll — driver + report lifecycle
│       │   ├── listeners/
│       │   │   └── ExtentManager.java     # Manual ExtentReports wiring (thread-safe)
│       │   ├── runners/
│       │   │   └── TestRunner.java        # AbstractTestNGCucumberTests bridge
│       │   ├── stepdefinitions/
│       │   │   ├── LoginSteps.java
│       │   │   ├── ProductSteps.java      # owns: homePage
│       │   │   ├── CartSteps.java         # owns: cartPage (single source of truth)
│       │   │   ├── CheckoutSteps.java     # owns: checkoutPage and successors
│       │   │   └── MenuSteps.java
│       │   └── tests/
│       │       └── ExcelLoginTest.java    # Plain TestNG test, Excel-driven
│       │
│       └── resources/
│           ├── config/
│           │   ├── config.properties
│           │   ├── extent-config.xml
│           │   └── testng.xml             # Registers BOTH Cucumber runner AND ExcelLoginTest
│           ├── features/
│           │   ├── login.feature
│           │   ├── product.feature
│           │   ├── cart.feature
│           │   ├── checkout.feature
│           │   └── menu.feature
│           ├── testdata/
│           │   └── LoginData.xlsx
│           ├── extent.properties
│           └── log4j2.xml
│
├── pom.xml
└── README.md
```

---

## ⚙️ Configuration

All runtime behaviour is controlled from `src/test/resources/config/config.properties`, with every value overridable from the command line via Maven system properties (handled in `ConfigReader`, which checks `System.getProperty()` before falling back to the file):

```properties
browser=chrome
headless=false
baseUrl=https://www.saucedemo.com
implicitWait=5
explicitWait=20
pageLoadTimeout=30
validUsername=standard_user
validPassword=secret_sauce
lockedUsername=locked_out_user
screenshotPath=test-output/screenshots/
reportPath=test-output/reports/
excelPath=src/test/resources/testdata/LoginData.xlsx
```

---

## ▶️ Running Tests

```bash
# Full suite — Cucumber scenarios + Excel-driven tests, parallel, headless by default
mvn clean test

# Force a visible browser locally
mvn clean test -Dheadless=false

# Cross-browser
mvn clean test -Dbrowser=firefox

# Tag-filtered Cucumber runs
mvn clean test -Dcucumber.filter.tags="@smoke"
mvn clean test -Dcucumber.filter.tags="@regression"

# Run only the Excel-driven login tests directly
mvn test -Dtest=ExcelLoginTest
```

> `testng.xml` registers two `<test>` blocks — `Cucumber BDD Tests` (runs `TestRunner`, which drives all 20 Gherkin scenarios) and `Excel Driven Login Tests` (runs `ExcelLoginTest`, which drives the 5 spreadsheet rows). Both execute under a single `mvn clean test` invocation.

---

## 📊 Reporting

### Why ExtentReports is wired manually, not via the Cucumber plugin

The obvious approach — `extentreports-cucumber7-adapter` declared in the `plugin = {...}` array of `@CucumberOptions` — was tried first and looked correct on paper: dependency resolved, class confirmed present inside the jar at the expected path, `extent.properties` confirmed on the classpath, multiple version combinations tried. It compiled, ran, and every scenario passed — but the adapter never wrote an HTML file, with zero exceptions or log output to explain why.

Rather than keep chasing an opaque third-party event-listening issue, `ExtentManager` calls the `ExtentReports`/`ExtentSparkReporter` API directly from `Hooks.java`'s `@Before`/`@After`/`@AfterAll`. This is more code, but it is *visible* — failures throw real exceptions instead of disappearing — and it gave a working, screenshot-embedded report on the first run after switching.

```
@Before     → ExtentManager.startTest(scenario name)
@After      → pass()/fail() + addScreenCaptureFromPath() with the scenario's final screenshot
@AfterAll   → ExtentManager.flush()   ← writes SparkReport.html to disk, once, after all scenarios
```

`ExtentManager` is thread-safe by design: the shared `ExtentReports` instance is guarded by `synchronized` on `getInstance()`, and the per-scenario `ExtentTest` reference is `ThreadLocal` — confirmed correct under real `parallel="methods"` execution with 10 concurrent threads.

### Where reports land

| Report | Path |
|---|---|
| Extent Spark HTML | `test-output/reports/SparkReport.html` |
| Cucumber native HTML | `test-output/cucumber-reports/cucumber-html-report.html` (if configured in `TestRunner`) |
| Screenshots | `test-output/screenshots/` — filenames include status, sanitized scenario name, millisecond timestamp, and thread ID |
| Log4j2 logs | `test-output/logs/` |
| Allure results | `target/allure-results/` → `mvn allure:serve` to view |

---

## ✅ Verified Test Run

The console output below is from an actual `mvn clean test` execution, parallel (`threadCount=3`, TestNG pool scaled to 10 concurrent workers), headless, against the live SauceDemo site:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running TestSuite
...
[ExtentManager] ExtentReports initialized. Output: ...\test-output\reports\SparkReport.html
...
[ExtentManager] Report flushed to disk.
...
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 53.48 s -- in TestSuite
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**25/25 passing** — 20 Cucumber scenarios (including 3 checkout-validation negatives and 3 invalid-login negatives exercising error paths) plus 5 Excel-driven TestNG tests, all under genuine parallel load with no thread-safety failures, no shared-state corruption between scenarios, and a correctly-flushed Extent report containing per-scenario screenshots.

### Issues found and fixed during verification

This framework went through an actual code review pass, not just a "it ran once" check. Four real bugs were identified and fixed, then re-verified by running the full suite again:

1. **Silent `null` on checkout validation failure** — `CheckoutPage.enterCheckoutInformation()` previously returned `null` when validation failed, with no caller-side null check. It worked by accident (no later step happened to touch the null reference) but was one feature-file change away from a `NullPointerException`. Fixed to return a `boolean`; the step definition now explicitly branches on it.
2. **Screenshot filename collisions under parallel execution** — second-precision timestamps plus identical Scenario Outline names meant two parallel threads could overwrite each other's screenshot. Fixed by adding millisecond precision and the thread ID to every filename.
3. **Cart state split across two unrelated step classes** — `ProductSteps` and `CheckoutSteps` each kept their own independent `cartPage` field, working only because of step-ordering coincidence in `cart.feature`. Fixed by extracting a dedicated `CartSteps` class as the single owner of cart state, injected wherever needed.
4. **Missing explicit waits in `isPageLoaded()`** — `CheckoutPage`, `CheckoutOverviewPage`, and `CheckoutCompletePage` read page text without waiting for visibility first, unlike every other page object in the framework. Fixed to match the established defensive pattern.

A resource leak in `ExcelReader` (unclosed `FileInputStream`) and duplicated slugify logic across four methods were also cleaned up.

---

## 🎯 Design Patterns Used

- **Page Object Model** — one class per page, locators private, actions public
- **Factory Pattern** — `DriverFactory` creates the right `WebDriver` for the configured browser
- **Singleton Pattern** — `ConfigReader`, guarded with `synchronized`
- **Dependency Injection** — Cucumber PicoContainer threading `ProductSteps` → `CartSteps` → `CheckoutSteps`/`MenuSteps`

---

## 🚀 Future Enhancements

- [ ] Jenkins CI/CD pipeline (Jenkinsfile + EC2 master/agent)
- [ ] Docker containerized execution
- [ ] Selenium Grid for distributed cross-browser runs
- [ ] RestAssured API layer for backend validation
- [ ] Database validation hooks
- [ ] Appium mobile automation extension

---

## 👨‍💻 Author

**Ayush Kumar** — Automation Test Engineer, incoming at Montran
Java · Selenium · Cucumber · TestNG · Maven

---

<div align="center">

⭐ If this framework structure helped you, consider starring the repo.

</div>````md
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
