package de.leycm.flux.registry;

import de.leycm.flux.event.Event;
import de.leycm.flux.handler.Listener;
import de.leycm.flux.handler.HandlerPriority;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public record LambdaHandlerExecutor(
        @NonNull String id,
        @NonNull HandlerPriority priority,
        @NonNull Listener owner,
        @NonNull BiConsumer<Object, Event> executor
) implements HandlerExecutor {

    @Contract("_, _, _, _ -> new")
    public static @NotNull LambdaHandlerExecutor create(final @NonNull String id,
                                                        final @NonNull HandlerPriority priority,
                                                        final @NonNull Listener owner,
                                                        final @NonNull Method method
    ) {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodHandle target = lookup.unreflect(method);

            final CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    target,
                    MethodType.methodType(void.class, owner.getClass(), method.getParameterTypes()[0])
            );

            @SuppressWarnings("unchecked")
            final BiConsumer<Object, Event> invoker = (BiConsumer<Object, Event>) site.getTarget().invokeExact();
            return new LambdaHandlerExecutor(id, priority, owner, invoker);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create lambda for " + id, t);
        }
    }

    @Override
    public void fire(@NonNull Event event) {
        executor.accept(owner, event);
    }
}