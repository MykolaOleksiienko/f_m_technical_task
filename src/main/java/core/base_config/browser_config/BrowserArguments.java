package core.base_config.browser_config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserArguments {
    DISABLE_SANDBOX("--disable-setuid-sandbox"),
    NO_SANDBOX("--no-sandbox"),
    CONSOLE_MESSAGES("--webview-log-js-console-messages");

    private final String value;
}
