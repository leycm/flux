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
package de.leycm.flux.event;

import java.time.Duration;
import java.time.Instant;

/**
 * Base interface for all events in the event system.
 * <p>
 * This interface provides common functionality for event tracking, cancellation support,
 * monitoring capabilities, and debugging information.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Cancelable
 * @see Monitorable
 */
public interface Event {

    /**
     * Returns the timestamp when this event was created.
     * <p>
     * The timestamp represents the exact moment when the event instance was created
     * and is used for age calculation and event ordering.
     * </p>
     *
     * @return the instant when this event was created, never {@code null}
     * @author LeyCM
     * @since 1.0.1
     */
    Instant timestamp();

    /**
     * Checks if this event supports cancellation.
     * <p>
     * An event is cancelable if it implements the {@link Cancelable} interface.
     * Cancelable events can be prevented from further processing by event handlers.
     * </p>
     *
     * @return {@code true} if this event implements {@link Cancelable}, {@code false} otherwise
     * @author LeyCM
     * @since 1.0.1
     * @see Cancelable
     */
    default boolean isCancelable() {
        return this instanceof Cancelable;
    }

    /**
     * Checks if this event supports monitoring.
     * <p>
     * An event is monitorable if it implements the {@link Monitorable} interface.
     * Monitorable events can create copies for observation without affecting the original event.
     * </p>
     *
     * @return {@code true} if this event implements {@link Monitorable}, {@code false} otherwise
     * @author LeyCM
     * @since 1.0.1
     * @see Monitorable
     */
    default boolean isMonitorable() {
        return this instanceof Monitorable;
    }

    /**
     * Returns the creation timestamp of this event.
     * <p>
     * This is a convenience method that delegates to {@link #timestamp()}.
     * </p>
     *
     * @return the instant when this event was created, never {@code null}
     * @author LeyCM
     * @since 1.0.1
     * @see #timestamp()
     */
    default Instant createAt() {
        return timestamp();
    }

    /**
     * Calculates the age of this event in milliseconds.
     * <p>
     * The age is calculated as the duration between the event's timestamp and the current time.
     * This can be useful for event timing analysis and performance monitoring.
     * </p>
     *
     * @return the age of this event in milliseconds
     * @author LeyCM
     * @since 1.0.1
     * @see #timestamp()
     * @see Duration
     */
    default long age() {
        return Duration.between(timestamp(), Instant.now()).toMillis();
    }

    /**
     * Returns a debug string representation of this event.
     * <p>
     * The debug string includes the simple class name and the hash code of the event instance.
     * This provides a concise identifier useful for logging and debugging purposes.
     * </p>
     *
     * @return a debug string in the format "ClassName@HashCode"
     * @author LeyCM
     * @since 1.0.1
     */
    default String debug() {
        return getClass().getSimpleName() + "@" + hashCode();
    }

}
