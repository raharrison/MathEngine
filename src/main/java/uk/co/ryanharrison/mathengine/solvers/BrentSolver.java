package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.core.Function;

import java.util.Objects;

/**
 * Immutable implementation of a {@link RootBracketingMethod} using Brent's algorithm.
 * <p>
 * Brent's method is a hybrid root-finding algorithm that combines the reliability of
 * bisection with the speed of inverse quadratic interpolation and linear interpolation
 * (secant method). It adaptively selects the best strategy at each iteration to achieve
 * super-linear convergence while maintaining the guaranteed convergence properties of
 * bracketing methods.
 * </p>
 *
 * <h2>Algorithm Description:</h2>
 * <p>
 * At each iteration, Brent's method chooses among three strategies:
 * </p>
 * <ul>
 *     <li><b>Inverse Quadratic Interpolation</b>: When three distinct points are available,
 *         fits a parabola through the points and uses its root. Provides super-linear
 *         convergence (order ~1.839) when close to the root.</li>
 *     <li><b>Linear Interpolation (Secant Method)</b>: When only two distinct points are
 *         available, draws a line between them and uses its x-intercept. Provides
 *         super-linear convergence (order ~1.618, the golden ratio).</li>
 *     <li><b>Bisection</b>: Falls back to bisection when interpolation would be unreliable
 *         (e.g., steps are too large, moving in wrong direction, or approaching numerical
 *         limits). Provides guaranteed linear convergence.</li>
 * </ul>
 *
 * <h2>Tolerance Semantics:</h2>
 * <p>
 * Convergence is achieved when the bracket width satisfies:
 * <br>
 * <b>|upper - lower| < tolerance</b>
 * </p>
 * <p>
 * This differs from other algorithms:
 * </p>
 * <ul>
 *     <li>Bisection uses: |upper - lower| / 2 < tolerance (half-bracket)</li>
 *     <li>Newton-Raphson uses: |f(x)| < tolerance (function value)</li>
 * </ul>
 *
 * <h2>Convergence Properties:</h2>
 * <ul>
 *     <li><b>Guaranteed Convergence</b>: Always converges for continuous functions with
 *         valid brackets (f(a) × f(b) < 0)</li>
 *     <li><b>Super-linear Convergence</b>: Near the root, convergence order is approximately
 *         1.839 (faster than secant's 1.618, slower than Newton's 2.0)</li>
 *     <li><b>Robustness</b>: Automatically falls back to bisection when interpolation fails,
 *         ensuring progress is always made</li>
 *     <li><b>No Derivatives Required</b>: Unlike Newton-Raphson, only function evaluations
 *         are needed</li>
 * </ul>
 *
 * <h2>When to Use Brent's Method:</h2>
 * <ul>
 *     <li><b>Prefer Brent</b> when function evaluations are cheap and you want reliable,
 *         fast convergence without derivatives</li>
 *     <li><b>Prefer Newton-Raphson</b> when derivatives are available and the function is
 *         well-behaved near the root</li>
 *     <li><b>Prefer Bisection</b> when function evaluations are expensive or you need
 *         predictable iteration counts</li>
 * </ul>
 *
 * <h2>Historical Background:</h2>
 * <p>
 * The algorithm was developed by Richard Brent in 1973 and published in:
 * <br>
 * <i>Brent, R. P. (1973). "Algorithms for Minimization without Derivatives",
 * Chapter 4. Prentice-Hall, Englewood Cliffs, NJ. ISBN 0-13-022335-2.</i>
 * </p>
 * <p>
 * It improves upon Dekker's method (1969) by adding safeguards and better selection
 * criteria for choosing between interpolation and bisection.
 * </p>
 *
 * <h2>Complexity Analysis:</h2>
 * <ul>
 *     <li><b>Time Complexity</b>: O(log n) iterations where n is the ratio of initial
 *         bracket width to tolerance. In practice, converges much faster due to
 *         super-linear convergence near the root.</li>
 *     <li><b>Space Complexity</b>: O(1) - uses only a constant number of variables</li>
 *     <li><b>Function Evaluations</b>: Typically 50-70% fewer than bisection, approaching
 *         the performance of Newton-Raphson without requiring derivatives</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Find root of x^3 - 2x - 5 = 0 in [2, 3]
 * Function f = new Function("x^3 - 2*x - 5");
 *
 * // Using builder (recommended)
 * BrentSolver solver = BrentSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(2.0)
 *     .upperBound(3.0)
 *     .tolerance(1e-8)
 *     .build();
 *
 * double root = solver.solve();  // Approximately 2.0946
 *
 * // Find all roots in a larger interval
 * List<Double> roots = solver.solveAll(0.0, 10.0);
 *
 * // Using factory method with defaults
 * BrentSolver defaultSolver = BrentSolver.of(f, 2.0, 3.0);
 * }</pre>
 *
 * <h2>Implementation Notes:</h2>
 * <p>
 * This implementation follows Brent's original algorithm closely, maintaining three
 * points (a, b, c) where:
 * </p>
 * <ul>
 *     <li><b>b</b>: The current best estimate (smallest |f(b)|)</li>
 *     <li><b>a</b>: The other bracket endpoint where f(a) × f(b) < 0</li>
 *     <li><b>c</b>: The previous value of a (used for interpolation)</li>
 * </ul>
 * <p>
 * Variables d and e track step sizes to decide between interpolation and bisection.
 * </p>
 *
 * @author Ryan Harrison
 * @see RootBracketingMethod
 * @see BisectionSolver
 * @see NewtonBisectionSolver
 */
