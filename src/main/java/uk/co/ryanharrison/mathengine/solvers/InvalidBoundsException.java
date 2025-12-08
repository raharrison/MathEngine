package uk.co.ryanharrison.mathengine.solvers;

/**
 * Exception thrown when the bounds provided to a bracketing method do not bracket a root.
 * <p>
 * For bracketing methods to work, the function must have opposite signs at the lower and upper bounds.
 * Mathematically, this means f(lower) × f(upper) < 0. When this condition is not met, this exception
 * is thrown.
 * </p>
 *
 * <h2>Common Causes:</h2>
 * <ul>
 *     <li>The interval does not contain a root</li>
 *     <li>The function has the same sign at both bounds</li>
 *     <li>The interval contains an even number of roots (signs cancel out)</li>
 *     <li>The function has a discontinuity between the bounds</li>
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * Function f = new Function("x^2 + 1"); // No real roots
 * BisectionSolver solver = BisectionSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(0.0)
 *     .upperBound(3.0)
 *     .build();
 *
 * // Throws InvalidBoundsException because f(x) > 0 for all real x
 * solver.solve();
 * }</pre>
 *
 * @see SolverException
 */
public class InvalidBoundsException extends SolverException {

    private final double lowerBound;
    private final double upperBound;
    private final double functionAtLower;
    private final double functionAtUpper;

    /**
     * Constructs a new invalid bounds exception with detailed context.
     *
     * @param lowerBound      the lower bound of the interval
     * @param upperBound      the upper bound of the interval
     * @param functionAtLower the function value at the lower bound
     * @param functionAtUpper the function value at the upper bound
     */
    public InvalidBoundsException(double lowerBound, double upperBound,
                                  double functionAtLower, double functionAtUpper) {
        super(String.format(
                "Bounds [%.6g, %.6g] do not bracket a root. " +
                        "f(%.6g) = %.6g and f(%.6g) = %.6g have the same sign. " +
                        "For bracketing methods, f(lower) and f(upper) must have opposite signs.",
                lowerBound, upperBound, lowerBound, functionAtLower, upperBound, functionAtUpper));
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.functionAtLower = functionAtLower;
        this.functionAtUpper = functionAtUpper;
    }

    /**
     * Returns the lower bound of the invalid interval.
     *
     * @return the lower bound
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * Returns the upper bound of the invalid interval.
     *
     * @return the upper bound
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Returns the function value at the lower bound.
     *
     * @return f(lowerBound)
     */
    public double getFunctionAtLower() {
        return functionAtLower;
    }

    /**
     * Returns the function value at the upper bound.
     *
     * @return f(upperBound)
     */
    public double getFunctionAtUpper() {
        return functionAtUpper;
    }
}
