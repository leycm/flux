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

/**
 * Enum representing the priority levels for event handlers.
 * Handlers are executed in the order: EARLY -> NORMAL -> LATE -> MONITOR.
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Handler
 */
public enum HandlerPriority {
    /**
     * Early execution - handlers with this level run first.
     */
    EARLY,

    /**
     * Normal execution - default priority level.
     */
    NORMAL,

    /**
     * Late execution - handlers with this level run after normal handlers.
     */
    LATE,

    /**
     * Monitor level - handlers with this level only receive copies of events for observation.
     * No modifications to the event state are allowed at this level.
     */
    MONITOR;

    /**
     * Checks if this handler level is the monitor level.
     *
     * @return {@code true} if this level is {@code MONITOR}, {@code false} otherwise
     */
    public boolean isMonitor() {
        return this.equals(MONITOR);
    }

}
