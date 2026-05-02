package de.leycm.flux;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FluxHandler {
    // note: this should always be in sync with EventPriority#DEFAULT#priority()
    int priority() default 0;
}
