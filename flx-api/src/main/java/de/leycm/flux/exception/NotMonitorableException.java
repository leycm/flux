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

import de.leycm.flux.event.Monitorable;

/**
 * Exception thrown when attempting to monitor an event that does not implement {@link Monitorable}.
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Monitorable
 */
public class NotMonitorableException extends RuntimeException {

    /**
     * Constructs a new NotMonitorableException with the specified detail message.
     *
     * @param message the detail message
     * @author LeyCM
     * @since 1.0.1
     */
    public NotMonitorableException(String message) {
        super(message);
    }

    /**
     * Constructs a new NotMonitorableException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     * @author LeyCM
     * @since 1.0.1
     */
    public NotMonitorableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new NotMonitorableException with the specified cause.
     *
     * @param cause the cause of the exception
     * @author LeyCM
     * @since 1.0.1
     */
    public NotMonitorableException(Throwable cause) {
        super(cause);
    }

}
