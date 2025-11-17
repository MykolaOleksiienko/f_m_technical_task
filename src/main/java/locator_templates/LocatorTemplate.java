package locator_templates;

import static java.lang.String.format;

public class LocatorTemplate {

    /**
     * Get Locator by Data Attribute
     *
     * @param attributeValue Attribute Value
     * @return locator.
     */
    public static String getLocatorByDataAttribute(String attributeValue) {
        return format("[data-ui-test='%s']", attributeValue);
    }
}
