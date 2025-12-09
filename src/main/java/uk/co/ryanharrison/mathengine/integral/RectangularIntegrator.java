package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.core.Function;

import java.util.Objects;

/**
 * Immutable implementation of the Rectangular Rule for {@link IntegrationMethod}.
 * <p>
 * The rectangular rule (also known as the Riemann sum) approximates the definite
 * integral by dividing the integration interval into n equal subintervals and
 * approximating the area using rectangles. The height of each rectangle is
 * determined by the function value at a specific point (left, midpoint, or right)
 * within each subinterval.
 * </p>
 *
 * <h2>Mathematical Formula:</h2>
 * <p>
 * For a function f(x) integrated over [a, b] with n iterations:
 * <br>
 * ∫[a,b] f(x) dx ≈ h * Σf(xᵢ)
 * <br>
 * where h = (b-a)/n and xᵢ depends on the {@link RectanglePosition}:
 * </p>
 * <ul>
 *     <li><b>LEFT</b>: xᵢ = a + i*h (i = 0, 1, ..., n-1)</li>
 *     <li><b>MIDPOINT</b>: xᵢ = a + (i + 0.5)*h (i = 0, 1, ..., n-1)</li>
 *     <li><b>RIGHT</b>: xᵢ = a + (i + 1)*h (i = 0, 1, ..., n-1)</li>
 * </ul>
 *
 * <h2>Algorithm Characteristics:</h2>
 * <ul>
 *     <li><b>Accuracy</b>: O(h) for left/right, O(h²) for midpoint</li>
 *     <li><b>Best for</b>: Simple approximations, educational purposes, or when
 *         specific endpoint behavior is desired</li>
 *     <li><b>Function evaluations</b>: n evaluations per integration</li>
 *     <li><b>Error behavior</b>:
 *         <ul>
 *             <li>LEFT: Underestimates increasing functions</li>
 *             <li>MIDPOINT: Generally unbiased, best accuracy</li>
 *             <li>RIGHT: Overestimates increasing functions</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Midpoint Rule Advantage:</h2>
 * <p>
 * The midpoint rule often provides better accuracy than the trapezoidal rule
 * for the same number of function evaluations. While trapezoidal rule uses n+1
 * evaluations, midpoint uses only n, yet achieves comparable or better accuracy
 * for smooth functions.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Example 1: Using midpoint rule (recommended)
 * Function f = new Function("x^2");
 * RectangularIntegrator midpoint = RectangularIntegrator.builder()
 *     .function(f)
 *     .lowerBound(0.0)
 *     .upperBound(1.0)
 *     .iterations(100)
 *     .position(RectanglePosition.MIDPOINT)
 *     .build();
 * double result = midpoint.integrate(); // ≈ 0.333...
 *
 * // Example 2: Quick creation with default midpoint position
 * RectangularIntegrator simple = RectangularIntegrator.of(f, 0.0, 1.0);
 * double area = simple.integrate(); // Uses MIDPOINT with default iterations
 *
 * // Example 3: Comparing left, midpoint, and right for f(x) = x
 * Function linear = new Function("x");
 * double leftSum = RectangularIntegrator.of(linear, 0.0, 2.0, RectanglePosition.LEFT).integrate();
 * double midSum = RectangularIntegrator.of(linear, 0.0, 2.0, RectanglePosition.MIDPOINT).integrate();
 * double rightSum = RectangularIntegrator.of(linear, 0.0, 2.0, RectanglePosition.RIGHT).integrate();
 * // All should give ≈ 2.0 (exact for linear functions)
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
public final class RectangularIntegrator implements IntegrationMethod {
    /**
     * Default number of iterations for integration when not specified.
     */
    private static final int DEFAULT_ITERATIONS = 1000;

    private final Function targetFunction;
    private final double lowerBound;
    private final double upperBound;
    private final int iterations;
    private final RectanglePosition position;

    /**
     * Private constructor for builder and factory methods.
     * Validates all parameters and ensures invariants.
     *
     * @param targetFunction the function to integrate
     * @param lowerBound     the lower bound of integration
     * @param upperBound     the upper bound of integration
     * @param iterations     the number of subdivisions
     * @param position       the rectangle position strategy
     * @throws IllegalArgumentException if validation fails
     */
    private RectangularIntegrator(Function targetFunction, double lowerBound,
                                  double upperBound, int iterations,
                                  RectanglePosition position) {
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
        if (position == null) {
            throw new IllegalArgumentException("Rectangle position cannot be null");
        }

        this.targetFunction = targetFunction;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.iterations = iterations;
        this.position = position;
    }

    /**
     * Creates a RectangularIntegrator with specified bounds, default iterations,
     * and MIDPOINT position.
     * <p>
     * Uses {@value #DEFAULT_ITERATIONS} iterations and {@link RectanglePosition#MIDPOINT}
     * for the best balance of accuracy and performance.
     * </p>
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @return a new RectangularIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static RectangularIntegrator of(Function function, double lowerBound, double upperBound) {
        return new RectangularIntegrator(function, lowerBound, upperBound,
                DEFAULT_ITERATIONS, RectanglePosition.MIDPOINT);
    }

    /**
     * Creates a RectangularIntegrator with specified bounds and position,
     * using default iterations.
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @param position   the rectangle position strategy
     * @return a new RectangularIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static RectangularIntegrator of(Function function, double lowerBound,
                                           double upperBound, RectanglePosition position) {
        return new RectangularIntegrator(function, lowerBound, upperBound,
                DEFAULT_ITERATIONS, position);
    }

    /**
     * Creates a RectangularIntegrator with all parameters specified.
     *
     * @param function   the function to integrate
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @param iterations the number of subdivisions to use
     * @param position   the rectangle position strategy
     * @return a new RectangularIntegrator instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static RectangularIntegrator of(Function function, double lowerBound,
                                           double upperBound, int iterations,
                                           RectanglePosition position) {
        return new RectangularIntegrator(function, lowerBound, upperBound, iterations, position);
    }

    /**
     * Creates a new builder for constructing a RectangularIntegrator with named parameters.
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
        double offset = position.getOffset();

        double sum = 0.0;
        for (int i = 0; i < iterations; i++) {
            double x = lowerBound + (i + offset) * stepSize;
            double fx = targetFunction.evaluateAt(x);

            if (!Double.isFinite(fx)) {
                throw new ArithmeticException(
                        "Function evaluation produced non-finite value at x = " + x + ": " + fx);
            }

            sum += fx;
        }

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

    /**
     * Returns the rectangle position strategy used by this integrator.
     *
     * @return the rectangle position (LEFT, MIDPOINT, or RIGHT)
     */
    public RectanglePosition getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RectangularIntegrator that)) return false;
        return Double.compare(that.lowerBound, lowerBound) == 0 &&
                Double.compare(that.upperBound, upperBound) == 0 &&
                iterations == that.iterations &&
                position == that.position &&
                Objects.equals(targetFunction.getEquation(), that.targetFunction.getEquation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetFunction.getEquation(), lowerBound, upperBound,
                iterations, position);
    }

    @Override
    public String toString() {
        return String.format("RectangularIntegrator(f=%s, bounds=[%.4f, %.4f], iterations=%d, position=%s)",
                targetFunction.getEquation(), lowerBound, upperBound, iterations, position);
    }

    /**
     * Builder for constructing {@link RectangularIntegrator} instances with named parameters.
     * <p>
     * All parameters are required except iterations and position, which have sensible defaults.
     * The builder validates parameters as they are set and throws
     * {@link IllegalArgumentException} for invalid values.
     * </p>
     *
     * <h2>Usage Example:</h2>
     * <pre>{@code
     * RectangularIntegrator integrator = RectangularIntegrator.builder()
     *     .function(new Function("sqrt(1 - x^2)"))
     *     .lowerBound(0.0)
     *     .upperBound(1.0)
     *     .iterations(500)
     *     .position(RectanglePosition.MIDPOINT)
     *     .build();
     * }</pre>
     */
    public static final class Builder {
        private Function function;
        private Double lowerBound;
        private Double upperBound;
        private int iterations = DEFAULT_ITERATIONS;
        private RectanglePosition position = RectanglePosition.MIDPOINT;

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
         * Sets the rectangle position strategy.
         * <p>
         * If not set, defaults to {@link RectanglePosition#MIDPOINT} for best accuracy.
         * </p>
         *
         * @param position the rectangle position (LEFT, MIDPOINT, or RIGHT)
         * @return this builder for method chaining
         * @throws IllegalArgumentException if position is null
         */
        public Builder position(RectanglePosition position) {
            if (position == null) {
                throw new IllegalArgumentException("Position cannot be null");
            }
            this.position = position;
            return this;
        }

        /**
         * Builds the RectangularIntegrator with the configured parameters.
         *
         * @return a new RectangularIntegrator instance
         * @throws IllegalStateException if required parameters are not set
         */
        public RectangularIntegrator build() {
            if (function == null) {
                throw new IllegalStateException("Function must be set");
            }
            if (lowerBound == null) {
                throw new IllegalStateException("Lower bound must be set");
            }
            if (upperBound == null) {
                throw new IllegalStateException("Upper bound must be set");
            }

            return new RectangularIntegrator(function, lowerBound, upperBound,
                    iterations, position);
        }
    }
}
