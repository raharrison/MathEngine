package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;
import uk.co.ryanharrison.mathengine.differential.symbolic.Differentiator;

/**
 * Immutable implementation of a hybrid {@link RootBracketingMethod} combining Newton-Raphson
 * with bisection fallback.
 * <p>
 * This algorithm attempts to use Newton-Raphson's fast quadratic convergence when possible,
 * but falls back to bisection when the Newton step would leave the bracketed interval.
 * This combines the <b>reliability</b> of bisection (guaranteed convergence for continuous
 * functions) with the <b>speed</b> of Newton-Raphson (quadratic convergence near roots).
 * </p>
 *
 * <h2>Hybrid Strategy:</h2>
 * <p>
 * At each iteration, the algorithm:
 * </p>
 * <ol>
 *     <li><b>Computes Newton-Raphson step:</b> x_new = x - f(x)/f'(x)</li>
 *     <li><b>Validates the step:</b> Checks if x_new is within [lower, upper] bracket</li>
 *     <li><b>Decides action:</b>
 *         <ul>
 *             <li>If x_new is inside bracket → Accept Newton step (fast convergence)</li>
 *             <li>If x_new is outside bracket → Use bisection instead (maintain bracket)</li>
 *         </ul>
 *     </li>
 *     <li><b>Updates bracket:</b> Replaces one bound based on sign of f(x_new)</li>
 * </ol>
 *
 * <h2>Convergence Criterion:</h2>
 * <p>
 * This solver uses <b>step size</b> as the convergence measure:
 * </p>
 * <ul>
 *     <li><b>Tolerance semantics:</b> |dx| < tolerance, where dx is the step size</li>
 *     <li><b>Alternative check:</b> |f(x)| < tolerance (function value near zero)</li>
 * </ul>
 *
 * <h2>Decision Criteria for Switching Methods:</h2>
 * <p>
 * The algorithm switches from Newton to bisection when:
 * </p>
 * <ul>
 *     <li><b>Out-of-bounds:</b> Newton step predicts x_new outside [lower, upper]</li>
 *     <li><b>Rationale:</b> Prevents divergence and maintains bracket invariant</li>
 * </ul>
 * <p>
 * This check is implemented as: <code>(upper - x_new) * (x_new - lower) < 0</code>
 * which evaluates to true when x_new is outside the bracket.
 * </p>
 *
 * <h2>Why More Reliable Than Pure Newton-Raphson:</h2>
 * <ul>
 *     <li><b>Guaranteed convergence:</b> Bisection fallback ensures convergence for continuous functions</li>
 *     <li><b>Bracket preservation:</b> Always maintains an interval bracketing the root</li>
 *     <li><b>Divergence prevention:</b> Cannot jump to distant regions like pure Newton-Raphson</li>
 *     <li><b>Worst-case behavior:</b> Falls back to bisection's linear convergence</li>
 *     <li><b>Best-case behavior:</b> Achieves Newton-Raphson's quadratic convergence</li>
 * </ul>
 *
 * <h2>Derivative Computation:</h2>
 * <p>
 * This solver requires f'(x) and supports three methods:
 * </p>
 * <ul>
 *     <li><b>{@link DifferentiationMethod#Numerical}</b> (default): Uses {@link ExtendedCentralDifferenceMethod}</li>
 *     <li><b>{@link DifferentiationMethod#Symbolic}</b>: Uses {@link Differentiator} for exact derivatives</li>
 *     <li><b>{@link DifferentiationMethod#Predefined}</b>: Uses user-provided derivative function</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 *
 * <h3>Example 1: Default Configuration (Numerical Derivatives)</h3>
 * <pre>{@code
 * // Find root of x^3 - 2x - 5 = 0 near x=2
 * Function f = new Function("x^3 - 2*x - 5");
 *
 * NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(1.0)
 *     .upperBound(3.0)
 *     .tolerance(1e-8)
 *     .build();
 *
 * double root = solver.solve(); // Approximately 2.0946
 * }</pre>
 *
 * <h3>Example 2: With Symbolic Differentiation</h3>
 * <pre>{@code
 * Function f = new Function("cos(x) - x");
 *
 * NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(0.0)
 *     .upperBound(1.0)
 *     .differentiationMethod(DifferentiationMethod.Symbolic)
 *     .tolerance(1e-10)
 *     .build();
 *
 * double root = solver.solve(); // Approximately 0.7390851332
 * }</pre>
 *
 * <h3>Example 3: With Predefined Derivative</h3>
 * <pre>{@code
 * Function f = new Function("x^2 - 4");
 * Function df = new Function("2*x"); // f'(x) = 2x
 *
 * NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
 *     .targetFunction(f)
 *     .derivativeFunction(df)
 *     .lowerBound(0.0)
 *     .upperBound(5.0)
 *     .build();
 *
 * double root = solver.solve(); // Exactly 2.0
 * }</pre>
 *
 * <h2>Performance Comparison:</h2>
 * <ul>
 *     <li><b>vs Bisection:</b> Typically 2-3x faster (fewer iterations)</li>
 *     <li><b>vs Newton-Raphson:</b> Slightly slower but never diverges</li>
 *     <li><b>vs Brent's Method:</b> Similar performance, simpler algorithm</li>
 * </ul>
 *
 * @see RootBracketingMethod
 * @see BisectionSolver
 * @see NewtonRaphsonSolver
 * @see BrentSolver
 */
