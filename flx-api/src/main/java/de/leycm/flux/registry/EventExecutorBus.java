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
import de.leycm.flux.exception.AlreadyInitializedException;
import de.leycm.flux.exception.EventProcessException;
import de.leycm.flux.exception.HandlerRegistrationException;
import de.leycm.flux.exception.NotInitializedException;
import de.leycm.flux.handler.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
public interface EventExecutorBus {

    // ====== INSTANCE METHODS ======

    /**
     * Dispatches the specified event to all registered handlers that support it.
     *
     * @param event the event instance to fire, must not be {@code null}
     * @throws IllegalArgumentException if {@code event} is {@code null}
     * @throws EventProcessException     if an error occurs during event handling
     */
    void fire(@NotNull Event event) throws EventProcessException;

    /**
     * Registers all handlers in the given {@link HandlerList}.
     *
     * @param list the handler list to register, must not be {@code null}
     * @throws IllegalArgumentException     if {@code list} is {@code null}
     * @throws HandlerRegistrationException if a handler method fails registration
     */
    void register(@NotNull HandlerList list) throws HandlerRegistrationException;

    /**
     * Unregisters all handlers in the given {@link HandlerList}.
     *
     * @param list the handler list to unregister, must not be {@code null}
     * @throws IllegalArgumentException if {@code list} is {@code null}
     */
    void unregister(@NotNull HandlerList list);

    /**
     * Returns the number of registered handlers for the given event type.
     *
     * @param eventType the event class
     * @return number of handlers registered for this event
     */
    int getHandlerCount(@NotNull Class<? extends Event> eventType);

    /**
     * Checks whether the specified {@link HandlerList} is registered.
     *
     * @param list the handler list
     * @return {@code true} if registered, otherwise {@code false}
     */
    boolean isRegistered(@NotNull HandlerList list);

    /**
     * Removes all registered handlers.
     */
    void clear();

    // ====== STATIC GLOBAL SINGLETON ======

    /** Global singleton reference. */
    AtomicReference<EventExecutorBus> INSTANCE = new AtomicReference<>();

    /**
     * Returns the global {@link EventExecutorBus} singleton.
     *
     * @return the current global instance
     * @throws NotInitializedException if no instance is registered
     */
    static @NotNull EventExecutorBus getInstance() throws NotInitializedException {
        EventExecutorBus bus = INSTANCE.get();
        if (bus == null) {
            throw new NotInitializedException("Global EventExecutorBus instance is not initialized.");
        }
        return bus;
    }

    /**
     * Registers a global {@link EventExecutorBus} singleton.
     *
     * @param bus the instance to register, must not be {@code null}
     * @throws IllegalArgumentException    if {@code bus} is {@code null}
     * @throws AlreadyInitializedException if a global instance is already registered
     */
    static void registerInstance(@NotNull EventExecutorBus bus)
            throws IllegalArgumentException, AlreadyInitializedException {
        Objects.requireNonNull(bus, "Cannot register null EventExecutorBus instance.");
        if (!INSTANCE.compareAndSet(null, bus))
            throw new AlreadyInitializedException("Global EventExecutorBus instance is already initialized.");
    }

    /**
     * Unregisters the global {@link EventExecutorBus} singleton.
     *
     * @throws NotInitializedException if no instance is registered
     */
    static void unregisterInstance() throws NotInitializedException {
        if (!INSTANCE.compareAndSet(INSTANCE.get(), null))
            throw new NotInitializedException("Global EventExecutorBus instance is not initialized.");
    }

}