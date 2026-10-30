# Selenium + TestNG Automation Framework

![Tests](https://github.com/andrewvimal11/selenium-testng-framework/actions/workflows/tests.yml/badge.svg)

A UI and API test automation framework in Java, built the way I build frameworks for client projects: Page Object Model, explicit waits only (no `Thread.sleep`), config you can override from the command line, parallel execution, HTML reports with failure screenshots, and CI on every push.

**Applications under test** (public practice sites):
- UI: [Sauce Demo](https://www.saucedemo.com), an e-commerce store
- API: [Restful Booker](https://restful-booker.herokuapp.com), a hotel booking REST API

## Tech stack

| Area | Tools |
|---|---|
| Language | Java 17 |
| UI automation | Selenium WebDriver 4 (Selenium Manager handles drivers) |
| API automation | REST Assured 5 + Jackson POJOs |
| Test runner | TestNG (groups, data providers, parallel classes) |
| Reporting | ExtentReports 5 with screenshots on failure |
| Build | Maven |
| CI | GitHub Actions: on push, pull request, and a daily schedule |

## What's covered

**UI (Sauce Demo)**
- Login: valid login, plus 4 invalid cases driven by a `@DataProvider` (empty username, empty password, wrong password, locked-out user)
- Products: sort by price (low→high) and by name (Z→A), add to cart, cart badge count
- Checkout: full end-to-end purchase with price check, and validation of required fields

**API (Restful Booker)**
- Auth token generation
- Full CRUD lifecycle: create → read → update → delete → confirm 404
- Negative test: update with an invalid token returns 403

## Project structure

```
src/test/java/com/andrewvimal/
├── framework/
│   ├── config/      ConfigReader: properties file + -D overrides
│   ├── driver/      DriverFactory: ThreadLocal WebDriver (Chrome/Firefox/Edge, headless)
│   ├── pages/       Page objects: Login, Inventory, Cart, Checkout (+ BasePage with waits)
│   ├── api/         BookingClient + request/response models
│   └── reporting/   ExtentReports manager + TestNG listener (screenshots on failure)
└── tests/
    ├── ui/          LoginTest, InventoryTest, CheckoutTest
    └── api/         BookingApiTest
src/test/resources/config.properties
testng.xml  testng-ui.xml  testng-api.xml  testng-smoke.xml
.github/workflows/tests.yml
```

## Running the tests

Requirements: JDK 17+, Maven 3.9+, and Chrome (or Firefox/Edge).

```bash
# Everything (UI + API), headless Chrome
mvn test

# Just one suite
mvn test -Dsuite=testng-ui.xml
mvn test -Dsuite=testng-api.xml
mvn test -Dsuite=testng-smoke.xml

# Watch the browser, or switch browsers
mvn test -Dheadless=false
mvn test -Dbrowser=firefox
```

The report is written to `target/extent-report/index.html`. In CI it's uploaded as a build artifact on every run.

## Design decisions

- **ThreadLocal WebDriver**, so classes run in parallel without sharing browsers.
- **A fresh browser per test** (`@BeforeMethod`/`@AfterMethod`), so tests never depend on each other's state.
- **Explicit waits in `BasePage`.** Every click and type waits for the element first, which removes most flakiness.
- **Fluent page objects.** Methods return the next page, so tests read like user journeys.
- **API client layer.** Endpoints live in one class; tests only state intent and assertions.
- **Config precedence.** `-Dkey=value` beats `config.properties`, so CI can change settings without editing code.

---
Built by Andrew, QA Automation Engineer · [GitHub](https://github.com/andrewvimal11)