public final class NewtonBisectionSolver implements RootBracketingMethod {

    private final Function targetFunction;
    private final double lowerBound;
    private final double upperBound;
    private final double tolerance;
    private final int iterations;
    private final ConvergenceCriteria convergenceCriteria;

    private final DifferentiationMethod differentiationMethod;
    private final Function derivativeFunction;

    /**
     * Private constructor. Use {@link #builder()} to create instances.
     *
     * @param targetFunction        the function whose roots are to be found
     * @param lowerBound            the lower bound of the bracket
     * @param upperBound            the upper bound of the bracket
     * @param tolerance             the convergence tolerance
     * @param iterations            the maximum number of iterations
     * @param convergenceCriteria   the convergence criterion
     * @param differentiationMethod the method for computing derivatives
     * @param derivativeFunction    the derivative function (may be null for numerical/symbolic)
     */
    private NewtonBisectionSolver(Function targetFunction,
                                  double lowerBound,
                                  double upperBound,
                                  double tolerance,
                                  int iterations,
                                  ConvergenceCriteria convergenceCriteria,
                                  DifferentiationMethod differentiationMethod,
                                  Function derivativeFunction) {
        this.targetFunction = targetFunction;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.tolerance = tolerance;
        this.iterations = iterations;
        this.convergenceCriteria = convergenceCriteria;
        this.differentiationMethod = differentiationMethod;
        this.derivativeFunction = derivativeFunction;
    }

    /**
     * Creates a new builder for configuring a {@link NewtonBisectionSolver}.
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
     * Returns the method used for computing derivatives.
     *
     * @return the differentiation method
     */
    public DifferentiationMethod getDifferentiationMethod() {
        return differentiationMethod;
    }

    /**
     * Returns the derivative function if one was explicitly provided.
     * <p>
     * This returns the user-provided derivative function for {@link DifferentiationMethod#Predefined},
     * the symbolically-computed derivative for {@link DifferentiationMethod#Symbolic},
     * or null for {@link DifferentiationMethod#Numerical}.
     * </p>
     *
     * @return the derivative function, or null if using numerical differentiation
     */
    public Function getDerivativeFunction() {
        return derivativeFunction;
    }

