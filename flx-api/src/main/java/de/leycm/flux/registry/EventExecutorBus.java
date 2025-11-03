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
package de.leycm.flux.registry;

import de.leycm.flux.event.Event;
import de.leycm.flux.exception.EventProcessException;
import de.leycm.flux.exception.HandlerRegistrationException;
import de.leycm.flux.handler.HandlerList;
import de.leycm.neck.instance.Initializable;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Centralized event dispatching system that manages registration,
 * execution, and lifecycle of event handlers.
 * <p>
 * This interface provides both instance-based and static global access
 * to a default singleton {@link EventExecutorBus} instance.
 * <p>
 * Usage example:
 * <pre>
 * EventExecutorBus bus = new HashEventExecutorBus();
 * EventExecutorBus.registerInstance(bus);
 * bus.register(myHandlerList);
 * bus.fire(myEvent);
 * </pre>
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public interface EventExecutorBus extends Initializable {

    /**
     * Returns the singleton instance of the EventExecutorBus.
     *
     * @return the EventExecutorBus instance
     * @throws NullPointerException if no instance is registered
     * @see Initializable#getInstance(Class)
     */
    @NonNull
    @Contract(pure = true)
    static EventExecutorBus getInstance() {
        return Initializable.getInstance(EventExecutorBus.class);
    }

    // ====== INSTANCE METHODS ======

    /**
     * Dispatches the specified event to all registered handlers that support it.
     *
     * @param event the event instance to fire, must not be {@code null}
     * @throws IllegalArgumentException if {@code event} is {@code null}
     * @throws EventProcessException     if an error occurs during event handling
     */
    void fire(final @NotNull Event event) throws EventProcessException;

    /**
     * Registers all handlers in the given {@link HandlerList}.
     *
     * @param list the handler list to register, must not be {@code null}
     * @throws IllegalArgumentException     if {@code list} is {@code null}
     * @throws HandlerRegistrationException if a handler method fails registration
     */
    void register(final @NotNull HandlerList list) throws HandlerRegistrationException;

    /**
     * Unregisters all handlers in the given {@link HandlerList}.
     *
     * @param list the handler list to unregister, must not be {@code null}
     * @throws IllegalArgumentException if {@code list} is {@code null}
     */
    void unregister(final @NotNull HandlerList list);

    /**
     * Returns the number of registered handlers for the given event type.
     *
     * @param eventType the event class
     * @return number of handlers registered for this event
     */
    int getHandlerCount(final @NotNull Class<? extends Event> eventType);

    /**
     * Checks whether the specified {@link HandlerList} is registered.
     *
     * @param list the handler list
     * @return {@code true} if registered, otherwise {@code false}
     */
    boolean isRegistered(final @NotNull HandlerList list);

    /**
     * Removes all registered handlers.
     */
    void clear();

}