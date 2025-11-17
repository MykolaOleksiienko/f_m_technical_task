package tests.login;

import annotations.functional.Login;
import annotations.test_types.Regression;
import annotations.test_types.Smoke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.login.LoginPage;
import pages.two_factor_verification.TwoFactorVerificationPage;
import tests.BaseTest;
import utils.DataGenerator;

import java.util.stream.Stream;

import static enums.ValidationErrorMessage.INVALID_LOGIN_ERROR;

@Regression
@Login
public class LoginTests extends BaseTest {
    private LoginPage loginPage;
    private static final String validEmail = "MrBeast@gmail.com";
    private static final String validPhone = "399000000000";
    private static final String validPassword = "ValidPassword123";

    @BeforeEach
    void beforeEach() {
        loginPage = openFreshBrowserWithPageSiteUrl(LoginPage.class);
    }

    @Smoke
    @Test
    @DisplayName("Verify login form UI elements are visible")
    void verifyLoginFormElementsAreVisible() {
        loginPage.verifyLoginPageUI();
    }

    @Smoke
    @ParameterizedTest(name = "Successful login with {0}")
    @MethodSource("validCredentialsProvider")
    @DisplayName("Successful login with valid credentials")
    void successfulLoginWithValidCredentials(String username) {
        loginPage.fillUsername(username)
                .fillPassword(validPassword)
                .clickLoginButton();
        new TwoFactorVerificationPage().verifyTwoFactorVerificationPagePresent();
    }

    @Test
    @DisplayName("Login with empty fields - verify error messages")
    void loginWithEmptyFields() {
        loginPage.clickLoginButton()
                .verifyLoginFormErrorMessage(INVALID_LOGIN_ERROR);
    }

    @Test
    @DisplayName("Login with wrong password for valid email")
    void loginWithWrongPasswordForValidEmail() {
        loginPage.fillUsername(validEmail)
                .fillPassword("WrongPassword123")
                .clickLoginButton()
                .verifyLoginFormErrorMessage(INVALID_LOGIN_ERROR);
    }

    @Test
    @DisplayName("Verify fields can be cleared")
    void verifyFieldsCanBeCleared() {
        loginPage.fillUsername(validEmail)
                .fillPassword(validPassword)
                .verifyUsernameFieldValue(validEmail)
                .clearUsernameField()
                .clearPasswordField()
                .verifyUsernameFieldIsEmpty()
                .verifyPasswordFieldIsEmpty();
    }

    @ParameterizedTest(name = "Login with empty {0} - verify error message")
    @MethodSource("emptyFieldProvider")
    @DisplayName("Login with empty field validation")
    void loginWithEmptyField(String fieldType) {
        if ("password".equals(fieldType)) {
            loginPage.fillUsername(validEmail);
        } else {
            loginPage.fillPassword(validPassword);
        }
        loginPage.clickLoginButton()
                .verifyLoginFormErrorMessage(INVALID_LOGIN_ERROR);
    }

    @ParameterizedTest(name = "Login with invalid {0} format")
    @MethodSource("invalidFormatProvider")
    @DisplayName("Login with invalid format validation")
    void loginWithInvalidFormat(String fieldType, String invalidValue) {
        if ("email".equals(fieldType)) {
            loginPage.fillUsername(invalidValue)
                    .fillPassword(validPassword)
                    .clickLoginButton()
                    .verifyLoginFormErrorMessage(INVALID_LOGIN_ERROR);
        } else {
            loginPage.fillUsername(validEmail)
                    .fillPassword(invalidValue)
                    .clickLoginButton()
                    .verifyLoginFormErrorMessage(INVALID_LOGIN_ERROR);
        }
    }

    private static Stream<Arguments> invalidFormatProvider() {
        return Stream.of(
            Arguments.of("email", DataGenerator.generateAlphaStringWithLength(10)),
            Arguments.of("password", DataGenerator.generateAlphaStringWithLength(10))
        );
    }

    private static Stream<Arguments> validCredentialsProvider() {
        return Stream.of(
                Arguments.of(validEmail),
                Arguments.of(validPhone)
        );
    }

    private static Stream<Arguments> emptyFieldProvider() {
        return Stream.of(
                Arguments.of("username"),
                Arguments.of("password")
        );
    }
}
