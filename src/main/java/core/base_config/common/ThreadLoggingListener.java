package core.base_config.common;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.logging.Logger;

public class ThreadLoggingListener implements BeforeEachCallback{
    public void beforeEach(ExtensionContext extensionContext) {
        Logger.getLogger(this.getClass().getName()).info((String.format("%s has been started at thread: %s", extensionContext.getDisplayName(), Thread.currentThread().getName())));
    }

}
