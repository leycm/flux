/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a>  <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.flux.handler;

import de.leycm.flux.registry.EventExecutorBus;

/**
 * Marker interface for classes that contain event handler methods.
 * Classes implementing this interface can have their methods annotated with {@link Handler}
 * to automatically register them as event handlers.
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Handler
 * @see EventExecutorBus#register(HandlerList)
 */
public interface HandlerList { }
