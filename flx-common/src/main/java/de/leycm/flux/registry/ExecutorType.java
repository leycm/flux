package de.leycm.flux.registry;

import de.leycm.flux.handler.Listener;
import de.leycm.flux.handler.HandlerPriority;

import java.lang.reflect.Method;

public enum ExecutorType {
    REFLECTION {
        @Override
        public HandlerExecutor create(String id, HandlerPriority priority, Listener owner, Method method) {
            return new ReflectiveHandlerExecutor(id, priority, owner, method);
        }
    },
    LAMBDA {
        @Override
        public HandlerExecutor create(String id, HandlerPriority priority, Listener owner, Method method) {
            return LambdaHandlerExecutor.create(id, priority, owner, method);
        }
    };

    public abstract HandlerExecutor create(String id, HandlerPriority priority, Listener owner, Method method);
}
