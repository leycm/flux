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
package de.leycm.flux;

import de.leycm.flux.event.EventSubNode;
import de.leycm.flux.event.EventNode;
import de.leycm.init4j.instance.Instanceable;

import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface EventManager extends Instanceable, EventSubNode<Object> {

    @ApiStatus.Internal
    // note: use EventPriority#nonNull() to convert to a never 0 int
    int resolvePriority(@Nullable EventPriority  priority);

    @ApiStatus.Internal
    @NonNull <T> Function<Class<T>, EventSubNode<T>> getSubNodeFactory();

    @Override
    default void register(
            final @NonNull Class<? super Object> clazz,
            final @NonNull EventNode<? super Object> node
    ) {
        register(clazz, null, node);
    }

    default void register(
            final @NonNull Class<? super Object> clazz,
            final @Nullable EventPriority priority,
            final @NonNull EventNode<? super Object> node
    ) {
        register(clazz, resolvePriority(priority), node);
    }

    void register(
            final @NonNull Class<? super Object> clazz,
            final int priority,
            final @NonNull EventNode<? super Object> node
    );

}
