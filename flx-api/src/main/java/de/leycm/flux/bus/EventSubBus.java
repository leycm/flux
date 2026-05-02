/*
 * This file is part of fluxpipe - https://github.com/leycm/fluxpipe.
 * Copyright (C) 2026 Lennard [leycm] <leycm@proton.me>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.leycm.flux.bus;

import de.leycm.flux.EventManager;
import de.leycm.flux.event.EventNode;
import de.leycm.flux.event.EventSubNode;

import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public final class EventSubBus<T> implements EventSubNode<T> {

    // sorted map: class -> handlers
    private final @NonNull Map<Class<? super T>, Set<EventNode<? super T>>> children = new ConcurrentHashMap<>();

    // fast lookup for unregistration
    private final @NonNull Map<EventNode<? super T>, Class<? super T>> index = new ConcurrentHashMap<>();
    private final @NonNull EventSubNode<?> parent;

    @ApiStatus.Internal
    public EventSubBus(final @NonNull EventSubNode<?> parent) {
        this.parent = parent;
    }

    @ApiStatus.Internal
    public @NonNull EventSubNode<?> getParent() {
        return parent;
    }

    @Override
    @ApiStatus.Internal
    public @NonNull EventManager getManager() {
        return parent.getManager();
    }

    @Override
    public void register(
            final @NonNull Class<? super T> clazz,
            final @NonNull EventNode<? super T> node
    ) {
        children.computeIfAbsent(clazz, c -> ConcurrentHashMap.newKeySet());

        // note: checking again because of race conditions
        children.computeIfPresent(clazz, (c, nodes) -> {
            nodes.add(node);
            index.put(node, clazz);
            return nodes;
        });

    }

    @Override
    public void unregister(@NonNull EventNode<? super T> node) {
        Class<? super T> removed = index.remove(node);
        if (removed != null) {
            children.computeIfPresent(removed, (c, nodes) -> {
                nodes.remove(node);
                return nodes;
            });
        }
    }

    @Override
    public void fire(@NotNull T event) {
        // note: the var is a Map.Entry<Class, Set<EventNode>>
        for (final var entry : children.entrySet()) {
            entry.getValue().forEach(node -> {
                node.fire(event);
            });
        }
    }
}
