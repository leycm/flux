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
import de.leycm.flux.handler.HandlerList;
import de.leycm.flux.handler.HandlerPriority;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public record ReflectiveHandlerExecutor(String id, HandlerPriority priority,
                                        HandlerList owner, Method method) implements HandlerExecutor {

    public ReflectiveHandlerExecutor {
        Objects.requireNonNull(id, "Parameter id cannot be null");
        Objects.requireNonNull(priority, "Parameter priority cannot be null");
        Objects.requireNonNull(owner, "Parameter owner cannot be null");
        Objects.requireNonNull(method, "Parameter method cannot be null");
    }

    @Override
    public String fire(Event event) {
        try {
            method.invoke(owner, event);
            return id;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to execute handler " + id, e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReflectiveHandlerExecutor other)) return false;
        return id.equals(other.id) && owner == other.owner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }
}