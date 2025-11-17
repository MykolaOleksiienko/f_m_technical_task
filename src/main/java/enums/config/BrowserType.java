package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserType {
    CHROMIUM("chromium"),
    FIREFOX("firefox"),
    SAFARI("safari");

    private final String name;
}
