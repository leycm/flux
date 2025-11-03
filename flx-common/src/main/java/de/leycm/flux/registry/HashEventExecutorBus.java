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
import de.leycm.flux.event.Monitorable;
import de.leycm.flux.exception.EventProcessException;
import de.leycm.flux.exception.HandlerRegistrationException;
import de.leycm.flux.exception.NotMonitorableException;
import de.leycm.flux.handler.Handler;
import de.leycm.flux.handler.HandlerList;
import de.leycm.flux.handler.HandlerPriority;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

/**
 * Thread-safe, high-performance event executor bus implementation.
 *
 * <p>This implementation uses optimistic locking and copy-on-write semantics for maximum
 * throughput during event firing while maintaining consistency during registration.
 * Events are processed in priority order: EARLY → NORMAL → LATE → MONITOR.</p>
 *
 * <p><strong>Registration Policy:</strong></p>
 * <ul>
 *   <li>Class-based tracking: Only one instance per HandlerList class can be registered</li>
 *   <li>Duplicate prevention: Attempting to register the same class twice throws exception</li>
 *   <li>Instance equality: unregister() works with any instance of the same class</li>
 * </ul>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public final class HashEventExecutorBus implements EventExecutorBus {

    private static final int INITIAL_CAPACITY = 32;
    private static final HandlerExecutor[] EMPTY_EXECUTORS = new HandlerExecutor[0];

    private final Map<Class<? extends HandlerList>, HandlerList> registeredHandlers;

    private final Map<Class<? extends Event>, HandlerExecutor[]> executorCache;

    private final StampedLock lock;

    public HashEventExecutorBus() {
        this.registeredHandlers = new ConcurrentHashMap<>(INITIAL_CAPACITY);
        this.executorCache = new ConcurrentHashMap<>(INITIAL_CAPACITY);
        this.lock = new StampedLock();
    }

    @Override
    public void fire(final @NonNull Event event) {
        Objects.requireNonNull(event, "Event cannot be null");

        Class<? extends Event> eventType = event.getClass();

        HandlerExecutor[] executors = executorCache.get(eventType);

        if (executors == null) return;

        executeHandlers(executors, event, eventType);
    }

    @Override
    public void register(final @NonNull HandlerList list) {
        Objects.requireNonNull(list, "HandlerList cannot be null");

        Class<? extends HandlerList> listClass = list.getClass();

        if (registeredHandlers.containsKey(listClass)) {
            throw new HandlerRegistrationException(
                    "HandlerList class already registered: " + listClass.getName());
        }

        long stamp = lock.writeLock();
        try {
            if (registeredHandlers.containsKey(listClass)) {
                throw new HandlerRegistrationException(
                        "HandlerList class already registered: " + listClass.getName());
            }

            registerHandlersInternal(list);
            registeredHandlers.put(listClass, list);
        } catch (Exception e) {
            registeredHandlers.remove(listClass);
            throw e;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public void unregister(final @NonNull HandlerList list) {
        Objects.requireNonNull(list, "HandlerList cannot be null");

        Class<? extends HandlerList> listClass = list.getClass();

        HandlerList registered = registeredHandlers.remove(listClass);
        if (registered == null) {
            throw new HandlerRegistrationException(
                    "HandlerList class not registered: " + listClass.getName());
        }

        long stamp = lock.writeLock();
        try {
            unregisterHandlersInternal(registered);
        } finally {
            lock.unlockWrite(stamp);
        }
    }


    public int getEventTypeCount() {
        return executorCache.size();
    }

    @Override
    public int getHandlerCount(final @NonNull Class<? extends Event> eventType) {
        Objects.requireNonNull(eventType, "Event type cannot be null");
        HandlerExecutor[] executors = executorCache.get(eventType);
        return executors != null ? executors.length : 0;
    }

    @Override
    public boolean isRegistered(final @NonNull HandlerList list) {
        Objects.requireNonNull(list, "HandlerList cannot be null");
        return registeredHandlers.containsKey(list.getClass());
    }

    @Override
    public void clear() {
        long stamp = lock.writeLock();
        try {
            executorCache.clear();
            registeredHandlers.clear();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    // ==================== Internal Methods ====================

    private void executeHandlers(
            final @NonNull HandlerExecutor @NonNull [] executors,
            final @NonNull Event event,
            final @NonNull Class<? extends Event> eventType) {

        for (HandlerExecutor executor : executors) {
            try {
                if (executor.priority().isMonitor()) {
                    executeMonitorHandler(executor, event, eventType);
                } else executor.fire(event);
            } catch (Exception e) {
                throw new EventProcessException("Failed to execute handler \"" + executor.id()
                                + "\" for event: " + eventType.getName(), e);
            }
        }
    }

    private void executeMonitorHandler(
            final @NonNull HandlerExecutor executor,
            final @NonNull Event event,
            final @NonNull Class<? extends Event> eventType) {

        if (!(event instanceof Monitorable<?> monitorable)) {
            throw new NotMonitorableException("Event " + eventType.getName()
                            + " is not Monitorable, but has MONITOR handlers");
        }

        Event copy = monitorable.copy();
        executor.fire(copy);
    }

    private void registerHandlersInternal(final @NonNull HandlerList list) {
        Class<?> listClass = list.getClass();
        Method[] methods = listClass.getDeclaredMethods();

        if (methods.length == 0) {
            throw new HandlerRegistrationException("HandlerList has no methods: "
                    + listClass.getName());
        }

        Map<Class<? extends Event>, List<HandlerExecutor>> newHandlers = new HashMap<>();
        int validHandlerCount = 0;

        for (Method method : methods) {
            if (!method.isAnnotationPresent(Handler.class)) continue;

            validateHandlerMethod(list, method);

            @SuppressWarnings("unchecked")
            Class<? extends Event> eventType = (Class<? extends Event>) method.getParameterTypes()[0];
            HandlerPriority priority = method.getAnnotation(Handler.class).priority();
            String handlerId = listClass.getName() + "#" + method.getName();

            HandlerExecutor executor = new ReflectiveHandlerExecutor(handlerId, priority, list, method);

            newHandlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(executor);
            validHandlerCount++;
        }

        if (validHandlerCount == 0)
            throw new HandlerRegistrationException("No valid handlers found in HandlerList: "
                    + listClass.getName());

        updateExecutorCache(newHandlers);
    }

    private void unregisterHandlersInternal(final @NonNull HandlerList list) {
        Map<Class<? extends Event>, HandlerExecutor[]> newCache = new HashMap<>();

        for (Map.Entry<Class<? extends Event>, HandlerExecutor[]> entry : executorCache.entrySet()) {
            HandlerExecutor[] currentExecutors = entry.getValue();
            List<HandlerExecutor> remaining = new ArrayList<>(currentExecutors.length);

            for (HandlerExecutor executor : currentExecutors) {
                if (!(executor instanceof ReflectiveHandlerExecutor rhe) || rhe.owner() != list)
                    remaining.add(executor);
            }

            if (!remaining.isEmpty())
                newCache.put(entry.getKey(), remaining.toArray(EMPTY_EXECUTORS));
        }

        executorCache.clear();
        executorCache.putAll(newCache);
    }

    private void updateExecutorCache(final @NonNull Map<Class<? extends Event>,
            @NonNull List<HandlerExecutor>> newHandlers) {
        for (Map.Entry<Class<? extends Event>, List<HandlerExecutor>> entry : newHandlers.entrySet()) {
            Class<? extends Event> eventType = entry.getKey();
            List<HandlerExecutor> additional = entry.getValue();

            executorCache.compute(eventType, (key, existing) -> {
                List<HandlerExecutor> merged = new ArrayList<>();

                if (existing != null)
                    merged.addAll(Arrays.asList(existing));

                merged.addAll(additional);

                merged.sort(Comparator.comparingInt(e -> e.priority().ordinal()));

                return merged.toArray(EMPTY_EXECUTORS);
            });
        }
    }

    private void validateHandlerMethod(final @NonNull HandlerList list, 
                                       final @NonNull Method method) {
        String methodId = getString(list, method);

        if (Modifier.isStatic(method.getModifiers())) {
            throw new HandlerRegistrationException(
                    "Handler method cannot be static: " + methodId);
        }

        if (!method.canAccess(list)) {
            try {
                method.setAccessible(true);
            } catch (SecurityException e) {
                throw new HandlerRegistrationException(
                        "Cannot access handler method: " + methodId, e);
            }
        }
    }

    private static @NonNull String getString(final @NonNull HandlerList list,
                                             final @NonNull Method method) {
        Class<?> listClass = list.getClass();
        String methodId = listClass.getName() + "#" + method.getName();

        if (method.getParameterCount() != 1)
            throw new HandlerRegistrationException("Handler method must have exactly one parameter: "
                    + methodId);

        Class<?> paramType = method.getParameterTypes()[0];
        if (!Event.class.isAssignableFrom(paramType))
            throw new HandlerRegistrationException("Handler method parameter must extend Event: "
                    + methodId);

        if (method.getReturnType() != void.class)
            throw new HandlerRegistrationException("Handler method must return void: "
                            + methodId);

        return methodId;
    }

}