    /**
     * Finds a root of the target function within the bracketed interval using the hybrid
     * Newton-Bisection algorithm.
     * <p>
     * This method implements the following algorithm:
     * </p>
     * <ol>
     *     <li>Validate that bounds bracket a root (f(lower) × f(upper) < 0)</li>
     *     <li>Initialize with the midpoint of the bracket</li>
     *     <li>For each iteration:
     *         <ul>
     *             <li>Evaluate f(x) and check convergence</li>
     *             <li>Update bracket based on sign of f(x)</li>
     *             <li>Compute Newton step: dx = -f(x)/f'(x)</li>
     *             <li>Apply Newton step: x_new = x + dx</li>
     *             <li>If x_new is outside bracket, use bisection instead</li>
     *             <li>Check step size convergence: |dx| < tolerance</li>
     *         </ul>
     *     </li>
     *     <li>Return converged root or throw exception</li>
     * </ol>
     *
     * @return the estimated root within the bracket
     * @throws InvalidBoundsException if bounds don't bracket a root
     * @throws ConvergenceException   if unable to converge within tolerance after max iterations
     * @throws DivergenceException    if the algorithm encounters infinite values
     */
    @Override
    public double solve() {
        // 1. Validate that the initial bounds bracket a root
        RootBracketingMethod.validateBounds(targetFunction, lowerBound, upperBound);

        // Correct bracketing:
        //   a = left bound
        //   b = right bound
        double a = lowerBound;
        double b = upperBound;

        // Store function values at the bounds
        double fa = targetFunction.evaluateAt(a);
        double fb = targetFunction.evaluateAt(b);

        // 2. Start the iteration from the midpoint of the bracket
        double x = 0.5 * (a + b);

        // Main iteration loop
        for (int iteration = 1; iteration <= iterations; iteration++) {

            double fx = targetFunction.evaluateAt(x);

            // --- Convergence Check: Function Value ---
            // If |f(x)| < tolerance, we consider x sufficiently close to a root.
            if (convergenceCriteria == ConvergenceCriteria.WithinTolerance &&
                    Math.abs(fx) < tolerance) {
                return x;
            }

            // --- 3. Update the bracketing interval ---
            // Maintain the invariant: f(a) and f(b) have opposite signs.
            if (fa * fx < 0.0) {
                // Root is between a and x
                b = x;
                fb = fx;
            } else {
                // Root is between x and b
                a = x;
                fa = fx;
            }

            // --- Divergence Check ---
            if (!Double.isFinite(a) || !Double.isFinite(b)) {
                throw new DivergenceException("Interval bounds became non-finite", iteration, x);
            }

            // --- 4. Attempt Newton Step ---
            // Newton step: dx = -f(x) / f'(x)
            double dfx = evaluateDerivativeAt(x);

            boolean derivativeBad =
                    (dfx == 0.0 || Double.isNaN(dfx) || Double.isInfinite(dfx));

            double newtonStep = derivativeBad ? Double.NaN : -fx / dfx;
            double xNewton = x + newtonStep;

            // Conditions for accepting Newton's method:
            //   • Derivative usable
            //   • Newton step produces a finite result
            //   • New point stays within the bracketing interval
            boolean newtonValid =
                    !derivativeBad &&
                            Double.isFinite(xNewton) &&
                            xNewton >= a && xNewton <= b;

            double dx;

            if (newtonValid) {
                // --- Accept Newton Step ---
                dx = newtonStep;
                x = xNewton;
            } else {
                // --- 5. Newton step rejected → use bisection instead ---
                // Bisection guarantees that the new x stays inside [a, b].
                dx = 0.5 * (b - a);
                x = 0.5 * (a + b);
            }

            // --- 6. Convergence Check: Step Size ---
            // If |dx| < tolerance, we assume we are close enough to the root.
            if (convergenceCriteria == ConvergenceCriteria.WithinTolerance &&
                    Math.abs(dx) < tolerance) {
                return x;
            }
        }

        // If convergence criterion is "return after N iterations", do so
        if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations) {
            return x;
        }

