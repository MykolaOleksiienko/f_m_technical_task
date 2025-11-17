package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationErrorMessage {

    NAME_REQUIRED("Name is required"),
    EMAIL_REQUIRED("Email is required"),
    MESSAGE_REQUIRED("Message is required"),
    INVALID_LOGIN_ERROR("Invalid login/password. Please check the spelling and try again. You can also use recover password.");

    private final String value;
}
