package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

import java.util.Objects;

/**
 * Immutable implementation of the Bisection root-finding algorithm.
 * <p>
 * The bisection method is one of the simplest and most reliable root-finding algorithms.
 * It is a bracketing method that repeatedly bisects an interval and selects the subinterval
 * where the function changes sign, guaranteeing convergence for continuous functions.
 * </p>
 *
 * <h2>Algorithm Description:</h2>
 * <p>
 * Given a continuous function f(x) and an interval [a, b] where f(a) × f(b) < 0:
 * </p>
 * <ol>
 *     <li>Calculate the midpoint: x = (a + b) / 2</li>
 *     <li>Evaluate f(x)</li>
 *     <li>If f(x) = 0 or convergence criteria are met, return x</li>
 *     <li>If f(a) × f(x) < 0, the root is in [a, x]; set b = x</li>
 *     <li>Otherwise, the root is in [x, b]; set a = x</li>
 *     <li>Repeat from step 1</li>
 * </ol>
 *
 * <h2>Convergence Properties:</h2>
 * <ul>
 *     <li><b>Convergence Rate:</b> Linear (one additional bit of accuracy per iteration)</li>
 *     <li><b>Guaranteed Convergence:</b> Always converges for continuous functions</li>
 *     <li><b>Error Bound:</b> After n iterations, error ≤ (b - a) / 2^(n+1)</li>
 *     <li><b>Robustness:</b> Extremely robust, cannot diverge</li>
 * </ul>
 *
 * <h2>Tolerance Semantics:</h2>
 * <p>
 * Convergence is achieved when the half-width of the current bracket is less than tolerance:
 * <br>
 * <b>|upper - lower| / 2 < tolerance</b>
 * </p>
 * <p>
 * This means the true root is guaranteed to be within ±tolerance of the returned value.
 * </p>
 *
 * <h2>Time Complexity:</h2>
 * <ul>
 *     <li><b>Iterations Required:</b> O(log₂((b - a) / tolerance))</li>
 *     <li><b>Function Evaluations:</b> 2 or 3 per iteration (depending on implementation)</li>
 *     <li><b>Total Cost:</b> O(log₂((b - a) / tolerance)) function evaluations</li>
 * </ul>
 *
 * <h2>When to Use:</h2>
 * <ul>
 *     <li>When reliability is more important than speed</li>
 *     <li>When the function is expensive to evaluate (few evaluations needed)</li>
 *     <li>When you have a good bracket but no derivative information</li>
 *     <li>When robustness is critical (e.g., production systems)</li>
 * </ul>
 *
 * <h2>Limitations:</h2>
 * <ul>
 *     <li>Slower than Newton-Raphson or secant methods near the root</li>
 *     <li>Requires a valid bracket (f(a) and f(b) must have opposite signs)</li>
 *     <li>Cannot find roots where the function is tangent to the x-axis</li>
 *     <li>May miss multiple roots in the same bracket</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Find the square root of 2 (root of x^2 - 2 = 0)
 * Function f = new Function("x^2 - 2");
 * BisectionSolver solver = BisectionSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(0.0)
 *     .upperBound(3.0)
 *     .tolerance(1e-8)
 *     .build();
 *
 * double root = solver.solve(); // Returns approximately 1.414213562
 *
 * // Using the convenient factory method
 * BisectionSolver solver2 = BisectionSolver.of(f, 0.0, 3.0);
 * double root2 = solver2.solve();
 *
 * // Find all roots in an interval
 * Function cubic = new Function("x^3 - 6*x^2 + 11*x - 6");
 * BisectionSolver solver3 = BisectionSolver.builder()
 *     .targetFunction(cubic)
 *     .lowerBound(0.0)
 *     .upperBound(4.0)
 *     .build();
 *
 * List<Double> allRoots = solver3.solveAll();
 * // Returns [1.0, 2.0, 3.0] (approximately)
 *
 * // Using NumberOfIterations convergence (stops after N iterations)
 * BisectionSolver solver4 = BisectionSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(0.0)
 *     .upperBound(3.0)
 *     .iterations(20)
 *     .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
 *     .build();
 *
 * double approxRoot = solver4.solve(); // Returns best estimate after 20 iterations
 * }</pre>
 *
 * <h2>Mathematical Background:</h2>
 * <p>
 * The bisection method is based on the <b>Intermediate Value Theorem</b>: If f is continuous
 * on [a, b] and f(a) and f(b) have opposite signs, then there exists at least one c ∈ (a, b)
 * such that f(c) = 0.
 * </p>
 * <p>
 * By repeatedly halving the interval while maintaining the sign-change property, we converge
 * to a root with guaranteed progress at each step.
 * </p>
 *
 * @see RootBracketingMethod
 * @see BrentSolver
 * @see NewtonBisectionSolver
 * @author Ryan Harrison
 */
