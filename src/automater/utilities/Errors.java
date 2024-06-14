/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines commonly used errors.
 *
 * Calling the methods will always cause an exception to be thrown.
 *
 * @author Bytevi
 */
public class Errors {

    public static @NotNull Exception unknownError() {
        return new Exception("Unknown error");
    }

    public static @NotNull RuntimeException invalidArgument(@Nullable String description) {
        return new IllegalArgumentException(description != null ? description : "");
    }

    public static @NotNull RuntimeException indexOutOfBounds(@Nullable String description) {
        return new IndexOutOfBoundsException(description != null ? description : "");
    }

    public static @NotNull RuntimeException illegalMathOperation(@Nullable String description) {
        return new ArithmeticException(description != null ? description : "");
    }

    public static @NotNull RuntimeException notImplemented() {
        return new UnsupportedOperationException("Not implemented");
    }

    public static @NotNull RuntimeException illegalStateError() {
        return new IllegalStateException();
    }

    public static @NotNull RuntimeException internalLogicError() {
        return new IllegalStateException("Internal logic");
    }

    public static @NotNull RuntimeException alreadyStarted() {
        return new IllegalStateException("Already started");
    }

    public static @NotNull RuntimeException alreadyStopped() {
        return new IllegalStateException("Already stopped");
    }

    public static @NotNull RuntimeException delegateNotSet() {
        return new IllegalStateException("Delegate not set");
    }

    public static @NotNull RuntimeException serializationFailed() {
        return new RuntimeException("Serialization failed");
    }

    public static @NotNull ClassNotFoundException classNotFound(@NotNull String description) {
        return new ClassNotFoundException(description);
    }
    
    public static @NotNull RuntimeException fileOrDirectoryNotFound() {
        return new RuntimeException("File or directory does not exist");
    }
}
