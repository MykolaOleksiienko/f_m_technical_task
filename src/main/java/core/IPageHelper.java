package automation.shared.library.web.helpers.component;

import automation.shared.library.core.base_config.annotations.URI;
import automation.shared.library.enums.SiteUrlLanguage;
import automation.shared.library.web.actions.AbstractBaseSteps;
import io.qameta.allure.Step;

import static automation.shared.library.core.base_config.configuration_execution.ConfigurationExecution.LAUNCH_CONFIG;
import static java.lang.String.format;

public interface IPageHelper {

    String BO = "bo";

    @Step("Get url")
    default String getUrl() {
        return getUri().isAbsolute() ? fetchUrl() : getEndpoint() + fetchUrl();
    }

    @Step("Get url from class: '{0}'")
    default <T extends AbstractBaseSteps> String getUrl(Class<T> clazz) {
        return getUri(clazz).isAbsolute() ? fetchUrl(clazz) : getEndpoint(clazz) + fetchUrl(clazz);
    }

    @Step("Initialize page instance from class: '{0}'")
    default <T extends AbstractBaseSteps> T initializePageInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(format("Something went wrong during page: %s initialization", clazz), e);
        }
    }

    @Step("Fetch URL from class: '{0}'")
    default <T extends AbstractBaseSteps> String fetchUrl(Class<T> clazz) {
        if (getUri(clazz).url().isEmpty()) return getUri(clazz).format();
        return getUri(clazz).url();
    }

    @Step("Fetch URL")
    default String fetchUrl() {
        if (getUri().url().isEmpty()) return getUri().format();
        return getUri().url();
    }

    @Step("Get endpoint")
    default String getEndpoint() {
        return format(STR."\{LAUNCH_CONFIG.get().getMainAppUrl()}/%s/", (getClass().getSimpleName().toLowerCase().startsWith(BO) ? BO : SiteUrlLanguage.LANGUAGE_EN_US.getName()));
    }

    @Step("Get endpoint from class: '{0}'")
    default <T extends AbstractBaseSteps> String getEndpoint(Class<T> clazz) {
        return format(STR."\{LAUNCH_CONFIG.get().getMainAppUrl()}/%s/", (clazz.getSimpleName().toLowerCase().startsWith(BO) ? BO : SiteUrlLanguage.LANGUAGE_EN_US.getName()));
    }

    @Step("Get URI")
    default URI getUri() {
        if (getClass().isAnnotationPresent(URI.class)) return getClass().getAnnotation(URI.class);

        throw new IllegalStateException(format("%s page doesn't have URI annotation", this.getClass()));
    }

    @Step("Get URI from class: '{0}'")
    default <T extends AbstractBaseSteps> URI getUri(Class<T> clazz) {
        if (clazz.isAnnotationPresent(URI.class)) return clazz.getAnnotation(URI.class);

        throw new IllegalStateException(format("%s page doesn't have URI annotation", clazz));
    }
}
