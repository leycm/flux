package de.leycm.flux;

import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

public interface ListenerReflection {

    @ApiStatus.Internal
    @NonNull EventManager getManager();

}
