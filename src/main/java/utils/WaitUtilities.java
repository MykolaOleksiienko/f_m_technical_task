package utils;

import com.microsoft.playwright.Locator;
import exceptions.ElementTimeoutException;
import io.qameta.allure.Step;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.fail;

public class WaitUtilities {
    private static final int DEFAULT_TIME_OUT_IN_SECONDS = 30;
    private static final int PULLING_TIME = 300;

    @Step("Wait for condition: '{0}' and error message '{1}'")
    public static void waitForCondition(Supplier<Boolean> condition, String errorMessage) {
        waitForCondition(condition, errorMessage, DEFAULT_TIME_OUT_IN_SECONDS);
    }

    @Step("Wait for condition: '{0}' with error message '{1}' and timeout: '{2}'")
    public static void waitForCondition(Supplier<Boolean> condition, String errorMessage, int seconds) {
        var timeout = seconds * 1000;
        while (!condition.get()) {
            waitForTime(PULLING_TIME);
            timeout -= PULLING_TIME;
            if (timeout <= 0) throw new ElementTimeoutException(errorMessage);
        }
    }

    @Step("Wait for condition: '{0}' with timeout: '{1}' milliseconds and error message '{2}'")
    public static void waitForCondition(Supplier<Boolean> condition, long timeout, String errorMessage) {
        var initialTimeOut = timeout;
        while (!condition.get()) {
            waitForTime(PULLING_TIME);
            timeout -= PULLING_TIME;
            if (timeout <= 0)
                throw new ElementTimeoutException(String.format("%s after :%s milliseconds", errorMessage, initialTimeOut));
        }
    }

    @Step("Wait for time {0} milliseconds")
    public static void waitForTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            fail(String.format("Thread sleep was interrupted: %s", e.getMessage()));
        }
    }

}
