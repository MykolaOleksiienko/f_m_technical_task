package tests.feedback;

import annotations.functional.Feedback;
import annotations.test_types.Regression;
import annotations.test_types.Smoke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.feedback.FeedbackPage;
import tests.BaseTest;

import static enums.ValidationErrorMessage.EMAIL_REQUIRED;
import static enums.ValidationErrorMessage.MESSAGE_REQUIRED;
import static enums.ValidationErrorMessage.NAME_REQUIRED;

@Regression
@Feedback
public class FeedbackTests extends BaseTest {
    private FeedbackPage feedbackPage;
    private static final String VALID_NAME = "Mister Beast";
    private static final String VALID_EMAIL = "MrBeast@gmail.com";
    private static final String VALID_MESSAGE = "This is a test feedback message.";
    private static final String EXPECTED_CONFIRMATION_MESSAGE = "Thank you for your feedback!";

    @BeforeEach
    void beforeEach() {
        feedbackPage = openFreshBrowserWithPageSiteUrl(FeedbackPage.class);
    }

    @Smoke
    @Test
    @DisplayName("Submit feedback form with all valid mandatory fields")
    void submitFeedbackFormWithValidData() {
        feedbackPage.fillName(VALID_NAME)
                .fillEmail(VALID_EMAIL)
                .fillMessage(VALID_MESSAGE)
                .clickSendButton()
                .verifyConfirmationMessage(EXPECTED_CONFIRMATION_MESSAGE);
    }

    @Smoke
    @Test
    @DisplayName("Submit feedback form with empty mandatory fields")
    void submitFeedbackFormWithEmptyFields() {
        feedbackPage.clickSendButton()
                .verifyNameErrorMessage(NAME_REQUIRED)
                .verifyEmailErrorMessage(EMAIL_REQUIRED)
                .verifyMessageErrorMessage(MESSAGE_REQUIRED);
    }
}
