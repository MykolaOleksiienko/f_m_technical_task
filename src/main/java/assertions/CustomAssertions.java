package assertions;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomAssertions {

    @Step("Assert that value {0} is equal to value {1}")
    public static void assertThatValueEqualToValue(Object actual, Object expected, String description) {
        assertThat(actual)
                .as(format("%s should be equal to expected value %s", description, expected))
                .isEqualTo(expected);
    }

    @Step("Assert That Element {0} Is Visible {1} with description {2}")
    public static void assertThatElementIsVisible(Locator locator, boolean isVisible, String description) {
        var assertMessage = isVisible ? format("%s should be Visible", description)
                : format("%s should not be Visible", description);

        assertThat(locator.isVisible())
                .as(assertMessage)
                .isEqualTo(isVisible);
    }

    @Step("Assert That Element {0} Text Equal To {1} with description {2}")
    public static void assertThatElementTextEqualTo(Locator locator, String expectedValue, String description) {
        assertThat(locator.textContent().trim())
                .as(format("%s should be equal expected value %s", description, expectedValue))
                .isEqualTo(expectedValue);
    }

    @Step("Assert That Element {0} Inner Text Equal To {1} with description {2}")
    public static void assertThatElementInnerTextEqualTo(Locator locator, String expectedValue, String description) {
        String actualText = locator.innerText()
                .replaceAll("\\r?\\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        assertThat(actualText)
                .as(format("%s should be equal expected value %s", description, expectedValue))
                .isEqualTo(expectedValue.replaceAll("\\r?\\n", " ").replaceAll("\\s+", " ").trim());
    }

    @Step("Assert That Input field element {0} value equal to expected value {1} with description {2}")
    public static void assertThatInputFieldElementValueEqualToValue(Locator locator, String expectedValue, String description) {
        final var inputValue = locator.inputValue().trim();
        assertThat(inputValue)
                .as(format("%s input field value should be equal expected", description))
                .isEqualTo(expectedValue);
    }
}
