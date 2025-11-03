/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a> l <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.flux.registry;

import de.leycm.flux.event.Event;
import de.leycm.flux.handler.HandlerPriority;
import lombok.NonNull;

/**
 * Represents a single handler executor responsible for invoking a specific handler method
 * in response to an event. Implementations of this interface encapsulate the metadata
 * and execution logic for individual event handlers.
 * <p>
 * A {@code HandlerExecutor} maintains information about the handler's unique identifier,
 * its execution priority, and provides a method to trigger the handler with a given event.
 * This allows the event bus to manage and invoke handlers in a controlled and predictable
 * manner.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public interface HandlerExecutor {

    /**
     * Returns the unique identifier of this handler.
     * <p>
     * The ID typically follows the format
     * {@code com.example.ExampleHandlerList#functionName(com.example.event.ExampleEvent)},
     * which uniquely identifies the method within a handler list class.
     * </p>
     *
     * @return the unique string identifier for this handler
     */
    String id();

    /**
     * Returns the priority of this handler, which determines the order of execution
     * relative to other handlers of the same event type.
     * <p>
     * Higher priority handlers are executed before lower priority ones. This is used
     * by the event bus to sort handlers before firing events.
     * </p>
     *
     * @return the {@link HandlerPriority} of this handler
     */
    HandlerPriority priority();

    /**
     * Triggers the execution of this handler with the specified event.
     * <p>
     * When invoked, the handler method associated with this executor will process
     * the provided {@link Event}.
     * </p>
     *
     * @param event the event instance to pass to the handler
     */
    void fire(final @NonNull Event event);

}
