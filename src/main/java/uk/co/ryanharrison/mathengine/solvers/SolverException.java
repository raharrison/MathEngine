package uk.co.ryanharrison.mathengine.solvers;

/**
 * Base exception for all root-finding solver errors.
 * <p>
 * This exception is the parent of all exceptions thrown by equation solvers in this package.
 * It extends {@link RuntimeException} to allow solvers to be used in functional contexts
 * without requiring explicit exception handling.
 * </p>
 *
 * @see ConvergenceException
 * @see InvalidBoundsException
 * @see DivergenceException
 */
public class SolverException extends RuntimeException {

    /**
     * Constructs a new solver exception with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public SolverException(String message) {
        super(message);
    }

    /**
     * Constructs a new solver exception with the specified detail message and cause.
     *
     * @param message the detail message explaining the cause of the exception
     * @param cause   the cause of this exception (which is saved for later retrieval by the
     *                {@link #getCause()} method)
     */
    public SolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
