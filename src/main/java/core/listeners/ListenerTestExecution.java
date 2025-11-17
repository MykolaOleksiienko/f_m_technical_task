package core.listeners;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Objects;

import static core.base_config.browser_config.BrowserContextHolder.remove;
import static core.base_config.configuration_execution.ConfigurationExecution.getLaunchConfig;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class ListenerTestExecution implements AfterEachCallback {

    @Override
    @Step("After Each Callback with context: '{0}'")
    public void afterEach(ExtensionContext context) {
        final var requiredTestInstance = context.getRequiredTestInstance();
        final var method = context.getRequiredTestMethod();
        final var nameScreen = format("Class: %s, Test: %s", requiredTestInstance.getClass().getSimpleName(), method.getName());
        var launchConfig = getLaunchConfig();
        if (launchConfig != null && context.getExecutionException().isPresent()) {
            makeScreenShot(launchConfig.getBrowser(), nameScreen);
        }
        if (launchConfig != null) {
            closeBrowserContexts(launchConfig.getBrowser());
        }
        remove();
    }

    @Step("ScreenShot For Failed Test: Browser: '{0}' Name Screenshot: '{1}'")
    private void makeScreenShot(Browser browser, String nameScreen) {
        browser.contexts().forEach(browserContext -> browserContext.pages().forEach(page ->
                Allure.addAttachment(nameScreen.concat(" --- ").concat("__Time__").concat(LocalDateTime.now().toString()),
                        new ByteArrayInputStream(((page.screenshot(new Page.ScreenshotOptions().setFullPage(true))))))));
    }

    @Step("Close Browser: '{0}' Contexts")
    private void closeBrowserContexts(Browser browser) {
        final var browserContextsPresent = ofNullable(browser).isPresent();
        Allure.addAttachment("State of browser contexts: ", String.valueOf(browserContextsPresent));
        if (browserContextsPresent) {
            browser.contexts().stream().filter(Objects::nonNull).forEach(BrowserContext::close);
        }
    }
}
