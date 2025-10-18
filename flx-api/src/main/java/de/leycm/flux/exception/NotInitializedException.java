package de.leycm.flux.exception;

/** Thrown when a global singleton is not initialized but accessed. */
public class NotInitializedException extends RuntimeException {
    public NotInitializedException(String message) { super(message); }
}

