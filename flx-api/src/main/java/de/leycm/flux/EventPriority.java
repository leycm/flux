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

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

// order: EARLIEST -> EARLY -> NORMAL -> LATE -> LATEST -> MONITOR.
// note: MONITOR should only read and not write
public enum EventPriority {
    EARLIEST,
    EARLY,
    NORMAL,
    LATE,
    LATEST,
    MONITOR;

    public static final @NonNull EventPriority DEFAULT = NORMAL;

    public static @NonNull EventPriority nonNull(final @Nullable EventPriority weight) {
        if(weight == null) return EventPriority.DEFAULT;
        return weight;
    }

    public boolean isMonitor() {
        return this.equals(MONITOR);
    }
}
