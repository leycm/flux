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
