package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

import java.util.Objects;

/**
 * Immutable implementation of the Trapezoidal Rule for {@link IntegrationMethod}.
 * <p>
 * The trapezoidal rule approximates the definite integral by dividing the integration
 * interval into n equal subintervals and approximating the area under the curve using
 * trapezoids. This method provides a good balance between simplicity and accuracy.
 * </p>
 *
 * <h2>Mathematical Formula:</h2>
 * <p>
 * For a function f(x) integrated over [a, b] with n iterations:
 * <br>
 * ∫[a,b] f(x) dx ≈ h/2 * [f(a) + 2*Σf(xᵢ) + f(b)]
 * <br>
 * where h = (b-a)/n and xᵢ = a + i*h for i = 1, 2, ..., n-1
 * </p>
 *
 * <h2>Algorithm Characteristics:</h2>
 * <ul>
 *     <li><b>Accuracy</b>: O(h²) - quadratic convergence with step size</li>
 *     <li><b>Best for</b>: General-purpose integration, linear or near-linear functions</li>
 *     <li><b>Function evaluations</b>: n+1 evaluations per integration</li>
 *     <li><b>Error behavior</b>: Exact for linear functions, overestimates convex curves,
 *         underestimates concave curves</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Example 1: Using builder pattern (recommended)
 * Function f = new Function("x^2 + 8*x + 12");
 * TrapeziumIntegrator integrator = TrapeziumIntegrator.builder()
 *     .function(f)
 *     .lowerBound(0.5)
 *     .upperBound(5.0)
 *     .iterations(100)
 *     .build();
 * double result = integrator.integrate(); // ≈ 194.626
 *
 * // Example 2: Using factory method with defaults
 * TrapeziumIntegrator simple = TrapeziumIntegrator.of(f, 0.0, 1.0);
 * double area = simple.integrate(); // Uses default 1000 iterations
 *
 * // Example 3: For simple quadrature (∫₀¹ x² dx = 1/3)
 * Function quadratic = new Function("x^2");
 * TrapeziumIntegrator quad = TrapeziumIntegrator.of(quadratic, 0.0, 1.0, 1000);
 * double exactlyOneThird = quad.integrate(); // ≈ 0.333333...
 * }</pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>
 * This class is immutable and thread-safe. Multiple threads can safely call
 * {@code integrate()} on the same instance concurrently.
 * </p>
 *
 * @author Ryan Harrison
 */
public final class TrapeziumIntegrator implements IntegrationMethod {
    /**
     * Default number of iterations for integration when not specified.
     */
    private static final int DEFAULT_ITERATIONS = 1000;

    private final Function targetFunction;
    private final double lowerBound;
    private final double upperBound;
    private final int iterations;

    /**
     * Private constructor for builder and factory methods.
     * Validates all parameters and ensures invariants.
     *
     * @param targetFunction the function to integrate
     * @param lowerBound     the lower bound of integration
     * @param upperBound     the upper bound of integration
     * @param iterations     the number of subdivisions
     * @throws IllegalArgumentException if validation fails
     */
    private TrapeziumIntegrator(Function targetFunction, double lowerBound,
                                double upperBound, int iterations) {
        if (targetFunction == null) {
            throw new IllegalArgumentException("Target function cannot be null");
        }
        if (!Double.isFinite(lowerBound)) {
            throw new IllegalArgumentException(
                    "Lower bound must be finite, got: " + lowerBound);
        }
        if (!Double.isFinite(upperBound)) {
            throw new IllegalArgumentException(
                    "Upper bound must be finite, got: " + upperBound);
        }
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(
                    "Lower bound must be less than upper bound, got: [" +
                            lowerBound + ", " + upperBound + "]");
        }
        if (iterations <= 0) {
            throw new IllegalArgumentException(
                    "Iterations must be positive, got: " + iterations);
        }

