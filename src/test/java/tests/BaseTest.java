package tests;

import annotations.common.RunBrowser;
import annotations.common.URI;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import core.base_config.common.ThreadLoggingListener;
import core.listeners.ListenerTestExecution;
import io.qameta.allure.Step;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.AbstractBaseSteps;

import static core.EnvProperties.getViewportHeight;
import static core.EnvProperties.getViewportWidth;
import static core.base_config.browser_config.BrowserContextHolder.set;
import static core.base_config.configuration_execution.ConfigurationExecution.getLaunchConfig;
import static pages.AbstractBaseSteps.DEFAULT_TIMEOUT;
import static java.lang.String.format;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({ListenerTestExecution.class, ThreadLoggingListener.class})
@RunBrowser()
public abstract class BaseTest {
    private static final int NAVIGATION_TIMEOUT = 60000;

    protected Browser getBrowser() {
        return getLaunchConfig().getBrowser();
    }

    @Step("Create browser context")
    private BrowserContext createBrowserContext() {
        BrowserContext browserContext = getBrowser().newContext();
        set(browserContext);
        return browserContext;
    }

    @Step("Create and configure page with viewport: {0}x{1}")
    private Page createAndConfigurePage(BrowserContext browserContext, int width, int height) {
        Page page = browserContext.newPage();
        page.setViewportSize(width, height);
        page.setDefaultTimeout(DEFAULT_TIMEOUT);
        page.setDefaultNavigationTimeout(NAVIGATION_TIMEOUT);
        return page;
    }

    @Step("Navigate to URL: '{0}'")
    private void navigateToUrl(Page page, String url) {
        page.navigate(url);
        page.waitForLoadState();
    }

    @Step("Open fresh browser on page: '{0}'")
    public <T extends AbstractBaseSteps> T openFreshBrowserWithPageSiteUrl(Class<T> clazz) {
        BrowserContext browserContext = createBrowserContext();
        int viewportWidth = getViewportWidth();
        int viewportHeight = getViewportHeight();
        Page page = createAndConfigurePage(browserContext, viewportWidth, viewportHeight);
        String url = getUrl(clazz);
        navigateToUrl(page, url);
        return initializePageInstance(clazz);
    }

    @Step("Get url from class: '{0}'")
    private <T extends AbstractBaseSteps> String getUrl(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(URI.class)) {
            throw new IllegalStateException(format("Class %s doesn't have @URI annotation", clazz.getName()));
        }
        
        URI uri = clazz.getAnnotation(URI.class);
        String url = uri.url();
        
        if (uri.isAbsolute()) {
            return url;
        } else {
            String mainAppUrl = getLaunchConfig().getMainAppUrl();
            return mainAppUrl + url;
        }
    }

    @Step("Initialize page instance from class: '{0}'")
    private <T extends AbstractBaseSteps> T initializePageInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to initialize page instance: %s", clazz.getName()), e);
        }
    }
}












