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

/**
 * Interface for events that can be monitored.
 * Monitorable events can create copies of themselves for read-only observation.
 *
 * @param <E> the type of event that implements this interface, must extend {@link Event}
 * @author LeyCM
 * @since 1.0.1
 * @see Event
 */
public interface Monitorable<E extends Event> {

    /**
     * Creates a copy of this event for monitoring purposes.
     * The copy should be used for read-only observation and should not affect the original event.
     *
     * @return a copy of this event
     * @author LeyCM
     * @since 1.0.1
     */
    E copy();
}
