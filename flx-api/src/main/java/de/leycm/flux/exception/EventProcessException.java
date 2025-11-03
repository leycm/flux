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
package de.leycm.flux.exception;

import lombok.NonNull;

/**
 * Thrown when an event handler cannot be registered due to configuration issues,
 * security restrictions, or other registration-related problems.
 *
 * @author LeyCM
 * @since 1.0.1
 */
public class EventProcessException extends RuntimeException {

    /**
     * Constructs a new ProcessHandlerException with the specified detail message.
     *
     * @param message the detail message
     */
    public EventProcessException(final @NonNull String message) {
        super(message);
    }

    /**
     * Constructs a new ProcessHandlerException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public EventProcessException(final @NonNull String message,
                                 final @NonNull Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ProcessHandlerException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public EventProcessException(final @NonNull Throwable cause) {
        super(cause);
    }

}
