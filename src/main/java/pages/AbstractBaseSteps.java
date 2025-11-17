package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import exceptions.NoSuchPageException;
import io.qameta.allure.Step;

import java.util.logging.Logger;

import static com.microsoft.playwright.options.WaitForSelectorState.ATTACHED;
import static com.microsoft.playwright.options.WaitForSelectorState.DETACHED;
import static core.base_config.browser_config.BrowserContextHolder.get;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

public abstract class AbstractBaseSteps {
    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    public static final int DEFAULT_TIMEOUT = 20000;
    private final Page page;

    public AbstractBaseSteps() {
        this.page = getPage();
    }

    @Step("Get default page")
    protected Page getPage() {
        if (nonNull(page)) {
            return page;
        }
        var browserContext = get();
        if (browserContext == null || browserContext.pages().isEmpty()) {
            throw new NoSuchPageException();
        }
        return browserContext.pages().getLast();
    }

    @Step("Find Element by Locator: '{0}'")
    protected Locator findElement(String locator) {
        return page.locator(locator);
    }

    @Step("Find Element By Text : '{0}'")
    protected Locator findElementByText(String text) {
        return page.getByText(text);
    }

    @Step("Wait for element {0} to be present")
    protected Locator waitForPresent(String locator) {
        Locator element = findElement(locator);
        element.first().waitFor(new Locator.WaitForOptions().setState(ATTACHED));
        return element;
    }

    @Step("Click Element: '{0}'")
    protected void click(String locator) {
        waitForPresent(locator).click();
    }

    @Step("Fill field by locator {0} with text {1}")
    protected void fill(String locator, String text) {
        waitForPresent(locator).fill(text);
    }

    @Step("Clear Input {0}")
    protected void clearInput(String locator) {
        waitForPresent(locator).clear();
    }

    @Step("Get Element text value {0}")
    protected String getText(String locator) {
        return waitForPresent(locator).textContent().trim();
    }

    @Step("Element {0} is Present on the Page")
    protected boolean isPresent(String locator) {
        return findElement(locator).count() > 0;
    }

    @Step("Get current url of the page")
    public String getCurrentUrl() {
        page.waitForLoadState();
        return page.url();
    }

    @Step("Get value from text field by locator {0}")
    protected String getInputValue(Locator locator) {
        waitForPresent(locator);
        return locator.inputValue();
    }

    @Step("Wait for element {0} to be present")
    protected Locator waitForPresent(Locator locator) {
        waitForPresent(locator, true);

        return locator;
    }

    @Step("Wait for element {0} to be present {1} in DOM")
    protected void waitForPresent(Locator locator, boolean attached) {
        if (attached) {
            waitFor(locator, ATTACHED);
        } else {
            waitFor(locator, DETACHED);
        }
    }

    @Step("Wait for selector {0} with state {1}")
    protected void waitFor(Locator locator, WaitForSelectorState state) {
        locator.first().waitFor(new Locator.WaitForOptions().setState(state));
    }

    @Step("Wait For Element {0}")
    public Locator waitFor(String locator) {
        Locator elementLocator = page.locator(locator);
        elementLocator.waitFor();

        return elementLocator;
    }

    @Step("Wait Load State Load And Dom Content Load")
    protected void waitPageContentLoad() {
        waitForPage(LoadState.LOAD);
        waitForPage(LoadState.DOMCONTENTLOADED);
    }

    @Step("Wait Page For Load State {0}")
    private void waitForPage(LoadState loadState) {
        try {
            page.waitForLoadState(loadState, new Page.WaitForLoadStateOptions().setTimeout(DEFAULT_TIMEOUT));
        } catch (Exception e) {
            final var message = format("Page is not fully loaded with loadState %s after: %s milliseconds", loadState.name(), DEFAULT_TIMEOUT);
            logger.warning(message + "\nMore info: " + e.getMessage());
        }
    }
}
