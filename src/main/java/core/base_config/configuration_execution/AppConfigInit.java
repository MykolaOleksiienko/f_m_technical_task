package automation.shared.library.core.base_config.configuration_execution;

import automation.shared.library.core.base_config.common.ConfigParams;
import automation.shared.library.helpers.EnvProperties;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static automation.shared.library.core.base_config.configuration_execution.ConfigurationExecution.LAUNCH_CONFIG;
import static automation.shared.library.helpers.EnvProperties.getUrlBackOffice;
import static automation.shared.library.helpers.EnvProperties.getUrlWeb;

class AppConfigInit implements BeforeAllCallback, AfterAllCallback {
    protected ConfigParams configParams = new ConfigParams();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        configParams.setMainAppUrl(getUrlWeb());
        configParams.setBackOfficeUrl(getUrlBackOffice());
        LAUNCH_CONFIG.set(configParams);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        LAUNCH_CONFIG.remove();
    }
}
