package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for numerical root-finding algorithms.
 * <p>
 * A <b>root</b> of a function f(x) is a value x₀ such that f(x₀) = 0. Root-finding algorithms
 * approximate these values through iterative numerical methods.
 * </p>
 *
 * <h2>Algorithm Categories:</h2>
 * <p>
 * This package provides two main categories of root-finding methods:
 * </p>
 * <ul>
 *     <li><b>Bracketing Methods</b> ({@link RootBracketingMethod}): Require an interval [a, b]
 *         where f(a) and f(b) have opposite signs. Guaranteed to converge if the function is
 *         continuous.</li>
 *     <li><b>Polishing Methods</b> ({@link RootPolishingMethod}): Refine an initial guess through
 *         iteration. Faster convergence but may diverge if the initial guess is poor.</li>
 * </ul>
 *
 * <h2>Convergence Behavior:</h2>
 * <p>
 * Different algorithms have different convergence properties:
 * </p>
 * <ul>
 *     <li><b>Bisection</b>: Linear convergence, guaranteed for continuous functions</li>
 *     <li><b>Brent's Method</b>: Super-linear convergence, guaranteed for continuous functions</li>
 *     <li><b>Newton-Raphson</b>: Quadratic convergence near root, may diverge</li>
 *     <li><b>Newton-Bisection</b>: Combines reliability of bisection with speed of Newton-Raphson</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Find the square root of 2 (root of x^2 - 2 = 0)
 * Function f = new Function("x^2 - 2");
 *
 * EquationSolver solver = BisectionSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(0.0)
 *     .upperBound(3.0)
 *     .tolerance(1e-6)
 *     .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
 *     .build();
 *
 * double root = solver.solve(); // Approximately 1.414214
 * System.out.println("√2 ≈ " + root);
 * }</pre>
 *
 * <h2>Implementation Notes:</h2>
 * <p>
 * All equation solvers in this package are immutable. Configuration is done through
 * builder patterns, and solvers cannot be modified after construction.
 * </p>
 *
 * @see RootBracketingMethod
 * @see RootPolishingMethod
 * @see BisectionSolver
 * @see BrentSolver
 * @see NewtonRaphsonSolver
 * @see NewtonBisectionSolver
 */
public interface EquationSolver {

    /**
     * Default tolerance for convergence checks.
     * <p>
     * This value represents a balance between accuracy and computational cost for most problems.
     * </p>
     */
    double DEFAULT_TOLERANCE = 1e-5;

    /**
     * Default maximum number of iterations.
     * <p>
     * This prevents infinite loops while allowing sufficient iterations for most problems.
     * </p>
     */
    int DEFAULT_ITERATIONS = 100;

    /**
     * Default convergence criterion.
     * <p>
     * Using tolerance-based convergence ensures accuracy but may require more iterations.
     * </p>
     */
    ConvergenceCriteria DEFAULT_CONVERGENCE_CRITERIA = ConvergenceCriteria.WithinTolerance;

    /**
     * Default number of subdivisions for finding brackets.
     * <p>
     * Used when searching for roots across an interval. More subdivisions increase
     * the likelihood of finding all roots but cost more function evaluations.
     * </p>
     */
    int DEFAULT_SUBDIVISIONS = 100;

    /**
     * Finds a single root of the target function.
     * <p>
     * The specific behavior depends on the algorithm implementation:
     * </p>
     * <ul>
     *     <li>Bracketing methods find a root within the specified interval</li>
     *     <li>Polishing methods refine the initial guess to a nearby root</li>
     * </ul>
     *
     * @return an estimated root of the target function
     * @throws ConvergenceException   if unable to converge within specified criteria
     * @throws DivergenceException    if the algorithm diverges (polishing methods only)
     * @throws InvalidBoundsException if bounds don't bracket a root (bracketing methods only)
     */
    double solve();

    /**
     * Returns the target function whose roots are being found.
     *
     * @return the target function
     */
    Function getTargetFunction();

    /**
     * Returns the tolerance criterion for convergence.
     * <p>
     * The interpretation of tolerance varies by algorithm. See {@link ConvergenceCriteria}
     * for details.
     * </p>
     *
     * @return the convergence tolerance
     */
    double getTolerance();

    /**
     * Returns the maximum number of iterations allowed.
     *
     * @return the maximum iteration count
     */
    int getIterations();

    /**
     * Returns the convergence criterion used by this solver.
     *
     * @return the convergence criterion
     * @see ConvergenceCriteria
     */
    ConvergenceCriteria getConvergenceCriteria();

    /**
     * Finds intervals that may contain roots by subdividing a range and checking for sign changes.
     * <p>
     * This method divides the interval [lower, upper] into {@code subdivisions} sub-intervals
     * and checks each one for a sign change in the function. When f(a) × f(b) < 0 for a
     * sub-interval [a, b], it indicates a root may exist in that interval (by the Intermediate
     * Value Theorem for continuous functions).
     * </p>
     *
     * <h3>Algorithm:</h3>
     * <ol>
     *     <li>Divide [lower, upper] into {@code subdivisions} equal sub-intervals</li>
     *     <li>For each sub-interval [a, b], check if f(a) × f(b) < 0</li>
     *     <li>If so, add the interval to the result list</li>
     * </ol>
     *
     * <h3>Important Notes:</h3>
     * <ul>
     *     <li>More subdivisions increase the likelihood of finding all roots</li>
     *     <li>Even numbers of roots in an interval may be missed (signs cancel)</li>
     *     <li>Roots at exact subdivision points may be missed</li>
     *     <li>Function must be continuous for reliable results</li>
     * </ul>
     *
     * @param function     the function to analyze
     * @param lower        the lower bound of the search range
     * @param upper        the upper bound of the search range
     * @param subdivisions the number of sub-intervals to create
     * @return a list of intervals that may contain roots (empty if none found)
     * @throws IllegalArgumentException if lower >= upper or subdivisions <= 0
     */
    static List<RootInterval> findBrackets(Function function, double lower, double upper, int subdivisions) {
        if (lower >= upper) {
            throw new IllegalArgumentException(
                    String.format("Lower bound must be less than upper bound, got: [%.6g, %.6g]", lower, upper));
        }
        if (subdivisions <= 0) {
            throw new IllegalArgumentException("Subdivisions must be positive, got: " + subdivisions);
        }

        List<RootInterval> brackets = new ArrayList<>(subdivisions);

        // Calculate step size
        double dx = (upper - lower) / subdivisions;
        double x = lower;

        // Evaluate at the lower bound
        double fp = function.evaluateAt(x);

        for (int j = 0; j < subdivisions; j++) {
            x += dx;
            double fc = function.evaluateAt(x);

            // If the subdivision crosses the x-axis, it may contain a root
            if (fc * fp < 0.0) {
                brackets.add(RootInterval.of(x - dx, x));
            }

            // Move to the next interval
            fp = fc;
        }

        return brackets;
    }
}
