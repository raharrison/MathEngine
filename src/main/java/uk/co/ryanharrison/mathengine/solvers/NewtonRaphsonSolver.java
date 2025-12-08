package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;
import uk.co.ryanharrison.mathengine.differential.symbolic.Differentiator;

import java.util.Objects;

/**
 * Immutable implementation of the Newton-Raphson {@link RootPolishingMethod}.
 * <p>
 * The Newton-Raphson method is a powerful root-finding algorithm that uses the derivative
 * of a function to iteratively refine an initial guess. It exhibits quadratic convergence
 * near the root, meaning the number of correct digits approximately doubles with each iteration.
 * </p>
 *
 * <h2>Algorithm:</h2>
 * <p>
 * Starting from an initial guess x₀, the method iteratively applies the Newton-Raphson formula:
 * </p>
 * <pre>
 * x_{n+1} = x_n - f(x_n) / f'(x_n)
 * </pre>
 * <p>
 * This formula finds the x-intercept of the tangent line at (x_n, f(x_n)) and uses it as the
 * next approximation. Geometrically, it "follows the tangent line down to the x-axis."
 * </p>
 *
 * <h2>Convergence Properties:</h2>
 * <ul>
 *     <li><b>Quadratic convergence</b>: Near the root, the number of correct digits approximately
 *         doubles with each iteration</li>
 *     <li><b>Fast convergence</b>: Typically requires far fewer iterations than bracketing methods</li>
 *     <li><b>Local convergence</b>: Convergence is guaranteed only when the initial guess is
 *         sufficiently close to the root</li>
 *     <li><b>May diverge</b>: Poor initial guesses can cause the algorithm to diverge to infinity
 *         or enter cycles</li>
 * </ul>
 *
 * <h2>Tolerance Semantics:</h2>
 * <p>
 * When using {@link ConvergenceCriteria#WithinTolerance}, the algorithm stops when:
 * </p>
 * <pre>
 * |f(x)| < tolerance
 * </pre>
 * <p>
 * This measures how close the function value is to zero, not the distance to the true root.
 * A smaller tolerance yields more accurate roots but may require more iterations.
 * </p>
 *
 * <h2>Derivative Computation Strategies:</h2>
 * <p>
 * The method requires f'(x) at each iteration. This implementation supports three strategies:
 * </p>
 * <ul>
 *     <li><b>{@link DifferentiationMethod#Numerical}</b>: Uses {@link ExtendedCentralDifferenceMethod}
 *         with O(h⁴) accuracy. Default option, works for any differentiable function.</li>
 *     <li><b>{@link DifferentiationMethod#Symbolic}</b>: Uses {@link Differentiator} to compute
 *         exact derivatives from the expression tree. Faster and more accurate, but requires
 *         the function to have a symbolic representation.</li>
 *     <li><b>{@link DifferentiationMethod#Predefined}</b>: Uses a user-provided derivative function.
 *         Fastest option, gives full control over derivative implementation.</li>
 * </ul>
 *
 * <h2>When to Use:</h2>
 * <ul>
 *     <li>You have a good initial guess (from domain knowledge or previous computation)</li>
 *     <li>The function is smooth and differentiable near the root</li>
 *     <li>The derivative is non-zero near the root</li>
 *     <li>You need high precision quickly</li>
 *     <li>You can afford the risk of divergence</li>
 * </ul>
 *
 * <h2>When NOT to Use:</h2>
 * <ul>
 *     <li>The function has multiple roots and you need all of them (use {@link RootBracketingMethod})</li>
 *     <li>The derivative is zero or undefined near the root</li>
 *     <li>The initial guess is far from the root</li>
 *     <li>You need guaranteed convergence (use {@link BisectionSolver} or {@link BrentSolver})</li>
 *     <li>The function has discontinuities or sharp corners</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <h3>Example 1: Numerical Differentiation (Default)</h3>
 * <pre>{@code
 * // Find the square root of 10 (root of x^2 - 10 = 0)
 * Function f = new Function("x^2 - 10");
 *
 * NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
 *     .targetFunction(f)
 *     .initialGuess(3.0)  // Close to √10 ≈ 3.162
 *     .tolerance(1e-10)
 *     .build();
 *
 * double root = solver.solve();  // Converges to 3.162277660168379 in ~4 iterations
 * System.out.println("√10 ≈ " + root);
 * }</pre>
 *
 * <h3>Example 2: Symbolic Differentiation</h3>
 * <pre>{@code
 * // Solve cos(x) = x (root of cos(x) - x = 0)
 * Function f = new Function("cos(x) - x");
 *
 * NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
 *     .targetFunction(f)
 *     .differentiationMethod(DifferentiationMethod.Symbolic)
 *     .initialGuess(1.0)
 *     .tolerance(1e-10)
 *     .build();
 *
 * double root = solver.solve();  // Converges to 0.739085... in ~4 iterations
 * // Derivative computed symbolically: d/dx[cos(x) - x] = -sin(x) - 1
 * }</pre>
 *
 * <h3>Example 3: Predefined Derivative</h3>
 * <pre>{@code
 * // Solve x^3 - 2x + 1 = 0 with known derivative 3x^2 - 2
 * Function f = new Function("x^3 - 2*x + 1");
 * Function df = new Function("3*x^2 - 2");
 *
 * NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
 *     .targetFunction(f)
 *     .derivativeFunction(df)  // Automatically sets method to Predefined
 *     .initialGuess(1.5)
 *     .tolerance(1e-10)
 *     .build();
 *
 * double root = solver.solve();  // Fastest convergence with exact derivative
 * }</pre>
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *     <li><b>Numerical differentiation</b>: Each iteration requires 5 function evaluations
 *         (for O(h⁴) accuracy)</li>
 *     <li><b>Symbolic differentiation</b>: Each iteration requires 2 function evaluations
 *         (target and derivative)</li>
 *     <li><b>Predefined derivative</b>: Each iteration requires 2 function evaluations
 *         (target and derivative)</li>
 * </ul>
 *
 * @see RootPolishingMethod
 * @see NewtonBisectionSolver
 * @see DifferentiationMethod
 */