        this.targetFunction = targetFunction;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.iterations = iterations;
    }

    /**
     * Creates a TrapeziumIntegrator with specified bounds and default iterations.
     * <p>
     * Uses {@value #DEFAULT_ITERATIONS} iterations for a good balance between
     * accuracy and performance.
     * </p>
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @return a new TrapeziumIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static TrapeziumIntegrator of(Function function, double lowerBound, double upperBound) {
        return new TrapeziumIntegrator(function, lowerBound, upperBound, DEFAULT_ITERATIONS);
    }

    /**
     * Creates a TrapeziumIntegrator with all parameters specified.
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @param iterations the number of subdivisions to use
     * @return a new TrapeziumIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static TrapeziumIntegrator of(Function function, double lowerBound,
                                         double upperBound, int iterations) {
        return new TrapeziumIntegrator(function, lowerBound, upperBound, iterations);
    }

    /**
     * Creates a new builder for constructing a TrapeziumIntegrator with named parameters.
     * <p>
     * The builder pattern is recommended for clarity when setting multiple parameters.
     * </p>
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public double integrate() {
        double range = upperBound - lowerBound;
        double stepSize = range / iterations;

        // Sum interior points (weighted by 1.0 in trapezoidal rule)
        double sum = 0.0;
        for (int i = 1; i < iterations; i++) {
            double x = lowerBound + i * stepSize;
            double fx = targetFunction.evaluateAt(x);

            if (!Double.isFinite(fx)) {
                throw new ArithmeticException(
                        "Function evaluation produced non-finite value at x = " + x + ": " + fx);
            }

            sum += fx;
        }

        // Add endpoints with weight 0.5
        double fa = targetFunction.evaluateAt(lowerBound);
        double fb = targetFunction.evaluateAt(upperBound);

        if (!Double.isFinite(fa)) {
            throw new ArithmeticException(
                    "Function evaluation at lower bound produced non-finite value: " + fa);
        }
        if (!Double.isFinite(fb)) {
            throw new ArithmeticException(
                    "Function evaluation at upper bound produced non-finite value: " + fb);
        }

        sum += (fa + fb) / 2.0;

        return sum * stepSize;
    }

    @Override
    public int getIterations() {
        return iterations;
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
    public Function getTargetFunction() {
        return targetFunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrapeziumIntegrator that)) return false;
        return Double.compare(that.lowerBound, lowerBound) == 0 &&
                Double.compare(that.upperBound, upperBound) == 0 &&
                iterations == that.iterations &&
                Objects.equals(targetFunction.getEquation(), that.targetFunction.getEquation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetFunction.getEquation(), lowerBound, upperBound, iterations);
    }

    @Override
    public String toString() {
        return String.format("TrapeziumIntegrator(f=%s, bounds=[%.4f, %.4f], iterations=%d)",
                targetFunction.getEquation(), lowerBound, upperBound, iterations);
    }

    /**
     * Builder for constructing {@link TrapeziumIntegrator} instances with named parameters.
     * <p>
     * All parameters are required. The builder validates parameters as they are set
     * and throws {@link IllegalArgumentException} for invalid values.
     * </p>
     *
     * <h2>Usage Example:</h2>
     * <pre>{@code
     * TrapeziumIntegrator integrator = TrapeziumIntegrator.builder()
     *     .function(new Function("sin(x)"))
     *     .lowerBound(0.0)
     *     .upperBound(Math.PI)
     *     .iterations(500)
     *     .build();
     * }</pre>
     */
    public static final class Builder {
        private Function function;
        private Double lowerBound;
        private Double upperBound;
        private int iterations = DEFAULT_ITERATIONS;

        private Builder() {
        }

        /**
         * Sets the function to integrate.
         *
         * @param function the target function
         * @return this builder for method chaining
         * @throws IllegalArgumentException if function is null
         */
        public Builder function(Function function) {
            if (function == null) {
                throw new IllegalArgumentException("Function cannot be null");
            }
            this.function = function;
            return this;
        }

        /**
         * Sets the lower bound of the integration interval.
         *
         * @param lowerBound the lower bound
         * @return this builder for method chaining
         * @throws IllegalArgumentException if lowerBound is not finite
         */
        public Builder lowerBound(double lowerBound) {
            if (!Double.isFinite(lowerBound)) {
                throw new IllegalArgumentException(
                        "Lower bound must be finite, got: " + lowerBound);
            }
            this.lowerBound = lowerBound;
            return this;
        }

        /**
         * Sets the upper bound of the integration interval.
         *
         * @param upperBound the upper bound
         * @return this builder for method chaining
         * @throws IllegalArgumentException if upperBound is not finite
         */
        public Builder upperBound(double upperBound) {
            if (!Double.isFinite(upperBound)) {
                throw new IllegalArgumentException(
                        "Upper bound must be finite, got: " + upperBound);
            }
            this.upperBound = upperBound;
            return this;
        }

        /**
         * Sets the number of iterations (subdivisions) for the integration.
         * <p>
         * Higher values increase accuracy but require more computation.
         * If not set, defaults to {@value #DEFAULT_ITERATIONS}.
         * </p>
         *
         * @param iterations the number of iterations, must be positive
         * @return this builder for method chaining
         * @throws IllegalArgumentException if iterations is not positive
         */
        public Builder iterations(int iterations) {
            if (iterations <= 0) {
                throw new IllegalArgumentException(
                        "Iterations must be positive, got: " + iterations);
            }
            this.iterations = iterations;
            return this;
        }

        /**
         * Builds the TrapeziumIntegrator with the configured parameters.
         *
         * @return a new TrapeziumIntegrator instance
         * @throws IllegalStateException if required parameters are not set
         */
        public TrapeziumIntegrator build() {
            if (function == null) {
                throw new IllegalStateException("Function must be set");
            }
            if (lowerBound == null) {
                throw new IllegalStateException("Lower bound must be set");
            }
            if (upperBound == null) {
                throw new IllegalStateException("Upper bound must be set");
            }

            return new TrapeziumIntegrator(function, lowerBound, upperBound, iterations);
        }
    }
}
