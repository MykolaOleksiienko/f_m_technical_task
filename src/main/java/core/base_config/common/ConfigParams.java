package core.base_config.common;

import com.microsoft.playwright.Browser;
import lombok.Data;

@Data
final public class ConfigParams {
    private Browser browser;
    private String mainAppUrl;
}