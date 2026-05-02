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
package de.leycm.flux.handler;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

// order: EARLIEST -> EARLY -> NORMAL -> LATE -> LATEST -> MONITOR.
// note: MONITOR should only read and not write
public enum HandlerPriority {
    FIRST    (Integer.MIN_VALUE),
    EARLIEST (Integer.MIN_VALUE + 1),
    EARLY    ((Integer.MIN_VALUE +1) / 2),
    NORMAL   (0),
    LATE     ((Integer.MAX_VALUE -1) / 2),
    LATEST   (Integer.MAX_VALUE -1),
    MONITOR  (Integer.MAX_VALUE);

    public static final @NonNull HandlerPriority DEFAULT = NORMAL;

    public static @NonNull HandlerPriority nonNull(final @Nullable HandlerPriority priority) {
        if(priority == null) return HandlerPriority.DEFAULT;
        return priority;
    }

    public static boolean isMonitor(final @Nullable HandlerPriority priority) {
        return nonNull(priority).priority == Integer.MAX_VALUE;
    }

    private final int priority;

    HandlerPriority(final int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }

    public boolean isMonitor() {
        return this.equals(MONITOR);
    }
}
