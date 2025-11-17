package core;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;


public class EnvProperties {
    private static final String THREAD_COUNT = "THREAD_COUNT";

    public static String getSystemProperty(String name) {
        return ofNullable(System.getProperty(name))
                .orElseThrow(() -> new IllegalArgumentException(STR."Wrong name of the property or property with name: \{name} does not exist"));
    }

    public static int getThreadCount() {
        return parseInt(getSystemProperty(EnvProperties.THREAD_COUNT));
    }
}
