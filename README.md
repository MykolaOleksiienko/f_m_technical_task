# Test Automation Framework

A modular, maintainable test automation framework built with Java, Playwright, and JUnit 5 for cross-browser and cross-resolution web application testing.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Key Components](#key-components)
- [How It Works](#how-it-works)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [CI/CD Integration](#cicd-integration)
- [Parallel Execution](#parallel-execution)
- [Reporting](#reporting)

## Overview

This framework implements a Page Object Model (POM) architecture with support for:
- **Cross-browser testing**: Chromium, Firefox, and Safari (WebKit)
- **Cross-resolution testing**: Desktop, Tablet, and Mobile viewports
- **Parallel test execution**: Configurable thread count for concurrent test runs
- **Comprehensive reporting**: Allure framework integration with step-by-step execution
- **CI/CD integration**: GitLab CI/CD with parameterized pipelines

## Architecture

### Layered Architecture

The framework follows a layered architecture pattern:

```
┌─────────────────────────────────────┐
│         Test Layer                  │
│    (BaseTest, Test Classes)         │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Page Object Layer              │
│  (AbstractBaseSteps, Page Classes)  │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         Core Layer                   │
│  (Configuration, Browser Management) │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Utilities Layer                 │
│  (DataGenerator, WaitUtilities)     │
└─────────────────────────────────────┘
```

### Design Principles

1. **Page Object Model (POM)**: Pages encapsulate UI interactions and locators
2. **Separation of Concerns**: Each layer has a specific responsibility
3. **DRY (Don't Repeat Yourself)**: Common functionality in base classes
4. **Thread Safety**: ThreadLocal for parallel execution support
5. **Configuration-Driven**: Environment variables for flexible test execution

## Project Structure

```
src/
├── main/java/
│   ├── annotations/          # Custom annotations
│   │   ├── common/          # @RunBrowser, @URI
│   │   ├── functional/      # @Login, @Feedback, @MainPage
│   │   └── test_types/      # @Smoke, @Regression
│   ├── assertions/          # Custom assertions (CustomAssertions)
│   ├── core/                # Core framework logic
│   │   ├── base_config/    # Configuration management
│   │   │   ├── browser_config/  # Browser context holder
│   │   │   ├── common/          # ConfigParams, ThreadLoggingListener
│   │   │   └── configuration_execution/  # Browser lifecycle
│   │   └── listeners/       # Test execution listeners
│   ├── enums/               # Enumerations
│   │   ├── config/         # BrowserType, ViewportPreset
│   │   └── ValidationErrorMessage
│   ├── exceptions/          # Custom exceptions
│   ├── locator_templates/   # Locator utilities
│   ├── pages/               # Page Object classes
│   │   ├── AbstractBaseSteps  # Base class for all pages
│   │   ├── login/           # LoginPage
│   │   ├── feedback/        # FeedbackPage
│   │   └── two_factor_verification/  # TwoFactorVerificationPage
│   └── utils/               # Utility classes
│       ├── DataGenerator
│       └── WaitUtilities
└── test/java/
    └── tests/               # Test classes
        ├── BaseTest        # Base test class
        ├── login/          # LoginTests
        └── feedback/       # FeedbackTests
```

## Key Components

### 1. Core Configuration (`core/base_config/`)

#### ConfigurationExecution
JUnit 5 extension that manages browser lifecycle:
- **BeforeAll**: Creates Playwright instance and launches browser based on `BROWSER_TYPE`
- **AfterAll**: Closes browser and Playwright instances
- Uses **ThreadLocal** for thread-safe parallel execution

#### BrowserContextHolder
Thread-safe browser context storage:
- Uses `ThreadLocal<BrowserContext>` to isolate contexts per thread
- Methods: `get()`, `set()`, `remove()`

#### CustomStrategyParallelism
Custom parallel execution strategy:
- Reads thread count from `THREAD_COUNT` system property
- Configures JUnit Platform for parallel test execution

### 2. Page Object Model (`pages/`)

#### AbstractBaseSteps
Base class providing common page interactions:
- Element location and waiting
- Click, fill, clear operations
- Page navigation and state management
- All methods annotated with `@Step` for Allure reporting

#### Page Classes
Page-specific classes extending `AbstractBaseSteps`:
- `LoginPage`: Login form interactions
- `FeedbackPage`: Feedback form interactions
- `TwoFactorVerificationPage`: 2FA page verification

**Example:**
```java
@URI(url = "/login")
public class LoginPage extends AbstractBaseSteps {
    public LoginPage fillUsername(String value) {
        fill(usernameField, value);
        return this;  // Method chaining
    }
}
```

### 3. Test Base (`tests/BaseTest`)

Base class for all test classes:
- Browser initialization via `@RunBrowser` annotation
- Viewport configuration from environment variables
- Page instance creation with URL resolution
- Test instance lifecycle: `PER_CLASS`

**Key Method:**
```java
public <T extends AbstractBaseSteps> T openFreshBrowserWithPageSiteUrl(Class<T> clazz);
```

### 4. Annotations

#### @RunBrowser
Triggers `ConfigurationExecution` extension to initialize browser

#### @URI
Defines page URL:
- `url`: Relative or absolute URL
- `isAbsolute`: Whether URL is absolute

#### @Smoke, @Regression
Test categorization tags for filtering

#### @Login, @Feedback, @MainPage
Functional area tags

### 5. Custom Assertions (`assertions/CustomAssertions`)

Playwright-specific assertions:
- `assertThatElementIsVisible()`: Element visibility checks
- `assertThatElementTextEqualTo()`: Text content validation
- `assertThatElementInnerTextEqualTo()`: Inner text with newline normalization
- `assertThatInputFieldElementValueEqualToValue()`: Input field value validation

### 6. Listeners (`core/listeners/`)

#### ListenerTestExecution
AfterEach callback that:
- Captures screenshots on test failures
- Closes browser contexts after each test
- Cleans up ThreadLocal storage

## How It Works

### Test Execution Flow

1. **Test Class Initialization**
   ```
   @RunBrowser() → ConfigurationExecution.beforeAll()
   ├── Creates Playwright instance (ThreadLocal)
   ├── Launches browser based on BROWSER_TYPE
   └── Stores in ThreadLocal<ConfigParams>
   ```

2. **Test Method Execution**
   ```
   @BeforeEach → BaseTest.beforeEach()
   ├── openFreshBrowserWithPageSiteUrl(LoginPage.class)
   │   ├── Creates new BrowserContext (ThreadLocal)
   │   ├── Creates Page with viewport from env vars
   │   ├── Navigates to URL (from @URI annotation)
   │   └── Returns initialized Page instance
   └── Test executes using Page Object methods
   ```

3. **After Test Cleanup**
   ```
   ListenerTestExecution.afterEach()
   ├── If test failed → Capture screenshot
   ├── Close browser contexts
   └── Remove ThreadLocal values
   ```

4. **After All Cleanup**
   ```
   ConfigurationExecution.afterAll()
   ├── Close browser
   ├── Close Playwright
   └── Remove ThreadLocal values
   ```

### Configuration Flow

```
Environment Variables (CI/CD or local)
    ↓
Gradle System Properties (build.gradle)
    ↓
EnvProperties (reads from System.getProperty/System.getenv)
    ↓
ConfigurationExecution (uses EnvProperties)
    ↓
Browser/Viewport Configuration
```

### Thread Safety

The framework uses **ThreadLocal** for thread-safe parallel execution:

- `BrowserContextHolder`: ThreadLocal browser context per thread
- `ConfigurationExecution`: ThreadLocal browser and Playwright instances
- Each thread has isolated browser context and configuration

## Configuration

### Environment Variables

| Variable | Description | Default | Example |
|----------|-------------|---------|---------|
| `BROWSER_TYPE` | Browser to use | `chromium` | `chromium`, `firefox`, `safari` |
| `VIEWPORT_WIDTH` | Viewport width | `1920` | `1920`, `768`, `375` |
| `VIEWPORT_HEIGHT` | Viewport height | `1080` | `1080`, `1024`, `667` |
| `HEADLESS` | Headless mode | `false` | `true`, `false` |
| `THREAD_COUNT` | Parallel threads | `1` | `1`, `2`, `4` |
| `UI_TAG` | Test tag filter | (empty) | `Smoke`, `Regression` |
| `ENVIRONMENT` | Environment | `dev` | `dev`, `prod` |

### Environment Configuration (`envConfig.groovy`)

```groovy
environments {
    dev {
        url = 'https://www.vmedia.ca'
    }
    prod {
        url = ""
    }
}
```

### JUnit Platform Configuration (`junit-platform.properties`)

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=same_thread
junit.jupiter.execution.parallel.mode.classes.default=concurrent
junit.jupiter.testinstance.lifecycle.default=PER_CLASS
junit.jupiter.execution.parallel.config.strategy=custom
junit.jupiter.execution.parallel.config.custom.class=core.base_config.configuration_execution.CustomStrategyParallelism
```

## Running Tests

### Local Execution

**Run all tests:**
```bash
./gradlew test
```

**Run tests with specific tag:**
```bash
./gradlew test -Djunit.jupiter.tags.include=Smoke
./gradlew test -Djunit.jupiter.tags.include=Login
```

**Run tests with custom configuration:**
```bash
BROWSER_TYPE=firefox VIEWPORT_WIDTH=768 VIEWPORT_HEIGHT=1024 ./gradlew test
```

**Run tests in parallel:**
```bash
THREAD_COUNT=4 ./gradlew test
```

**Run tests for specific environment:**
```bash
./gradlew test -Penv=dev
./gradlew test -Penv=prod
```

### Parameterized Tests

Tests can be parameterized using `@ParameterizedTest` and `@MethodSource`:

```java
@ParameterizedTest(name = "Successful login with {0}")
@MethodSource("validCredentialsProvider")
void successfulLoginWithValidCredentials(String username) {
    loginPage.fillUsername(username)
            .fillPassword(validPassword)
            .clickLoginButton();
}
```

## CI/CD Integration

### GitLab CI/CD Pipeline

The framework includes a comprehensive GitLab CI/CD pipeline with:

#### Automatic Jobs

**Smoke Tests:**
- Quick validation with default browser/viewport
- Runs on merge requests and main branches

**Cross-Browser/Cross-Resolution Jobs:**
- `test:chromium:desktop` - Chromium, 1920x1080
- `test:chromium:tablet` - Chromium, 768x1024
- `test:chromium:mobile` - Chromium, 375x667
- `test:firefox:desktop` - Firefox, 1920x1080
- `test:firefox:mobile` - Firefox, 375x667
- `test:webkit:desktop` - Safari, 1920x1080
- `test:webkit:mobile` - Safari, 375x667

All jobs run **in parallel** for faster execution.

#### Manual Parameterized Job

`parameterized-tests` allows custom configuration via GitLab CI/CD variables:

**Variables:**
- `UI_TAG`: `Smoke`, `Regression`, or empty for all tests
- `BROWSER`: `chromium`, `firefox`, `safari`
- `RESOLUTION`: `desktop`, `tablet`, `mobile`, or custom `WIDTHxHEIGHT`
- `ENVIRONMENT`: `dev`, `prod`
- `THREAD_COUNT`: Number of parallel threads

**Usage:**
1. Go to **CI/CD → Pipelines → Run pipeline**
2. Set variables in the **Variables** section
3. Run pipeline
4. Manually trigger `parameterized-tests` job

#### Pipeline Schedules

Create scheduled pipelines with custom parameters:

**Example Schedule:**
- **Description**: "Nightly Regression Tests"
- **Cron**: `0 2 * * *` (daily at 2:00 AM)
- **Variables**:
  - `UI_TAG = Regression`
  - `BROWSER = chromium`
  - `RESOLUTION = desktop`
  - `ENVIRONMENT = dev`
  - `THREAD_COUNT = 4`

### Artifacts

All jobs save artifacts:
- `build/reports/tests/` - Gradle test reports
- `allure-results/` - Allure test results

### Report Generation

`generate-allure-report` job:
- Collects results from all test jobs
- Generates comprehensive Allure report
- Available as artifact for download

## Parallel Execution

### Configuration

Parallel execution is configured via:

1. **JUnit Platform Properties** (`junit-platform.properties`):
   - Enables parallel execution
   - Sets custom strategy to `CustomStrategyParallelism`

2. **Thread Count**:
   - Read from `THREAD_COUNT` system property
   - Default: `1` (sequential execution)
   - Set via environment variable or CI/CD

3. **Thread Safety**:
   - `ThreadLocal` for browser contexts
   - `ThreadLocal` for browser instances
   - Each thread has isolated configuration

### Execution Modes

- **Classes**: Run in parallel (`concurrent`)
- **Methods**: Run in same thread (`same_thread`)
- **Test Instance**: `PER_CLASS` lifecycle

### Example

```bash
# Run tests with 4 parallel threads
THREAD_COUNT=4 ./gradlew test
```

## Reporting

### Allure Reports

**Generate report:**
```bash
./gradlew allureReport
```

**Open report:**
```bash
./gradlew allureServe
```

**Run tests and generate report:**
```bash
./gradlew testAndGenerateReport
```

### Report Features

- **Step-by-step execution**: All methods annotated with `@Step`
- **Screenshots**: Automatic capture on test failures
- **Test categorization**: Tags displayed in report
- **History**: Test execution history across runs
- **Attachments**: Screenshots and logs attached to failed tests

### Allure Steps

All page methods and test steps are annotated with `@Step`:

```java
@Step("Fill username value: '{0}'")
public LoginPage fillUsername(String value) {
    fill(usernameField, value);
    return this;
}
```

This creates detailed step-by-step reports in Allure.

## Best Practices

### Writing Tests

1. **Extend BaseTest**: All test classes should extend `BaseTest`
2. **Use Page Objects**: Interact with pages through Page Object methods
3. **Method Chaining**: Use fluent interface for readable tests
4. **Tag Tests**: Use `@Smoke`, `@Regression` for categorization
5. **Parameterized Tests**: Use `@ParameterizedTest` for data-driven testing

### Writing Page Objects

1. **Extend AbstractBaseSteps**: All pages extend base class
2. **Use @URI Annotation**: Define page URL
3. **Method Chaining**: Return `this` for fluent interface
4. **@Step Annotations**: Add to all public methods
5. **Locator Strategy**: Use `data-ui-test` attributes via `LocatorTemplate`

### Locator Strategy

Use `data-ui-test` attributes for stable selectors:

```java
private static final String usernameField = getLocatorByDataAttribute("customer-username-input");
```

This provides:
- Stability: Less affected by UI changes
- Readability: Clear purpose of each element
- Maintainability: Centralized locator management

## Dependencies

### Core Dependencies

- **JUnit 5**: Testing framework (5.10.0)
- **Playwright**: Browser automation (1.56.0)
- **AssertJ**: Fluent assertions (3.24.2)
- **Allure**: Test reporting (2.24.0)
- **Lombok**: Code generation (1.18.30)
- **Apache Commons Lang3**: Utilities (3.18.0)

### Build Tool

- **Gradle**: Build and dependency management (7.6+)
- **Java**: Version 17

## Troubleshooting

### Common Issues

**Tests fail with "NoSuchPageException":**
- Ensure `@RunBrowser` annotation is present on test class
- Check that browser context is created before page access

**Parallel execution issues:**
- Verify `ThreadLocal` is properly cleaned up
- Check `THREAD_COUNT` configuration
- Ensure test classes use `PER_CLASS` lifecycle

**Browser not launching:**
- Check `BROWSER_TYPE` environment variable
- Verify Playwright browsers are installed
- Check headless mode configuration

**Allure report not generating:**
- Ensure Allure CLI is installed
- Check that test results exist in `allure-results/`
- Verify Allure dependencies in `build.gradle`

## License

This project is part of a technical assessment.

