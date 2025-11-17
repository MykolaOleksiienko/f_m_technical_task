package automation.shared.library.core.base_config.configuration_execution;

import automation.shared.library.core.base_config.annotations.RunBrowser;
import automation.shared.library.core.base_config.browser_config.BrowserName;
import automation.shared.library.core.base_config.common.ConfigParams;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import static automation.shared.library.core.base_config.browser_config.BrowserArguments.CONSOLE_MESSAGES;
import static automation.shared.library.core.base_config.browser_config.BrowserArguments.DISABLE_SANDBOX;
import static automation.shared.library.core.base_config.browser_config.BrowserArguments.NO_SANDBOX;
import static automation.shared.library.helpers.EnvProperties.getUrlBackOffice;
import static automation.shared.library.helpers.EnvProperties.getUrlWeb;
import static automation.shared.library.helpers.EnvProperties.isHeadlessMode;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class ConfigurationExecution extends AppConfigInit implements BeforeAllCallback, AfterAllCallback {
    private static final Logger logger = Logger.getLogger(ConfigurationExecution.class.getName());
    private static final String ENV_BROWSER_NAME = "browser";
    public static final ThreadLocal<ConfigParams> LAUNCH_CONFIG = new ThreadLocal<>();

    private Playwright playwright;
    private Browser browser;

    public static void initForThread() {
        ConfigParams configParams = new ConfigParams();
        configParams.setMainAppUrl(getUrlWeb());
        configParams.setBackOfficeUrl(getUrlBackOffice());
        LAUNCH_CONFIG.set(configParams);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        super.beforeAll(extensionContext);
        final var remoteBrowserName = System.getenv(ENV_BROWSER_NAME);
        if (ofNullable(remoteBrowserName).isPresent()) {
            browser = runBrowser(getRemoteBrowserName(remoteBrowserName));
        } else {
            final var annotation = extensionContext.getRequiredTestClass().getAnnotation(RunBrowser.class);
            if (ofNullable(annotation).isPresent()) {
                final var browserName = annotation.browserName();
                browser = runBrowser(browserName);
            } else {
                throw new RuntimeException("RunBrowser annotation was not found");
            }
        }

        LAUNCH_CONFIG.get().setBrowser(browser);
        logger.info(format("'Browser: %s with version: %s ' is starting to execute!",
                browser.browserType().name(), browser.version()));
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        LAUNCH_CONFIG.get()
                .getBrowser()
                .contexts()
                .stream()
                .filter(Objects::nonNull)
                .forEach(BrowserContext::close);

        super.afterAll(extensionContext);
        browser.close();
        playwright.close();
    }

    // Browser name have to be set as Enum value: browser=SAFARI or CHROMIUM
    private BrowserName getRemoteBrowserName(String name) {
        return Optional.of(BrowserName.valueOf(name))
                .orElseThrow(() -> new RuntimeException(format("Browser with Name: '%s' was not found on CI. " +
                        "Re-check correct browser name or exiting in enum BrowserName", name)));
    }

    private Browser runBrowser(BrowserName browserName) {
        playwright = Playwright.create();
        final var headless = isHeadlessMode();
        return switch (browserName) {
            case CHROMIUM -> playwright
                    .chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList(DISABLE_SANDBOX.getValue(), NO_SANDBOX.getValue(), CONSOLE_MESSAGES.getValue()))
                            .setHeadless(headless));

            case SAFARI -> playwright
                    .webkit()
                    .launch(new BrowserType.LaunchOptions().setHeadless(headless));
        };
    }
}