        // Otherwise, report failure to satisfy tolerance within the limit
        throw new ConvergenceException(
                "Unable to converge within specified tolerance",
                iterations,
                x,
                tolerance);
    }

    @Override
    public EquationSolver createSolverForInterval(RootInterval interval) {
        Builder b = builder()
                .targetFunction(targetFunction)
                .lowerBound(interval.getLower())
                .upperBound(interval.getUpper())
                .tolerance(tolerance)
                .iterations(iterations)
                .convergenceCriteria(convergenceCriteria)
                .differentiationMethod(differentiationMethod);

        // Only set derivative function if it's not null (for Predefined/Symbolic methods)
        if (derivativeFunction != null) {
            b.derivativeFunction(derivativeFunction);
        }

        return b.build();
    }

    /**
     * Evaluates the derivative of the target function at the specified point.
     * <p>
     * The evaluation method depends on the configured {@link DifferentiationMethod}:
     * </p>
     * <ul>
     *     <li><b>Numerical:</b> Creates a new {@link ExtendedCentralDifferenceMethod} instance
     *         and computes the derivative numerically (5-point stencil, O(h⁴) accuracy)</li>
     *     <li><b>Symbolic/Predefined:</b> Evaluates the stored derivative function directly</li>
     * </ul>
     *
     * @param x the point at which to evaluate the derivative
     * @return f'(x), the derivative value at x
     */
    private double evaluateDerivativeAt(double x) {
        if (differentiationMethod == DifferentiationMethod.Numerical) {
            ExtendedCentralDifferenceMethod numericalDiff = ExtendedCentralDifferenceMethod.builder()
                    .targetFunction(targetFunction)
                    .targetPoint(x)
                    .build();
            return numericalDiff.deriveFirst();
        } else {
            return derivativeFunction.evaluateAt(x);
        }
    }

    /**
     * Builder for constructing immutable {@link NewtonBisectionSolver} instances.
     * <p>
     * The builder provides sensible defaults for all optional parameters and validates
     * configuration before construction.
     * </p>
     *
     * <h3>Required Parameters:</h3>
     * <ul>
     *     <li>{@link #targetFunction(Function)} - The function to solve</li>
     *     <li>{@link #lowerBound(double)} - Lower bound of the bracket</li>
     *     <li>{@link #upperBound(double)} - Upper bound of the bracket</li>
     * </ul>
     *
     * <h3>Optional Parameters (with defaults):</h3>
     * <ul>
     *     <li>{@link #tolerance(double)} - Default: {@link EquationSolver#DEFAULT_TOLERANCE}</li>
     *     <li>{@link #iterations(int)} - Default: {@link EquationSolver#DEFAULT_ITERATIONS}</li>
     *     <li>{@link #convergenceCriteria(ConvergenceCriteria)} - Default: {@link EquationSolver#DEFAULT_CONVERGENCE_CRITERIA}</li>
     *     <li>{@link #differentiationMethod(DifferentiationMethod)} - Default: {@link DifferentiationMethod#Numerical}</li>
     * </ul>
     */
    public static final class Builder {
        private Function targetFunction;
        private double lowerBound;
        private double upperBound;
        private double tolerance = EquationSolver.DEFAULT_TOLERANCE;
        private int iterations = EquationSolver.DEFAULT_ITERATIONS;
        private ConvergenceCriteria convergenceCriteria = EquationSolver.DEFAULT_CONVERGENCE_CRITERIA;
        private DifferentiationMethod differentiationMethod = DifferentiationMethod.Numerical;
        private Function derivativeFunction;

        private Builder() {
        }

        /**
         * Sets the target function whose roots are to be found.
         *
         * @param targetFunction the target function (required)
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
         * Sets the lower bound of the bracketing interval.
         *
         * @param lowerBound the lower bound (required)
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
         * @param upperBound the upper bound (required)
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
         * For this solver, tolerance represents the maximum acceptable step size: |dx| < tolerance.
         * </p>
         *
         * @param tolerance the tolerance (must be positive)
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
         * @param iterations the maximum iterations (must be positive)
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
         * @param convergenceCriteria the convergence criterion (required)
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
         * Sets the differentiation method.
         * <p>
         * If set to {@link DifferentiationMethod#Symbolic}, the derivative will be computed
         * automatically using symbolic differentiation. If set to {@link DifferentiationMethod#Predefined},
         * you must also call {@link #derivativeFunction(Function)}.
         * </p>
         *
         * @param differentiationMethod the differentiation method (required)
         * @return this builder
         * @throws IllegalArgumentException if differentiationMethod is null
         */
        public Builder differentiationMethod(DifferentiationMethod differentiationMethod) {
            if (differentiationMethod == null) {
                throw new IllegalArgumentException("Differentiation method cannot be null");
            }
            this.differentiationMethod = differentiationMethod;
            return this;
        }

        /**
         * Sets a user-provided derivative function and automatically sets the differentiation
         * method to {@link DifferentiationMethod#Predefined}.
         * <p>
         * The derivative function should represent f'(x) where f(x) is the target function.
         * The user is responsible for ensuring the derivative is correct.
         * </p>
         *
         * @param derivativeFunction the derivative function f'(x) (required for predefined mode)
         * @return this builder
         * @throws IllegalArgumentException if derivativeFunction is null
         */
        public Builder derivativeFunction(Function derivativeFunction) {
            if (derivativeFunction == null) {
                throw new IllegalArgumentException("Derivative function cannot be null");
            }
            this.derivativeFunction = derivativeFunction;
            this.differentiationMethod = DifferentiationMethod.Predefined;
            return this;
        }

        /**
         * Constructs an immutable {@link NewtonBisectionSolver} from the current builder configuration.
         * <p>
         * This method performs final validation:
         * </p>
         * <ul>
         *     <li>Ensures all required fields are set</li>
         *     <li>Validates that lower < upper</li>
         *     <li>For predefined differentiation, ensures derivative function is provided</li>
         *     <li>For symbolic differentiation, computes the derivative automatically</li>
         * </ul>
         *
         * @return a new immutable {@link NewtonBisectionSolver}
         * @throws IllegalArgumentException if required fields are missing or invalid
         */
        public NewtonBisectionSolver build() {
            if (targetFunction == null) {
                throw new IllegalArgumentException("Target function must be specified");
            }
            if (lowerBound >= upperBound) {
                throw new IllegalArgumentException(
                        String.format("Lower bound must be less than upper bound, got: [%.6g, %.6g]",
                                lowerBound, upperBound));
            }

            // Handle derivative function based on differentiation method
            Function finalDerivativeFunction = this.derivativeFunction;

            if (differentiationMethod == DifferentiationMethod.Predefined) {
                if (finalDerivativeFunction == null) {
                    throw new IllegalArgumentException(
                            "Derivative function must be provided for Predefined differentiation method");
                }
            } else if (differentiationMethod == DifferentiationMethod.Symbolic) {
                // Compute symbolic derivative automatically
                finalDerivativeFunction = new Differentiator().differentiate(targetFunction, false);
            }
            // For Numerical, derivativeFunction remains null

            return new NewtonBisectionSolver(
                    targetFunction,
                    lowerBound,
                    upperBound,
                    tolerance,
                    iterations,
                    convergenceCriteria,
                    differentiationMethod,
                    finalDerivativeFunction);
        }
    }

    @Override
    public String toString() {
        return String.format("NewtonBisectionSolver(bounds=[%.6g, %.6g], tolerance=%.2e, " +
                        "iterations=%d, differentiationMethod=%s)",
                lowerBound, upperBound, tolerance, iterations, differentiationMethod);
    }
}
