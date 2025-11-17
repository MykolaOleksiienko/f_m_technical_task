package core.base_config.common;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.CDPSession;
import com.microsoft.playwright.Page;
import core.IPageHelper;
import enums.SiteUrl;
import io.qameta.allure.Step;
import pages.AbstractBaseSteps;

import static core.base_config.configuration_execution.ConfigurationExecution.LAUNCH_CONFIG;
import static java.lang.String.format;
import static pages.AbstractBaseSteps.DEFAULT_TIMEOUT;

@SuppressWarnings("all")
public abstract class CommonBase implements IPageHelper {
    private Page page;
    private BrowserContext browserContext;
    private static final int NAVIGATION_TIMEOUT = 60000;
    public static final ThreadLocal<BrowserContext> BROWSER_CONTEXTS = new ThreadLocal<>();

    public Browser getBrowser() {
        return LAUNCH_CONFIG.get().getBrowser();
    }

    @Step("Close All Contexts")
    public void closeAllContexts() {
        BROWSER_CONTEXTS.get().browser().contexts().forEach(BrowserContext::close);
    }

    @Step("Open Fresh Browser With Page Site Url: '{0}' isBackOffice: '{1}'")
    public void openFreshBrowserWithPageSiteUrl(SiteUrl navigationUrl, boolean isBackOffice) {
        browserContext = getBrowser().newContext();
        BROWSER_CONTEXTS.set(browserContext);
        initPage();
        setDefaultDesktopScreenSize();
        openUrl(LAUNCH_CONFIG.get().getMainAppUrl().concat(format("/%s", navigationUrl)));
    }

    @Step("Open Fresh Browser on Page: '{0}'")
    public <T extends AbstractBaseSteps> T openFreshBrowserWithPageSiteUrl(Class<T> clazz) {
        browserContext = getBrowser().newContext();
        BROWSER_CONTEXTS.set(browserContext);
        initPage();
        setDefaultDesktopScreenSize();
        openUrl(getUrl(clazz));

        return initializePageInstance(clazz);
    }

    @Step("Navigate to page: '{0}' with params: '{1}'")
    public <T extends AbstractBaseSteps> T navigateToPageWithParams(Class<T> clazz, Object... params) {
        openUrl(format(getUrl(clazz), params));
        return initializePageInstance(clazz);
    }

    @Step("Navigate to url: '{0}' with params: '{1}'")
    public void navigateToUrlWithParams(SiteUrl url, Object... params) {
        openUrl(LAUNCH_CONFIG.get().getMainAppUrl().concat(format("/%s/%s", url, params)));
    }

    @Step("Open Fresh Browser")
    public void openFreshBrowser() {
        browserContext = getBrowser().newContext();
        BROWSER_CONTEXTS.set(browserContext);
        initPage();
        setDefaultDesktopScreenSize();
    }

    @Step("Open Browser With URL: '{0}'")
    public void openBrowserWithURL(String url) {
        openFreshBrowser();
        page.navigate(url);
        page.waitForLoadState();
    }

    @Step("Set View Page Size width {0} height {1}")
    public void setViewPageSize(int width, int height) {
        page.setViewportSize(width, height);
    }


    @Step("Clear Browser local storage")
    public void clearLocalStorage() {
        page.evaluate("localStorage.clear()");
    }

    @Step("Clear Browser Session storage")
    public void clearSessionStorage() {
        page.evaluate("sessionStorage.clear()");
    }

    @Step("Clear Browser Cache storage")
    public void clearBrowserCacheStorage() {
        CDPSession client = page.context().newCDPSession(page);
        client.send("Network.clearBrowserCookies");
        client.send("Network.clearBrowserCache");
    }
    @Step("Set Page Full Screen")
    private void setDefaultDesktopScreenSize() {
        setViewPageSize(1920, 1080);
    }

    @Step("Initialize page with default configuration")
    private void initPage() {
        page = browserContext.newPage();
        setPageConfiguration(page);
    }

    @Step("Set Page Configuration {0}")
    private Page setPageConfiguration(Page page) {
        page.setDefaultTimeout(DEFAULT_TIMEOUT);
        page.setDefaultNavigationTimeout(NAVIGATION_TIMEOUT);
        return page;
    }

    @Step("Open Url: '{0}'")
    private void openUrl(String url) {
        page.navigate(url);
        page.waitForLoadState();
    }
}
