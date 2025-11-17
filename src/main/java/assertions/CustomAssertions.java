package assertions;

import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;

import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomAssertions {

    @Step("Assert that value {0} is equal to value {1}")
    public static void assertThatValueEqualToValue(Object actual, Object expected, String description) {
        Assertions.assertThat(actual)
                .as(format("%s should be equal to expected value %s", description, expected))
                .isEqualTo(expected);
    }

    @Step("Assert that elements in list of {0} is sorted by comparator {1} with description {2}")
    public static <E> void assertThatElementsInListIsSorted(List<E> values, Comparator<? super E> comparator, String description) {
        assertThat(values)
                .isNotEmpty()
                .as(description)
                .isSortedAccordingTo(comparator);
    }
}
