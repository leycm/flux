/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <leycm@proton.me> <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.flux.handler;

import de.leycm.flux.event.Event;

import java.lang.annotation.*;

/**
 * Annotation to mark methods as event handlers.
 * Methods annotated with {@code @Handler} will be automatically registered as event handlers.
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Event
 * @see HandlerPriority
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Handler {

    /**
     * The priority of this handler.
     * Defaults to {@link HandlerPriority#NORMAL}.
     *
     * @return the handler priority
     * @see HandlerPriority
     */
    HandlerPriority priority() default HandlerPriority.NORMAL;

}