public final class BrentSolver implements RootBracketingMethod {

    private final Function targetFunction;
    private final double lowerBound;
    private final double upperBound;
    private final double tolerance;
    private final int iterations;
    private final ConvergenceCriteria convergenceCriteria;

    /**
     * Private constructor to enforce builder usage.
     *
     * @param targetFunction      the function whose roots are to be found
     * @param lowerBound          the lower bound of the bracket
     * @param upperBound          the upper bound of the bracket
     * @param tolerance           the convergence tolerance
     * @param iterations          the maximum number of iterations
     * @param convergenceCriteria the convergence criterion
     */
    private BrentSolver(Function targetFunction, double lowerBound, double upperBound,
                        double tolerance, int iterations, ConvergenceCriteria convergenceCriteria) {
        this.targetFunction = targetFunction;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.tolerance = tolerance;
        this.iterations = iterations;
        this.convergenceCriteria = convergenceCriteria;
    }

    /**
     * Creates a Brent solver with the specified function and bounds, using default settings.
     * <p>
     * Default settings:
     * </p>
     * <ul>
     *     <li>Tolerance: {@link EquationSolver#DEFAULT_TOLERANCE} (1e-5)</li>
     *     <li>Iterations: {@link EquationSolver#DEFAULT_ITERATIONS} (100)</li>
     *     <li>Convergence: {@link EquationSolver#DEFAULT_CONVERGENCE_CRITERIA} (WithinTolerance)</li>
     * </ul>
     *
     * @param targetFunction the function whose roots are to be found
     * @param lowerBound     the lower bound of the bracket
     * @param upperBound     the upper bound of the bracket
     * @return a new BrentSolver instance
     * @throws IllegalArgumentException if lowerBound >= upperBound
     */
    public static BrentSolver of(Function targetFunction, double lowerBound, double upperBound) {
        return builder()
                .targetFunction(targetFunction)
                .lowerBound(lowerBound)
                .upperBound(upperBound)
                .build();
    }

