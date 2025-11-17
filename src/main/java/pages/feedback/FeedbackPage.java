package pages.feedback;

import annotations.common.URI;
import assertions.CustomAssertions;
import enums.ValidationErrorMessage;
import io.qameta.allure.Step;
import pages.AbstractBaseSteps;

import static locator_templates.LocatorTemplate.getLocatorByDataAttribute;

@URI(url = "/feedback")
public class FeedbackPage extends AbstractBaseSteps {
    private static final String nameField = getLocatorByDataAttribute("feedback-name-input");
    private static final String emailField = getLocatorByDataAttribute("feedback-email-input");
    private static final String messageField = getLocatorByDataAttribute("feedback-message-input");
    private static final String sendButton = getLocatorByDataAttribute("feedback-send-button");
    private static final String confirmationMessage = getLocatorByDataAttribute("feedback-confirmation-message");
    private static final String nameFieldError = getLocatorByDataAttribute("feedback-name-error");
    private static final String emailFieldError = getLocatorByDataAttribute("feedback-email-error");
    private static final String messageFieldError = getLocatorByDataAttribute("feedback-message-error");

    @Step("Fill name value: '{0}'")
    public FeedbackPage fillName(String value) {
        fill(nameField, value);
        return this;
    }

    @Step("Fill email value: '{0}'")
    public FeedbackPage fillEmail(String value) {
        fill(emailField, value);
        return this;
    }

    @Step("Fill message value: '{0}'")
    public FeedbackPage fillMessage(String value) {
        fill(messageField, value);
        return this;
    }

    @Step("Click send button")
    public FeedbackPage clickSendButton() {
        click(sendButton);
        return this;
    }

    @Step("Verify confirmation message: '{0}'")
    public void verifyConfirmationMessage(String expectedMessage) {
        CustomAssertions.assertThatElementTextEqualTo(waitFor(confirmationMessage), expectedMessage, "Confirmation Message");
    }

    @Step("Verify name error message: '{0}'")
    public FeedbackPage verifyNameErrorMessage(ValidationErrorMessage expectedMessage) {
        CustomAssertions.assertThatElementTextEqualTo(waitFor(nameFieldError), expectedMessage.getValue(), "Name Error Message");
        return this;
    }

    @Step("Verify email error message: '{0}'")
    public FeedbackPage verifyEmailErrorMessage(ValidationErrorMessage expectedMessage) {
        CustomAssertions.assertThatElementTextEqualTo(waitFor(emailFieldError), expectedMessage.getValue(), "Email Error Message");
        return this;
    }

    @Step("Verify message error message: '{0}'")
    public void verifyMessageErrorMessage(ValidationErrorMessage expectedMessage) {
        CustomAssertions.assertThatElementTextEqualTo(waitFor(messageFieldError), expectedMessage.getValue(), "Message Error Message");
    }
}
