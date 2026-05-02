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
