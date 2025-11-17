package core.base_config.browser_config;

import com.microsoft.playwright.BrowserContext;

public class BrowserContextHolder {
    private static final ThreadLocal<BrowserContext> BROWSER_CONTEXTS = new ThreadLocal<>();

    public static BrowserContext get() {
        return BROWSER_CONTEXTS.get();
    }

    public static void set(BrowserContext browserContext) {
        BROWSER_CONTEXTS.set(browserContext);
    }

    public static void remove() {
        BROWSER_CONTEXTS.remove();
    }
}