    /**
     * Creates a new builder for constructing a {@link BrentSolver}.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Function getTargetFunction() {
        return targetFunction;
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public int getIterations() {
        return iterations;
    }

    @Override
    public ConvergenceCriteria getConvergenceCriteria() {
        return convergenceCriteria;
    }

    /**
     * Finds a root of the target function using Brent's algorithm.
     * <p>
     * This method combines bisection, linear interpolation (secant method), and inverse
     * quadratic interpolation to efficiently find a root within the specified bracket.
     * The algorithm adaptively chooses the best strategy at each iteration.
     * </p>
     *
     * <h3>Algorithm Steps:</h3>
     * <ol>
     *     <li>Initialize three points: a (lower bound), b (upper bound), c (copy of a)</li>
     *     <li>Ensure b has the smaller function value magnitude</li>
     *     <li>At each iteration:
     *         <ul>
     *             <li>Try inverse quadratic interpolation if three distinct points available</li>
     *             <li>Fall back to linear interpolation if only two points available</li>
     *             <li>Fall back to bisection if interpolation would be unreliable:
     *                 <ul>
     *                     <li>Step size would be too small</li>
     *                     <li>Step would go outside the bracket</li>
     *                     <li>Step is not decreasing fast enough</li>
     *                 </ul>
     *             </li>
     *             <li>Update the bracket based on sign changes</li>
     *             <li>Keep the endpoint with smaller function value as b</li>
     *         </ul>
     *     </li>
     *     <li>Stop when bracket width < tolerance or iterations exhausted</li>
     * </ol>
     *
     * <h3>Interpolation Details:</h3>
     * <p>
     * <b>Linear Interpolation (Secant Method):</b><br>
     * When only two distinct points (a, b) are available:
     * <br>
     * Fit a line through (a, f(a)) and (b, f(b)), find its x-intercept:
     * <br>
     * x = b - f(b) × (b - a) / (f(b) - f(a))
     * </p>
     * <p>
     * <b>Inverse Quadratic Interpolation:</b><br>
     * When three distinct points (a, b, c) are available:
     * <br>
     * Instead of fitting y = ax² + bx + c (standard quadratic), fit the inverse:
     * <br>
     * x = L(y) where L is a quadratic in y
     * <br>
     * Then evaluate at y = 0 to find the estimated root.
     * <br>
     * This avoids division by zero when f(x) ≈ 0 and provides better numerical stability.
     * </p>
     *
     * <h3>Convergence:</h3>
     * <p>
     * The algorithm converges when:
     * </p>
     * <ul>
     *     <li><b>WithinTolerance</b>: |upper - lower| < tolerance</li>
     *     <li><b>NumberOfIterations</b>: Maximum iterations reached</li>
     * </ul>
     *
     * @return an estimated root of the target function
     * @throws InvalidBoundsException if the bounds don't bracket a root (f(a) × f(b) >= 0)
     * @throws ConvergenceException   if unable to converge within tolerance after maximum iterations
     *                                (only when convergence criteria is WithinTolerance)
     */
    @Override
    public double solve() {
        // Validate that bounds bracket a root
        RootBracketingMethod.validateBounds(targetFunction, lowerBound, upperBound);

        // Initialize bracket endpoints
        // a and b are the current bracket, c is the previous value of a
        double a = lowerBound;
        double b = upperBound;
        double c = a;

        // Function values at the bracket endpoints
        double fa = targetFunction.evaluateAt(a);
        double fb = targetFunction.evaluateAt(b);
        double fc = fa;

        // Step sizes: d is the current step, e is the previous step
        // Used to decide between interpolation and bisection
        double d = 0.0;
        double e = 0.0;

        int iterationCount = 0;

        // Main iteration loop
        while (iterationCount < iterations) {
            iterationCount++;

            // Ensure b is the endpoint with smaller function value magnitude
            // This improves convergence by keeping the best estimate as b
            if (Math.abs(fc) < Math.abs(fb)) {
                // Swap: make b the better estimate
                a = b;
                b = c;
                c = a;
                fa = fb;
                fb = fc;
                fc = fa;
            }

            // Calculate tolerance for this iteration
            // tol is scaled with |b| to handle different magnitudes
            // The factor 2.0 is from Brent's original algorithm
            double tol = 2.0 * tolerance * Math.abs(b) + tolerance;

            // Calculate midpoint of the bracket
            double m = 0.5 * (c - b);

            // Check convergence: bracket is small enough and we haven't found exact root
            if (Math.abs(m) <= tol || fb == 0.0) {
                // Converged successfully
                return b;
            }

            // Force bisection if:
            // 1. Previous step e was too small (< tol), or
            // 2. Previous function value |fa| <= current |fb| (not decreasing fast enough)
            if (Math.abs(e) < tol || Math.abs(fa) <= Math.abs(fb)) {
                // Bisection: set both d and e to the full half-bracket width
                d = e = m;
            } else {
                // Try interpolation
                double s = fb / fa;
                double p, q;

                if (a == c) {
                    // LINEAR INTERPOLATION (Secant Method)
                    // Only two distinct points available: (a, fa) and (b, fb)
                    // Fit a line through these points and find its x-intercept
                    //
                    // Line equation: (y - fb) / (x - b) = (fa - fb) / (a - b)
                    // Set y = 0 and solve for x:
                    // x = b - fb × (b - a) / (fb - fa)
                    //
                    // Rearranged for numerical stability:
                    // s = fb / fa
                    // x = b + (b - a) × s / (1 - s)
                    //
                    // p and q are chosen so that p/q gives the step size
                    p = 2.0 * m * s;
                    q = 1.0 - s;
                } else {
                    // INVERSE QUADRATIC INTERPOLATION
                    // Three distinct points available: (a, fa), (b, fb), (c, fc)
                    // Instead of fitting y = Ax² + Bx + C (which fails near roots),
                    // fit the inverse function x = L(y) where L is quadratic in y
                    //
                    // Using Lagrange interpolation with y as the independent variable:
                    // x(y) = a × L_a(y) + b × L_b(y) + c × L_c(y)
                    // where L_i are Lagrange basis polynomials
                    //
                    // Evaluate at y = 0 to find the estimated root
                    //
                    // The formulas below compute the step size p/q using
                    // ratios q = fa/fc, r = fb/fc, s = fb/fa
                    double q_ratio = fa / fc;  // Renamed to avoid confusion with q below
                    double r = fb / fc;
                    p = s * (2.0 * m * q_ratio * (q_ratio - r) - (b - a) * (r - 1.0));
                    q = (q_ratio - 1.0) * (r - 1.0) * (s - 1.0);
                }

                // Ensure p is positive and q has the sign
                // This simplifies the subsequent comparison
                if (p > 0.0) {
                    q = -q;
                } else {
                    p = -p;
                }

                // Save the old step size
                double oldStep = e;
                e = d;

                // Check if interpolation is acceptable
                // Accept interpolation if:
                // 1. The step is decreasing: p < 0.75 * m * q (within bracket), and
                // 2. The step is smaller than half the previous step: p < 0.5 * oldStep * q
                // These conditions ensure we're making good progress
                if (2.0 * p < 3.0 * m * q - Math.abs(tol * q) && p < Math.abs(0.5 * oldStep * q)) {
                    // Interpolation accepted
                    d = p / q;
                } else {
                    // Interpolation rejected, use bisection
                    d = e = m;
                }
            }

            // Update a to the old value of b
            a = b;
            fa = fb;

            // Calculate the new value of b
            // Ensure we move by at least tol (avoid stagnation)
            if (Math.abs(d) > tol) {
                // Normal step: move by d
                b += d;
            } else {
                // Small step: move by at least tol in the direction of m
                b += (m > 0.0) ? tol : -tol;
            }

            // Evaluate function at the new point
            fb = targetFunction.evaluateAt(b);

            // Update the bracket based on sign changes
            // Ensure the bracket maintains opposite signs at endpoints
            if ((fb > 0.0 && fc > 0.0) || (fb <= 0.0 && fc <= 0.0)) {
                // Same sign: move c to old a
                c = a;
                fc = fa;
                d = e = b - a;
            }

            // Check convergence criteria
            if (Math.abs(b - a) < tolerance && convergenceCriteria == ConvergenceCriteria.WithinTolerance) {
                return b;
            }
        }

        // Maximum iterations reached
        if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations) {
            // Accept the result even though tolerance wasn't met
            return b;
        }

