package de.leycm.flux.exception;

/** Thrown when trying to register a global singleton that already exists. */
public class AlreadyInitializedException extends RuntimeException {
    public AlreadyInitializedException(String message) { super(message); }
}

