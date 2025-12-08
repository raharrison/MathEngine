package uk.co.ryanharrison.mathengine.solvers;

/**
 * Exception thrown when a root-finding algorithm fails to converge within the specified criteria.
 * <p>
 * This typically occurs when:
 * </p>
 * <ul>
 *     <li>The maximum number of iterations is exceeded without meeting the tolerance requirement</li>
 *     <li>The tolerance criterion cannot be satisfied due to numerical precision limits</li>
 *     <li>The algorithm gets stuck in a cyclic pattern</li>
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * try {
 *     double root = solver.solve();
 * } catch (ConvergenceException e) {
 *     System.err.println("Failed to converge after " + e.getIterations() + " iterations");
 *     System.err.println("Last estimate: " + e.getLastEstimate());
 * }
 * }</pre>
 *
 * @see SolverException
 */
public class ConvergenceException extends SolverException {

    private final int iterations;
    private final double lastEstimate;
    private final double tolerance;

    /**
     * Constructs a new convergence exception with detailed context.
     *
     * @param message      the detail message explaining why convergence failed
     * @param iterations   the number of iterations completed before failure
     * @param lastEstimate the last estimated value before giving up
     * @param tolerance    the tolerance criterion that could not be met
     */
    public ConvergenceException(String message, int iterations, double lastEstimate, double tolerance) {
        super(String.format("%s (iterations: %d, last estimate: %.10g, tolerance: %.2e)",
                message, iterations, lastEstimate, tolerance));
        this.iterations = iterations;
        this.lastEstimate = lastEstimate;
        this.tolerance = tolerance;
    }

    /**
     * Returns the number of iterations completed before convergence failure.
     *
     * @return the iteration count at failure
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Returns the last estimated root value before the algorithm gave up.
     * <p>
     * This value may be close to the actual root even though convergence criteria were not met.
     * </p>
     *
     * @return the last estimated root value
     */
    public double getLastEstimate() {
        return lastEstimate;
    }

    /**
     * Returns the tolerance criterion that could not be satisfied.
     *
     * @return the required tolerance
     */
    public double getTolerance() {
        return tolerance;
    }
}
