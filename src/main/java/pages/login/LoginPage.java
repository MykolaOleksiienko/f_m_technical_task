package pages.login_page;

import annotations.common.URI;
import assertions.CustomAssertions;
import enums.ValidationErrorMessage;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import pages.AbstractBaseSteps;

import static assertions.CustomAssertions.assertThatElementIsVisible;
import static locator_templates.LocatorTemplate.getLocatorByDataAttribute;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertAll;

@URI(url = "/login")
public class LoginPage extends AbstractBaseSteps {
    private static final String usernameField = getLocatorByDataAttribute("customer-username-input");
    private static final String passwordField = getLocatorByDataAttribute("customer-password-input");
    private static final String loginButton = getLocatorByDataAttribute("customer-login-button");
    private static final String usernameFieldError = getLocatorByDataAttribute("customer-username-error");
    private static final String passwordFieldError = getLocatorByDataAttribute("customer-password-error");

    @Step("Fill username value: '{0}'")
    public LoginPage fillUserName(String value) {
        fill(usernameField, value);
        return this;
    }

    @Step("Fill password value: '{0}'")
    public LoginPage fillPassword(String value) {
        fill(passwordField, value);
        return this;
    }

    @Step("Click login button")
    public LoginPage clickLoginButton() {
        click(loginButton);
        return this;
    }

    @Step("Verify username error message: '{0}'")
    public LoginPage verifyUsernameErrorMessage(ValidationErrorMessage expectedErrorMessage) {
        CustomAssertions.assertThatElementTextEqualTo(waitFor(usernameFieldError), expectedErrorMessage.getValue(), "Username Error Message");
        return this;
    }

    @Step("Verify password error message: '{0}'")
    public LoginPage verifyPasswordErrorMessage(ValidationErrorMessage expectedErrorMessage) {
        CustomAssertions.assertThatElementTextEqualTo(waitFor(passwordFieldError), expectedErrorMessage.getValue(), "Password Error Message");
        return this;
    }

    @Step("Verify login page UI")
    public void verifyLoginPageUI() {
        assertAll(
                () -> assertThatElementIsVisible(waitForPresent(usernameField), true, "Username Field"),
                () -> assertThatElementIsVisible(waitForPresent(passwordField), true, "Password Field"),
                () -> assertThatElementIsVisible(waitForPresent(loginButton), true, "Login Button")
        );
    }

    @Step("Verify username field value: '{0}'")
    public LoginPage verifyUsernameFieldValue(String expectedValue) {
        CustomAssertions.assertThatInputFieldElementValueEqualToValue(waitForPresent(usernameField), expectedValue, "Username");
        return this;
    }

    @Step("Verify username field is empty")
    public LoginPage verifyUsernameFieldIsEmpty() {
        CustomAssertions.assertThatInputFieldElementValueEqualToValue(waitForPresent(usernameField), EMPTY, "Username");
        return this;
    }

    @Step("Verify password field is empty")
    public LoginPage verifyPasswordFieldIsEmpty() {
        CustomAssertions.assertThatInputFieldElementValueEqualToValue(waitForPresent(passwordField), EMPTY, "Password");
        return this;
    }

    @Step("Clear username field")
    public LoginPage clearUsernameField() {
        clearInput(usernameField);
        return this;
    }

    @Step("Clear password field")
    public LoginPage clearPasswordField() {
        clearInput(passwordField);
        return this;
    }
}
