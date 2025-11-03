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
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public record ReflectiveHandlerExecutor(@NonNull String id,
                                        @NonNull HandlerPriority priority,
                                        @NonNull HandlerList owner,
                                        @NonNull Method method)
        implements HandlerExecutor {

    @Override
    public void fire(final @NonNull Event event) {
        try {
            method.invoke(owner, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to execute handler " + id, e);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof ReflectiveHandlerExecutor other)) return false;
        return id.equals(other.id) && owner == other.owner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }

}