public final class NewtonRaphsonSolver implements RootPolishingMethod {

    private final Function targetFunction;
    private final Function derivativeFunction;
    private final DifferentiationMethod differentiationMethod;
    private final double tolerance;
    private final int iterations;
    private final ConvergenceCriteria convergenceCriteria;
    private final double initialGuess;

    // Pre-created numerical differentiator (reused with different target points for performance)
    private final ExtendedCentralDifferenceMethod numericalDifferentiator;

    /**
     * Private constructor - use builder() or static factory methods.
     *
     * @param targetFunction        the function to find roots of
     * @param derivativeFunction    the derivative function (may be null for numerical method)
     * @param differentiationMethod the method for computing derivatives
     * @param tolerance             the convergence tolerance
     * @param iterations            the maximum number of iterations
     * @param convergenceCriteria   the convergence criterion
     * @param initialGuess          the initial guess for the root
     */
    private NewtonRaphsonSolver(
            Function targetFunction,
            Function derivativeFunction,
            DifferentiationMethod differentiationMethod,
            double tolerance,
            int iterations,
            ConvergenceCriteria convergenceCriteria,
            double initialGuess) {

        this.targetFunction = Objects.requireNonNull(targetFunction, "Target function cannot be null");
        this.derivativeFunction = derivativeFunction;
        this.differentiationMethod = differentiationMethod;
        this.tolerance = tolerance;
        this.iterations = iterations;
        this.convergenceCriteria = convergenceCriteria;
        this.initialGuess = initialGuess;

        if (tolerance <= 0.0) {
            throw new IllegalArgumentException("Tolerance must be positive, got: " + tolerance);
        }
        if (iterations <= 0) {
            throw new IllegalArgumentException("Iterations must be positive, got: " + iterations);
        }
        if (!Double.isFinite(initialGuess)) {
            throw new IllegalArgumentException("Initial guess must be finite, got: " + initialGuess);
        }

        // Pre-create numerical differentiator for performance (avoid creating new instance per iteration)
        if (differentiationMethod == DifferentiationMethod.Numerical) {
            this.numericalDifferentiator = ExtendedCentralDifferenceMethod.of(targetFunction);
        } else {
            this.numericalDifferentiator = null;
        }
    }