public final class BisectionSolver implements RootBracketingMethod {

    /**
     * The function whose roots are being found.
     */
    private final Function targetFunction;

    /**
     * The lower bound of the bracketing interval.
     */
    private final double lowerBound;

    /**
     * The upper bound of the bracketing interval.
     */
    private final double upperBound;

    /**
     * The convergence tolerance.
     */
    private final double tolerance;

    /**
     * The maximum number of iterations allowed.
     */
    private final int iterations;

    /**
     * The convergence criterion to use.
     */
    private final ConvergenceCriteria convergenceCriteria;

    /**
     * Private constructor to enforce builder usage.
     * Use {@link #builder()} or {@link #of(Function, double, double)} to create instances.
     *
     * @param builder the builder containing configuration
     */
    private BisectionSolver(Builder builder) {
        this.targetFunction = builder.targetFunction;
        this.lowerBound = builder.lowerBound;
        this.upperBound = builder.upperBound;
        this.tolerance = builder.tolerance;
        this.iterations = builder.iterations;
        this.convergenceCriteria = builder.convergenceCriteria;
    }

    /**
     * Creates a bisection solver with the specified function and bounds.
     * <p>
     * Uses default values for tolerance ({@link EquationSolver#DEFAULT_TOLERANCE}),
     * iterations ({@link EquationSolver#DEFAULT_ITERATIONS}), and convergence criteria
     * ({@link EquationSolver#DEFAULT_CONVERGENCE_CRITERIA}).
     * </p>
     *
     * @param targetFunction the function whose roots are to be found
     * @param lowerBound     the lower bound of the bracketing interval
     * @param upperBound     the upper bound of the bracketing interval
     * @return a new BisectionSolver instance
     * @throws IllegalArgumentException if targetFunction is null
     * @throws IllegalArgumentException if lowerBound >= upperBound
     * @throws IllegalArgumentException if bounds are NaN or infinite
     * @throws InvalidBoundsException if the bounds don't bracket a root
     */
    public static BisectionSolver of(Function targetFunction, double lowerBound, double upperBound) {
        return builder()
                .targetFunction(targetFunction)
                .lowerBound(lowerBound)
                .upperBound(upperBound)
                .build();
    }

    /**
     * Creates a new builder for constructing a {@link BisectionSolver}.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Bisection Root Solver
     * <p>
     * The bisection method is a globally convergent algorithm for continuous
     * functions on an initial interval [a, b] where f(a) and f(b) have opposite signs.
     * <p>
     * At each iteration, the bracket is reduced by evaluating the midpoint:
     * <p>
     *       x = (a + b) / 2
     * <p>
     * and selecting the subinterval that contains the root.
     * <p>
     * Convergence is guaranteed and linear. This implementation provides:
     *   • Stable function evaluations
     *   • Midpoint and interval-size convergence checks
     *   • Early return for exact root hits
     *   • Non-finite value detection
     */
    @Override
    public double solve() {
        // --- 1. Validate the initial bracket ---
        RootBracketingMethod.validateBounds(targetFunction, lowerBound, upperBound);

        double a = lowerBound;
        double b = upperBound;

        // Evaluate function at the ends once — reuse these values
        double fa = targetFunction.evaluateAt(a);
        double fb = targetFunction.evaluateAt(b);

        // Ensure f(a) and f(b) still bracket a root after evaluation
        if (fa == 0.0) return a;
        if (fb == 0.0) return b;

        if (fa * fb > 0.0) {
            throw new IllegalArgumentException(
                    "Function values at interval endpoints do not bracket a root.");
        }

        double x = a; // will be updated in loop

        for (int iteration = 1; iteration <= iterations; iteration++) {

            // --- 2. Compute midpoint ---
            x = 0.5 * (a + b);

            if (!Double.isFinite(x)) {
                throw new DivergenceException(
                        "Midpoint evaluation resulted in a non-finite value",
                        iteration,
                        x);
            }

            // --- 3. Check interval-size convergence ---
            double halfWidth = Math.abs(b - a) * 0.5;
            if (convergenceCriteria == ConvergenceCriteria.WithinTolerance &&
                    halfWidth < tolerance) {
                return x;
            }

            // --- 4. Evaluate function at midpoint ---
            double fx = targetFunction.evaluateAt(x);

            // Exact root found
            if (fx == 0.0) {
                return x;
            }

            // --- 5. Determine subinterval containing the root ---
            if (fa * fx < 0.0) {
                // Root lies in [a, x]
                b = x;
                fb = fx;
            } else {
                // Root lies in [x, b]
                a = x;
                fa = fx;
            }
        }

        // --- 6. Return last midpoint if using fixed-iteration mode ---
        if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations) {
            return x;
        }

