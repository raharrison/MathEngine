package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.core.Function;

import java.util.Objects;

/**
 * Immutable implementation of Simpson's Rule for {@link IntegrationMethod}.
 * <p>
 * Simpson's Rule approximates the definite integral using quadratic interpolation
 * (parabolas) rather than linear approximation. This generally provides higher
 * accuracy than the trapezoidal rule for smooth functions.
 * </p>
 *
 * <h2>Mathematical Formula:</h2>
 * <p>
 * For a function f(x) integrated over [a, b] with n iterations:
 * <br>
 * ∫[a,b] f(x) dx ≈ (h/6) * [f(a) + f(b) + 4*Σf(x₂ᵢ₋₁) + 2*Σf(x₂ᵢ)]
 * <br>
 * where h = (b-a)/n, and points are evaluated at subinterval midpoints and boundaries.
 * </p>
 *
 * <h2>Algorithm Characteristics:</h2>
 * <ul>
 *     <li><b>Accuracy</b>: O(h⁴) - quartic convergence, significantly better than trapezoidal</li>
 *     <li><b>Best for</b>: Smooth functions with continuous derivatives</li>
 *     <li><b>Function evaluations</b>: 2n+1 evaluations per integration</li>
 *     <li><b>Error behavior</b>: Exact for polynomials up to degree 3, excellent for
 *         smooth curves with minimal oscillation</li>
 *     <li><b>Limitation</b>: Less accurate for functions with discontinuities or
 *         sharp changes in slope</li>
 * </ul>
 *
 * <h2>Mathematical Background:</h2>
 * <p>
 * Simpson's Rule works by fitting a quadratic polynomial through three consecutive
 * points and integrating the polynomial exactly. This process is repeated across
 * the entire interval. The method is exact for polynomials of degree ≤ 3 because
 * the error term depends on the fourth derivative.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Example 1: Integrating a smooth polynomial
 * Function f = new Function("x^3 - 2*x^2 + x + 1");
 * SimpsonIntegrator integrator = SimpsonIntegrator.builder()
 *     .function(f)
 *     .lowerBound(0.0)
 *     .upperBound(2.0)
 *     .iterations(100)
 *     .build();
 * double result = integrator.integrate(); // High accuracy
 *
 * // Example 2: Using factory method for quick integration
 * Function sinFunc = new Function("sin(x)");
 * SimpsonIntegrator simpson = SimpsonIntegrator.of(sinFunc, 0.0, Math.PI);
 * double area = simpson.integrate(); // ≈ 2.0 (exact value)
 *
 * // Example 3: Comparing with known result (∫₀² x³ dx = 4)
 * Function cubic = new Function("x^3");
 * double exactResult = SimpsonIntegrator.of(cubic, 0.0, 2.0, 50).integrate();
 * // Result will be very close to 4.0
 * }</pre>
 *
 * <h2>Comparison with Other Methods:</h2>
 * <ul>
 *     <li><b>vs Trapezoidal</b>: More accurate for smooth functions, but requires
 *         more function evaluations (2n+1 vs n+1)</li>
 *     <li><b>vs Rectangular</b>: Significantly more accurate, recommended for
 *         production use unless simplicity is paramount</li>
 * </ul>
 *
 * <h2>Thread Safety:</h2>
 * <p>
 * This class is immutable and thread-safe. Multiple threads can safely call
 * {@code integrate()} on the same instance concurrently.
 * </p>
 *
 * @author Ryan Harrison
 */
public final class SimpsonIntegrator implements IntegrationMethod {
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
    private SimpsonIntegrator(Function targetFunction, double lowerBound,
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
     * Creates a SimpsonIntegrator with specified bounds and default iterations.
     * <p>
     * Uses {@value #DEFAULT_ITERATIONS} iterations for excellent accuracy
     * with reasonable performance.
     * </p>
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @return a new SimpsonIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static SimpsonIntegrator of(Function function, double lowerBound, double upperBound) {
        return new SimpsonIntegrator(function, lowerBound, upperBound, DEFAULT_ITERATIONS);
    }

    /**
     * Creates a SimpsonIntegrator with all parameters specified.
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @param iterations the number of subdivisions to use
     * @return a new SimpsonIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static SimpsonIntegrator of(Function function, double lowerBound,
                                       double upperBound, int iterations) {
        return new SimpsonIntegrator(function, lowerBound, upperBound, iterations);
    }

    /**
     * Creates a new builder for constructing a SimpsonIntegrator with named parameters.
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

        // Evaluate endpoints
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

        // Sum midpoints (weighted by 4) - these are at odd multiples of h/2
        double sum1 = targetFunction.evaluateAt(lowerBound + range / (iterations * 2.0));
        if (!Double.isFinite(sum1)) {
            throw new ArithmeticException(
                    "Function evaluation produced non-finite value during integration");
        }

        // Sum interior points and their corresponding midpoints
        double sum2 = 0.0;

        for (int i = 1; i < iterations; i++) {
            // Midpoint at (i + 0.5) * h - weighted by 4
            double x1 = lowerBound + range * (i + 0.5) / iterations;
            double fx1 = targetFunction.evaluateAt(x1);

            if (!Double.isFinite(fx1)) {
                throw new ArithmeticException(
                        "Function evaluation produced non-finite value at x = " + x1 + ": " + fx1);
            }

            sum1 += fx1;

            // Interior boundary point at i * h - weighted by 2
            double x2 = lowerBound + range * i / iterations;
            double fx2 = targetFunction.evaluateAt(x2);

            if (!Double.isFinite(fx2)) {
                throw new ArithmeticException(
                        "Function evaluation produced non-finite value at x = " + x2 + ": " + fx2);
            }

            sum2 += fx2;
        }

        // Simpson's formula: (h/6) * [f(a) + f(b) + 4*sum1 + 2*sum2]
        // where h = range/iterations
        return (fa + fb + 4.0 * sum1 + 2.0 * sum2) * range / (iterations * 6.0);
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
        if (!(o instanceof SimpsonIntegrator that)) return false;
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
        return String.format("SimpsonIntegrator(f=%s, bounds=[%.4f, %.4f], iterations=%d)",
                targetFunction.getEquation(), lowerBound, upperBound, iterations);
    }

    /**
     * Builder for constructing {@link SimpsonIntegrator} instances with named parameters.
     * <p>
     * All parameters are required except iterations (defaults to {@value #DEFAULT_ITERATIONS}).
     * The builder validates parameters as they are set and throws
     * {@link IllegalArgumentException} for invalid values.
     * </p>
     *
     * <h2>Usage Example:</h2>
     * <pre>{@code
     * SimpsonIntegrator integrator = SimpsonIntegrator.builder()
     *     .function(new Function("exp(-x^2)"))
     *     .lowerBound(0.0)
     *     .upperBound(1.0)
     *     .iterations(200)
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
         * Builds the SimpsonIntegrator with the configured parameters.
         *
         * @return a new SimpsonIntegrator instance
         * @throws IllegalStateException if required parameters are not set
         */
        public SimpsonIntegrator build() {
            if (function == null) {
                throw new IllegalStateException("Function must be set");
            }
            if (lowerBound == null) {
                throw new IllegalStateException("Lower bound must be set");
            }
            if (upperBound == null) {
                throw new IllegalStateException("Upper bound must be set");
            }

            return new SimpsonIntegrator(function, lowerBound, upperBound, iterations);
        }
    }
}