    /**
     * Creates a new NewtonRaphsonSolver with the specified function using numerical differentiation.
     * <p>
     * Uses default values: tolerance = {@value EquationSolver#DEFAULT_TOLERANCE},
     * iterations = {@value EquationSolver#DEFAULT_ITERATIONS},
     * convergence criteria = {@link ConvergenceCriteria#WithinTolerance},
     * initial guess = {@value RootPolishingMethod#DEFAULT_INITIAL_GUESS}.
     * </p>
     *
     * @param targetFunction the function to find roots of
     * @return a new NewtonRaphsonSolver instance
     * @throws NullPointerException if targetFunction is null
     */
    public static NewtonRaphsonSolver of(Function targetFunction) {
        return builder()
                .targetFunction(targetFunction)
                .build();
    }

    /**
     * Creates a new NewtonRaphsonSolver with the specified function and initial guess.
     * <p>
     * Uses numerical differentiation and default values for other parameters.
     * </p>
     *
     * @param targetFunction the function to find roots of
     * @param initialGuess   the initial guess for the root
     * @return a new NewtonRaphsonSolver instance
     * @throws NullPointerException     if targetFunction is null
     * @throws IllegalArgumentException if initialGuess is not finite
     */
    public static NewtonRaphsonSolver of(Function targetFunction, double initialGuess) {
        return builder()
                .targetFunction(targetFunction)
                .initialGuess(initialGuess)
                .build();
    }

    /**
     * Creates a new NewtonRaphsonSolver with the specified function and derivative.
     * <p>
     * Uses predefined differentiation method and default values for other parameters.
     * </p>
     *
     * @param targetFunction     the function to find roots of
     * @param derivativeFunction the derivative of the target function
     * @param initialGuess       the initial guess for the root
     * @return a new NewtonRaphsonSolver instance
     * @throws NullPointerException     if targetFunction or derivativeFunction is null
     * @throws IllegalArgumentException if initialGuess is not finite
     */
    public static NewtonRaphsonSolver of(Function targetFunction, Function derivativeFunction, double initialGuess) {
        return builder()
                .targetFunction(targetFunction)
                .derivativeFunction(derivativeFunction)
                .initialGuess(initialGuess)
                .build();
    }

    /**
     * Creates a new builder for constructing NewtonRaphsonSolver instances.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Evaluates the derivative of the target function at the specified point using
     * the configured differentiation method.
     *
     * @param x the point at which to evaluate the derivative
     * @return the value of f'(x)
     */
    private double evaluateDerivativeAt(double x) {
        return switch (differentiationMethod) {
            case Numerical ->
                // Use pre-created differentiator with updated target point for performance
                    numericalDifferentiator.withTargetPoint(x).deriveFirst();
            case Symbolic, Predefined -> derivativeFunction.evaluateAt(x);
        };
    }

    /**
     * Newton–Raphson Root Solver
     * <p>
     * This method implements the classic Newton iteration:
     * <p>
     * x_{n+1} = x_n - f(x_n) / f'(x_n)
     * <p>
     * Newton-Raphson typically converges quadratically near a simple root,
     * but it can diverge when the derivative is small, zero, or rapidly changing.
     * <p>
     * This implementation adds robust safety checks:
     * • Non-finite x detection (±∞, NaN)
     * • Safe handling of zero or near-zero derivative
     * • Step-size based convergence: |dx| < tolerance
     * • Function-value based convergence: |f(x)| < tolerance
     * <p>
     * This solver does NOT maintain a bracketing interval and therefore
     * convergence is not guaranteed on arbitrary inputs.
     */
    @Override
    public double solve() {
        double x = initialGuess;

        for (int iteration = 1; iteration <= iterations; iteration++) {

            // --- Divergence Check ---
            if (!Double.isFinite(x)) {
                throw new DivergenceException(
                        "Iteration produced a non-finite value (NaN or Infinity)",
                        iteration,
                        x);
            }

            // --- Evaluate function and derivative ---
            double fx = targetFunction.evaluateAt(x);
            double dfx = evaluateDerivativeAt(x);

            // --- Zero or Bad Derivative Check ---
            // Newton's method cannot proceed when derivative is zero or invalid
            if (dfx == 0.0 || Double.isNaN(dfx) || Double.isInfinite(dfx)) {
                throw new DivergenceException(
                        "Derivative is zero or undefined at x = " + x +
                                " — Newton step cannot continue",
                        iteration,
                        x);
            }

            // --- Compute Newton Step ---
            double dx = -fx / dfx;
            double xNew = x + dx;

            // --- Step Convergence Check ---
            // If the step size is sufficiently small, assume convergence.
            if (convergenceCriteria == ConvergenceCriteria.WithinTolerance &&
                    Math.abs(dx) < tolerance) {
                return xNew;
            }

            // --- Function Convergence Check ---
            // Note: we use fx from old x; this is intentional and standard.
            if (convergenceCriteria == ConvergenceCriteria.WithinTolerance &&
                    Math.abs(fx) < tolerance) {
                return x;
            }

            // --- Divergence Detection: non-finite new x ---
            if (!Double.isFinite(xNew)) {
                throw new DivergenceException(
                        "Newton step resulted in non-finite value",
                        iteration,
                        xNew);
            }

            // Commit step
            x = xNew;
        }

        // --- Return after fixed number of iterations ---
        if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations) {
            return x;
        }

