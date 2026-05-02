package de.leycm.flux.bus;

import de.leycm.flux.EventManager;
import de.leycm.flux.EventPriority;
import de.leycm.flux.event.EventNode;
import de.leycm.flux.event.EventSubNode;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

public class EventManagerBus implements EventManager {

    // sorted map: priority -> class -> listeners
    private final ConcurrentSkipListMap<Integer, Map<Class<?>, Set<EventNode<? super Object>>>> handlers
            = new ConcurrentSkipListMap<>();

    // fast lookup for unregistration
    private final Map<EventNode<? super Object>, NodeKey> index = new ConcurrentHashMap<>();

    private record NodeKey(Class<?> clazz, int priority) {}

    @Override
    public int resolvePriority(@Nullable EventPriority priority) {
        return EventPriority.nonNull(priority).ordinal() + 1;
    }

    @Override
    public @NonNull <T> Function<Class<T>, EventSubNode<T>> getSubNodeFactory() {
        return clazz -> new EventSubBus<>(this);
    }

    @Override
    public void register(
            @NonNull Class<? super Object> clazz,
            int priority,
            @NonNull EventNode<? super Object> node
    ) {
        Map<Class<?>, Set<EventNode<? super Object>>> classMap =
                handlers.computeIfAbsent(priority, k -> new ConcurrentHashMap<>());
        Set<EventNode<? super Object>> nodes =
                classMap.computeIfAbsent(clazz, c -> ConcurrentHashMap.newKeySet());
        nodes.add(node);
        index.put(node, new NodeKey(clazz, priority));
    }

    @Override
    public @NonNull EventManager getManager() {
        return this;
    }

    @Override
    public void unregister(@NonNull EventNode<? super Object> node) {
        NodeKey key = index.remove(node);
        if (key != null) {
            Map<Class<?>, Set<EventNode<? super Object>>> classMap = handlers.get(key.priority());
            if (classMap != null) {
                Set<EventNode<? super Object>> nodes = classMap.get(key.clazz());
                if (nodes != null) {
                    nodes.remove(node);
                }
            }
        }
    }

    @Override
    public void fire(@NotNull Object event) {
        // Iterate in natural (ascending) priority order – no explicit sorting needed
        for (var classMap : handlers.values()) {
            classMap.forEach((clazz, nodes) -> {
                if (clazz.isAssignableFrom(event.getClass())) {
                    nodes.forEach(node -> node.fire(event));
                }
            });
        }
    }
}