package core.base_config.configuration_execution;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Playwright;
import core.base_config.common.ConfigParams;
import enums.config.BrowserType;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.List;

import static core.EnvProperties.getBrowserType;
import static core.EnvProperties.getUrlWeb;
import static core.base_config.browser_config.BrowserArguments.*;
import static core.EnvProperties.isHeadlessMode;

public class ConfigurationExecution implements BeforeAllCallback, AfterAllCallback {
    private static final ThreadLocal<ConfigParams> LAUNCH_CONFIG = new ThreadLocal<>();
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();

    public static ConfigParams getLaunchConfig() {
        return LAUNCH_CONFIG.get();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        ConfigParams configParams = new ConfigParams();
        configParams.setMainAppUrl(getUrlWeb());
        LAUNCH_CONFIG.set(configParams);

        Playwright playwrightInstance = Playwright.create();
        PLAYWRIGHT.set(playwrightInstance);
        
        Browser browserInstance = launchBrowser(getBrowserType());
        BROWSER.set(browserInstance);
        
        configParams.setBrowser(browserInstance);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        Browser browserInstance = BROWSER.get();
        if (browserInstance != null) {
            browserInstance.contexts().forEach(BrowserContext::close);
            browserInstance.close();
            BROWSER.remove();
        }
        
        Playwright playwrightInstance = PLAYWRIGHT.get();
        if (playwrightInstance != null) {
            playwrightInstance.close();
            PLAYWRIGHT.remove();
        }
        
        LAUNCH_CONFIG.remove();
    }

    private Browser launchBrowser(BrowserType browserType) {
        Playwright playwrightInstance = PLAYWRIGHT.get();
        LaunchOptions options = new LaunchOptions()
                .setHeadless(isHeadlessMode())
                .setArgs(getBrowserArgs(browserType));

        return switch (browserType) {
            case CHROMIUM -> playwrightInstance.chromium().launch(options);
            case FIREFOX -> playwrightInstance.firefox().launch(options);
            case SAFARI -> playwrightInstance.webkit().launch(options);
        };
    }

    private List<String> getBrowserArgs(BrowserType browserType) {
        // Common args for all browsers
        List<String> commonArgs = Arrays.asList(
                DISABLE_SANDBOX.getValue(),
                NO_SANDBOX.getValue()
        );

        // Add browser-specific args if needed
        if (browserType == BrowserType.CHROMIUM) {
            return Arrays.asList(
                    DISABLE_SANDBOX.getValue(),
                    NO_SANDBOX.getValue(),
                    CONSOLE_MESSAGES.getValue()
            );
        }

        return commonArgs;
    }
}