        // --- Convergence Failure ---
        throw new ConvergenceException(
                "Unable to converge to a root within the specified tolerance",
                iterations,
                x,
                tolerance);
    }

    @Override
    public EquationSolver createSolverForGuess(double initialGuess) {
        return new NewtonRaphsonSolver(
                targetFunction,
                derivativeFunction,
                differentiationMethod,
                tolerance,
                iterations,
                convergenceCriteria,
                initialGuess);
    }

    @Override
    public Function getTargetFunction() {
        return targetFunction;
    }

    /**
     * Returns the derivative function.
     * <p>
     * This will be null when using {@link DifferentiationMethod#Numerical}.
     * </p>
     *
     * @return the derivative function, or null if using numerical differentiation
     */
    public Function getDerivativeFunction() {
        return derivativeFunction;
    }

    /**
     * Returns the differentiation method used to compute derivatives.
     *
     * @return the differentiation method
     */
    public DifferentiationMethod getDifferentiationMethod() {
        return differentiationMethod;
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
    public double getInitialGuess() {
        return initialGuess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewtonRaphsonSolver that)) return false;
        return Double.compare(that.tolerance, tolerance) == 0 &&
                iterations == that.iterations &&
                Double.compare(that.initialGuess, initialGuess) == 0 &&
                Objects.equals(targetFunction, that.targetFunction) &&
                Objects.equals(derivativeFunction, that.derivativeFunction) &&
                differentiationMethod == that.differentiationMethod &&
                convergenceCriteria == that.convergenceCriteria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetFunction, derivativeFunction, differentiationMethod,
                tolerance, iterations, convergenceCriteria, initialGuess);
    }

    @Override
    public String toString() {
        return String.format("NewtonRaphsonSolver(method=%s, x0=%.4f, tolerance=%.2e, iterations=%d)",
                differentiationMethod, initialGuess, tolerance, iterations);
    }

    /**
     * Builder for constructing {@link NewtonRaphsonSolver} instances with custom configuration.
     * <p>
     * All parameters have sensible defaults and can be configured independently.
     * </p>
     */
    public static final class Builder {
        private Function targetFunction;
        private Function derivativeFunction;
        private DifferentiationMethod differentiationMethod = DifferentiationMethod.Numerical;
        private double tolerance = EquationSolver.DEFAULT_TOLERANCE;
        private int iterations = EquationSolver.DEFAULT_ITERATIONS;
        private ConvergenceCriteria convergenceCriteria = EquationSolver.DEFAULT_CONVERGENCE_CRITERIA;
        private double initialGuess = RootPolishingMethod.DEFAULT_INITIAL_GUESS;

        private Builder() {
        }

        /**
         * Sets the target function whose roots will be found.
         *
         * @param targetFunction the function to find roots of
         * @return this builder
         * @throws NullPointerException if targetFunction is null
         */
        public Builder targetFunction(Function targetFunction) {
            this.targetFunction = Objects.requireNonNull(targetFunction, "Target function cannot be null");
            return this;
        }

        /**
         * Sets the derivative function and changes differentiation method to {@link DifferentiationMethod#Predefined}.
         * <p>
         * Use this when you have the exact derivative available for maximum performance.
         * </p>
         *
         * @param derivativeFunction the derivative of the target function
         * @return this builder
         * @throws NullPointerException if derivativeFunction is null
         */
        public Builder derivativeFunction(Function derivativeFunction) {
            this.derivativeFunction = Objects.requireNonNull(derivativeFunction, "Derivative function cannot be null");
            this.differentiationMethod = DifferentiationMethod.Predefined;
            return this;
        }

        /**
         * Sets the differentiation method for computing derivatives.
         * <p>
         * Default: {@link DifferentiationMethod#Numerical}
         * </p>
         * <p>
         * Note: If you call {@link #derivativeFunction(Function)}, the method is automatically
         * set to {@link DifferentiationMethod#Predefined}.
         * </p>
         *
         * @param differentiationMethod the differentiation method
         * @return this builder
         * @throws NullPointerException if differentiationMethod is null
         */
        public Builder differentiationMethod(DifferentiationMethod differentiationMethod) {
            this.differentiationMethod = Objects.requireNonNull(differentiationMethod, "Differentiation method cannot be null");
            return this;
        }

        /**
         * Sets the convergence tolerance.
         * <p>
         * Default: {@value EquationSolver#DEFAULT_TOLERANCE}
         * </p>
         * <p>
         * When using {@link ConvergenceCriteria#WithinTolerance}, the algorithm stops when |f(x)| < tolerance.
         * </p>
         *
         * @param tolerance the convergence tolerance (must be positive)
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
         * <p>
         * Default: {@value EquationSolver#DEFAULT_ITERATIONS}
         * </p>
         *
         * @param iterations the maximum number of iterations (must be positive)
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
         * <p>
         * Default: {@link ConvergenceCriteria#WithinTolerance}
         * </p>
         *
         * @param convergenceCriteria the convergence criterion
         * @return this builder
         * @throws NullPointerException if convergenceCriteria is null
         */
        public Builder convergenceCriteria(ConvergenceCriteria convergenceCriteria) {
            this.convergenceCriteria = Objects.requireNonNull(convergenceCriteria, "Convergence criteria cannot be null");
            return this;
        }

        /**
         * Sets the initial guess for the root.
         * <p>
         * Default: {@value RootPolishingMethod#DEFAULT_INITIAL_GUESS}
         * </p>
         * <p>
         * The quality of the initial guess significantly affects convergence. A good initial
         * guess close to the actual root ensures fast convergence, while a poor guess may
         * cause divergence.
         * </p>
         *
         * @param initialGuess the initial guess (must be finite)
         * @return this builder
         * @throws IllegalArgumentException if initialGuess is not finite
         */
        public Builder initialGuess(double initialGuess) {
            if (!Double.isFinite(initialGuess)) {
                throw new IllegalArgumentException("Initial guess must be finite, got: " + initialGuess);
            }
            this.initialGuess = initialGuess;
            return this;
        }

        /**
         * Builds a new NewtonRaphsonSolver instance.
         * <p>
         * Validates that the configuration is consistent:
         * </p>
         * <ul>
         *     <li>Target function must be set</li>
         *     <li>If using {@link DifferentiationMethod#Predefined}, derivative function must be set</li>
         *     <li>If using {@link DifferentiationMethod#Symbolic}, derivative will be computed automatically</li>
         * </ul>
         *
         * @return a new NewtonRaphsonSolver
         * @throws NullPointerException     if targetFunction is not set
         * @throws IllegalArgumentException if using Predefined method without derivative function
         */
        public NewtonRaphsonSolver build() {
            Objects.requireNonNull(targetFunction, "Target function must be set");

            // Validate differentiation method configuration
            if (differentiationMethod == DifferentiationMethod.Predefined && derivativeFunction == null) {
                throw new IllegalArgumentException(
                        "Predefined differentiation method requires a derivative function. " +
                                "Use derivativeFunction() to provide one, or switch to Numerical or Symbolic method.");
            }

            // Compute symbolic derivative if needed
            Function finalDerivativeFunction = derivativeFunction;
            if (differentiationMethod == DifferentiationMethod.Symbolic) {
                finalDerivativeFunction = new Differentiator().differentiate(targetFunction, false);
            }

            return new NewtonRaphsonSolver(
                    targetFunction,
                    finalDerivativeFunction,
                    differentiationMethod,
                    tolerance,
                    iterations,
                    convergenceCriteria,
                    initialGuess);
        }
    }
}