        // --- 7. Otherwise fail due to tolerance not achieved ---
        throw new ConvergenceException(
                "Bisection method failed to converge within specified tolerance",
                iterations,
                x,
                tolerance);
    }

    @Override
    public EquationSolver createSolverForInterval(RootInterval interval) {
        return builder()
                .targetFunction(targetFunction)
                .lowerBound(interval.getLower())
                .upperBound(interval.getUpper())
                .tolerance(tolerance)
                .iterations(iterations)
                .convergenceCriteria(convergenceCriteria)
                .build();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BisectionSolver that)) return false;
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
        return String.format(
                "BisectionSolver(function=%s, bounds=[%.6g, %.6g], tolerance=%.2e, iterations=%d, criteria=%s)",
                targetFunction.getEquation(),
                lowerBound,
                upperBound,
                tolerance,
                iterations,
                convergenceCriteria);
    }

    /**
     * Builder for creating {@link BisectionSolver} instances.
     * <p>
     * Provides a fluent API for constructing solvers with named parameters.
     * All parameters except the target function have sensible defaults.
     * </p>
     *
     * <h3>Default Values:</h3>
     * <ul>
     *     <li><b>tolerance:</b> {@value EquationSolver#DEFAULT_TOLERANCE}</li>
     *     <li><b>iterations:</b> {@value EquationSolver#DEFAULT_ITERATIONS}</li>
     *     <li><b>convergenceCriteria:</b> {@link ConvergenceCriteria#WithinTolerance}</li>
     * </ul>
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
         * @param targetFunction the target function, must not be null
         * @return this builder
         * @throws IllegalArgumentException if targetFunction is null
         */
        public Builder targetFunction(Function targetFunction) {
            if (targetFunction == null) {
                throw new IllegalArgumentException("Target function must not be null");
            }
            this.targetFunction = targetFunction;
            return this;
        }

        /**
         * Sets the lower bound of the bracketing interval.
         *
         * @param lowerBound the lower bound, must be finite
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
         * Sets the upper bound of the bracketing interval.
         *
         * @param upperBound the upper bound, must be finite
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
         * For bisection, convergence is achieved when the half-width of the bracket
         * is less than this tolerance: |upper - lower| / 2 < tolerance.
         * </p>
         *
         * @param tolerance the tolerance, must be positive
         * @return this builder
         * @throws IllegalArgumentException if tolerance is not positive
         */
        public Builder tolerance(double tolerance) {
            if (tolerance <= 0.0) {
                throw new IllegalArgumentException("Tolerance must be positive, got: " + tolerance);
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
         * @param convergenceCriteria the convergence criterion, must not be null
         * @return this builder
         * @throws IllegalArgumentException if convergenceCriteria is null
         * @see ConvergenceCriteria
         */
        public Builder convergenceCriteria(ConvergenceCriteria convergenceCriteria) {
            if (convergenceCriteria == null) {
                throw new IllegalArgumentException("Convergence criteria must not be null");
            }
            this.convergenceCriteria = convergenceCriteria;
            return this;
        }

        /**
         * Builds the {@link BisectionSolver} instance.
         *
         * @return a new immutable BisectionSolver
         * @throws IllegalArgumentException if target function was not set
         * @throws IllegalArgumentException if lowerBound >= upperBound
         */
        public BisectionSolver build() {
            if (targetFunction == null) {
                throw new IllegalArgumentException("Target function must be set");
            }
            if (lowerBound >= upperBound) {
                throw new IllegalArgumentException(
                        String.format("Lower bound must be less than upper bound, got: [%.6g, %.6g]",
                                lowerBound, upperBound));
            }
            return new BisectionSolver(this);
        }
    }
}
