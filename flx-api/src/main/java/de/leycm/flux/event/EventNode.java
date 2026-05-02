
package de.leycm.flux.event;

import lombok.NonNull;

public interface EventNode<T> {
    void fire(@NonNull T event);
}