        // Failed to converge within tolerance
        throw new ConvergenceException(
                "Unable to converge to root within specified tolerance",
                iterationCount,
                b,
                tolerance
        );
    }

    @Override
    public EquationSolver createSolverForInterval(RootInterval interval) {
        return builder()
                .targetFunction(this.targetFunction)
                .lowerBound(interval.getLower())
                .upperBound(interval.getUpper())
                .tolerance(this.tolerance)
                .iterations(this.iterations)
                .convergenceCriteria(this.convergenceCriteria)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrentSolver that)) return false;
        return Double.compare(that.lowerBound, lowerBound) == 0 &&
                Double.compare(that.upperBound, upperBound) == 0 &&
                Double.compare(that.tolerance, tolerance) == 0 &&
                iterations == that.iterations &&
                Objects.equals(targetFunction, that.targetFunction) &&
                convergenceCriteria == that.convergenceCriteria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetFunction, lowerBound, upperBound, tolerance, iterations, convergenceCriteria);
    }

    @Override
    public String toString() {
        return String.format("BrentSolver(function=%s, bounds=[%.6g, %.6g], tolerance=%.2e, iterations=%d, criteria=%s)",
                targetFunction.getEquation(), lowerBound, upperBound, tolerance, iterations, convergenceCriteria);
    }

    /**
     * Builder for creating {@link BrentSolver} instances.
     * <p>
     * Provides a fluent API for constructing solvers with named parameters.
     * Includes validation and sensible defaults for optional parameters.
     * </p>
     */
    public static final class Builder {
        private Function targetFunction;
        private double lowerBound;
        private double upperBound;
        private double tolerance = EquationSolver.DEFAULT_TOLERANCE;
        private int iterations = EquationSolver.DEFAULT_ITERATIONS;
        private ConvergenceCriteria convergenceCriteria = EquationSolver.DEFAULT_CONVERGENCE_CRITERIA;

        private Builder() {
        }

        /**
         * Sets the target function whose roots are to be found.
         *
         * @param targetFunction the target function
         * @return this builder
         * @throws IllegalArgumentException if targetFunction is null
         */
        public Builder targetFunction(Function targetFunction) {
            if (targetFunction == null) {
                throw new IllegalArgumentException("Target function cannot be null");
            }
            this.targetFunction = targetFunction;
            return this;
        }

        /**
         * Sets the lower bound of the bracket.
         *
         * @param lowerBound the lower bound
         * @return this builder
         * @throws IllegalArgumentException if lowerBound is NaN or infinite
         */
        public Builder lowerBound(double lowerBound) {
            if (Double.isNaN(lowerBound) || Double.isInfinite(lowerBound)) {
                throw new IllegalArgumentException("Lower bound must be finite, got: " + lowerBound);
            }
            this.lowerBound = lowerBound;
            return this;
        }

        /**
         * Sets the upper bound of the bracket.
         *
         * @param upperBound the upper bound
         * @return this builder
         * @throws IllegalArgumentException if upperBound is NaN or infinite
         */
        public Builder upperBound(double upperBound) {
            if (Double.isNaN(upperBound) || Double.isInfinite(upperBound)) {
                throw new IllegalArgumentException("Upper bound must be finite, got: " + upperBound);
            }
            this.upperBound = upperBound;
            return this;
        }

        /**
         * Sets the convergence tolerance.
         * <p>
         * For Brent's method, convergence is achieved when the bracket width
         * satisfies: |upper - lower| < tolerance
         * </p>
         *
         * @param tolerance the convergence tolerance, must be positive
         * @return this builder
         * @throws IllegalArgumentException if tolerance is not positive
         */
        public Builder tolerance(double tolerance) {
            if (tolerance <= 0.0 || Double.isNaN(tolerance) || Double.isInfinite(tolerance)) {
                throw new IllegalArgumentException("Tolerance must be positive and finite, got: " + tolerance);
            }
            this.tolerance = tolerance;
            return this;
        }

        /**
         * Sets the maximum number of iterations.
         *
         * @param iterations the maximum number of iterations, must be positive
         * @return this builder
         * @throws IllegalArgumentException if iterations is not positive
         */
        public Builder iterations(int iterations) {
            if (iterations <= 0) {
                throw new IllegalArgumentException("Iterations must be positive, got: " + iterations);
            }
            this.iterations = iterations;
            return this;
        }

        /**
         * Sets the convergence criterion.
         *
         * @param convergenceCriteria the convergence criterion
         * @return this builder
         * @throws IllegalArgumentException if convergenceCriteria is null
         */
        public Builder convergenceCriteria(ConvergenceCriteria convergenceCriteria) {
            if (convergenceCriteria == null) {
                throw new IllegalArgumentException("Convergence criteria cannot be null");
            }
            this.convergenceCriteria = convergenceCriteria;
            return this;
        }

        /**
         * Builds the {@link BrentSolver} instance.
         *
         * @return a new immutable BrentSolver
         * @throws IllegalArgumentException if targetFunction is null
         * @throws IllegalArgumentException if lowerBound >= upperBound
         */
        public BrentSolver build() {
            if (targetFunction == null) {
                throw new IllegalArgumentException("Target function must be specified");
            }
            if (lowerBound >= upperBound) {
                throw new IllegalArgumentException(
                        String.format("Lower bound must be less than upper bound, got: [%.6g, %.6g]",
                                lowerBound, upperBound));
            }

            return new BrentSolver(targetFunction, lowerBound, upperBound,
                    tolerance, iterations, convergenceCriteria);
        }
    }
}
