package core;

import enums.config.BrowserType;
import enums.config.ViewportPreset;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

public class EnvProperties {
    private static final String HEADLESS_MODE = "HEADLESS";
    private static final String URL_WEB = "url_web";
    private static final String BROWSER_TYPE = "BROWSER_TYPE"; // chromium, firefox, safari
    private static final String VIEWPORT_WIDTH = "VIEWPORT_WIDTH";
    private static final String VIEWPORT_HEIGHT = "VIEWPORT_HEIGHT";
    private static final String THREAD_COUNT = "THREAD_COUNT";

    public static String getSystemProperty(String name) {
        return ofNullable(System.getProperty(name))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Wrong name of the property or property with name: %s does not exist", name)));
    }

    public static BrowserType getBrowserType() {
        String browser = ofNullable(System.getenv(BROWSER_TYPE)).orElse("chromium");
        try {
            return BrowserType.valueOf(browser.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to CHROMIUM if invalid browser type provided
            return BrowserType.CHROMIUM;
        }
    }

    public static int getViewportWidth() {
        String width = System.getenv(VIEWPORT_WIDTH);
        if (width != null) {
            try {
                return Integer.parseInt(width);
            } catch (NumberFormatException e) {
                // Return default if invalid
            }
        }
        return ViewportPreset.DESKTOP_1920x1080.getWidth();
    }

    public static int getViewportHeight() {
        String height = System.getenv(VIEWPORT_HEIGHT);
        if (height != null) {
            try {
                return Integer.parseInt(height);
            } catch (NumberFormatException e) {
                // Return default if invalid
            }
        }
        return ViewportPreset.DESKTOP_1920x1080.getHeight();
    }

    public static boolean isHeadlessMode() {
        var mode = ofNullable(System.getenv(HEADLESS_MODE)).orElse("false");
        return Boolean.parseBoolean(mode);
    }

    public static String getUrlWeb() {
        return getSystemProperty(URL_WEB);
    }

    public static int getThreadCount() {
        String threadCount = System.getProperty(THREAD_COUNT);
        if (threadCount != null) {
            try {
                return parseInt(threadCount);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }
}
