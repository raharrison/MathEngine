package uk.co.ryanharrison.mathengine.solvers;

/**
 * Defines how convergence is determined in root-finding algorithms.
 * <p>
 * Different convergence criteria are appropriate for different scenarios:
 * </p>
 * <ul>
 *     <li><b>{@link #NumberOfIterations}</b>: Use when you need a result within a fixed
 *         computational budget, regardless of accuracy</li>
 *     <li><b>{@link #WithinTolerance}</b>: Use when you need a result that meets a specific
 *         accuracy requirement, regardless of computation time</li>
 * </ul>
 *
 * <h2>Tolerance Semantics by Algorithm:</h2>
 * The specific meaning of "within tolerance" varies by algorithm:
 * <ul>
 *     <li><b>Bisection:</b> |upper - lower| / 2 < tolerance (half-bracket width)</li>
 *     <li><b>Brent:</b> |upper - lower| < tolerance (bracket width)</li>
 *     <li><b>Newton-Raphson:</b> |f(x)| < tolerance (function value magnitude)</li>
 *     <li><b>Newton-Bisection:</b> |dx| < tolerance (step size magnitude)</li>
 * </ul>
 *
 * @see EquationSolver#getConvergenceCriteria()
 */
public enum ConvergenceCriteria {

    /**
     * Converge after a fixed number of iterations regardless of accuracy.
     * <p>
     * The algorithm will stop after completing the specified number of iterations
     * and return the current estimate, even if the tolerance criterion has not been met.
     * </p>
     * <p>
     * <b>Use when:</b> Computational budget is limited or approximate solutions are acceptable.
     * </p>
     */
    NumberOfIterations,

    /**
     * Converge when the result satisfies the tolerance condition.
     * <p>
     * The algorithm will continue iterating until the tolerance criterion is met,
     * up to the maximum number of iterations. If tolerance is not achieved within
     * the iteration limit, a {@link ConvergenceException} is thrown.
     * </p>
     * <p>
     * <b>Use when:</b> Accuracy is critical and you can afford the computational cost.
     * </p>
     */
    WithinTolerance
}
