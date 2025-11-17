package annotations.common;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Retention(RUNTIME)
@Target(TYPE)
public @interface URI {

    boolean isAbsolute() default false;
    String url() default EMPTY;
}
