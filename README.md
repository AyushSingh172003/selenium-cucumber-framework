# 🚀 Selenium Cucumber BDD Automation Framework

![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4-green)
![Cucumber](https://img.shields.io/badge/Cucumber-7-brightgreen)
![TestNG](https://img.shields.io/badge/TestNG-7-red)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Status](https://img.shields.io/badge/Build-Passing-success)

---

## 📖 Project Overview

An industry-style **Selenium Automation Framework** built using:

✅ Java 17
✅ Selenium WebDriver 4
✅ Cucumber BDD
✅ TestNG
✅ Maven
✅ Extent Reports
✅ Log4j2 Logging
✅ ThreadLocal WebDriver
✅ Page Object Model (POM)

The framework is designed to be **scalable, maintainable, and CI/CD ready**.

---

## 🛠️ Tech Stack

| Technology            | Purpose               |
| --------------------- | --------------------- |
| ☕ Java 17             | Programming Language  |
| 🌐 Selenium 4         | Browser Automation    |
| 🥒 Cucumber           | BDD Framework         |
| 🧪 TestNG             | Test Execution        |
| 📦 Maven              | Dependency Management |
| 📊 Extent Reports     | Reporting             |
| 📝 Log4j2             | Logging               |
| 📸 Screenshot Utility | Failure Analysis      |
| 🚗 WebDriverManager   | Driver Management     |
| 🔗 PicoContainer      | Dependency Injection  |

---

## ✨ Framework Features

### 🏗️ Architecture

* ✅ Page Object Model (POM)
* ✅ Factory Design Pattern
* ✅ Singleton Pattern
* ✅ Dependency Injection with PicoContainer
* ✅ Thread-safe Driver Management using ThreadLocal

### ⚡ Execution

* ✅ Cross Browser Support
* ✅ Headless Execution
* ✅ Tag Based Execution
* ✅ Maven CLI Execution
* ✅ Jenkins Ready Structure

### 📊 Reporting

* ✅ Extent Spark Report
* ✅ Cucumber HTML Report
* ✅ Cucumber JSON Report
* ✅ Log4j Execution Logs
* ✅ Automatic Screenshot Capture

---

## 📂 Project Structure

```text
selenium-cucumber-framework
│
├── src
│   ├── main
│   │   └── java
│   │       └── com.automation
│   │           ├── bases
│   │           ├── factory
│   │           ├── pages
│   │           └── utils
│   │
│   └── test
│       ├── java
│       │   └── com.automation
│       │       ├── hooks
│       │       ├── listeners
│       │       ├── runners
│       │       └── stepdefinitions
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

## 🧪 Implemented Test Scenarios

### 🔐 Login Module

* ✅ Successful Login
* ✅ Invalid Login Validation
* ✅ Locked User Validation
* ✅ Logout Functionality

### 🛒 Product Module

* ✅ Verify Product Count
* ✅ Add Single Product to Cart
* ✅ Add Multiple Products to Cart
* ✅ Validate Cart Badge Count
* ✅ Verify Cart Contents

---

## 📸 Reports Generated

After execution, the framework automatically generates:

📊 Extent Spark Report
📄 Cucumber HTML Report
📑 Cucumber JSON Report
📝 Execution Logs
📸 Screenshots

---

## ⚙️ Configuration

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

## ▶️ Running Tests

### 🚀 Run Complete Suite

```bash
mvn clean test
```

### 👻 Run in Headless Mode

```bash
mvn clean test -Dheadless=true
```

### 🌐 Run on Firefox

```bash
mvn clean test -Dbrowser=firefox
```

### 🏷️ Run Smoke Tests

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

### 🔥 Run Regression Tests

```bash
mvn clean test -Dcucumber.filter.tags="@regression"
```

---

## 🎯 Design Patterns Used

🏗️ Page Object Model (POM)
🏭 Factory Pattern
🔒 Singleton Pattern
🔗 Dependency Injection Pattern

---

## 🚀 Future Enhancements

* 🔄 Parallel Execution
* 🤖 Jenkins CI/CD Integration
* 🐳 Docker Support
* 🌍 Selenium Grid Execution
* 🔌 RestAssured API Automation
* 📈 Allure Reports
* 🗄️ Database Validation

---

## 👨‍💻 Author

### Ayush Singh

💻 Java Automation Engineer
🧪 Selenium | Cucumber | TestNG | Maven
☕ Passionate about Test Automation & Framework Development

⭐ If you found this project useful, don't forget to star the repository!
