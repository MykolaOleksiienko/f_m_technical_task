package automation.shared.library.core.base_config.configuration_execution;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.logging.Logger;

public final class ApiConfigurationExecution extends AppConfigInit implements BeforeAllCallback, AfterAllCallback {
    private static final Logger logger = Logger.getLogger(ApiConfigurationExecution.class.getName());

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        super.beforeAll(extensionContext);
        logger.info("'ApiConfigurationExecution is starting to execute!");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        super.afterAll(extensionContext);
    }
}
