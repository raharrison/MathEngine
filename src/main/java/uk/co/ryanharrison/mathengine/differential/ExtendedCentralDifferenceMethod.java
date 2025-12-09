package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.core.Function;

import java.util.Objects;

/**
 * Immutable implementation of {@link NumericalDifferentiationMethod} using extended central difference formulas.
 * <p>
 * Extended central difference methods use more function evaluation points than standard central differences,
 * resulting in higher-order accuracy. These formulas are symmetric around the target point and typically
 * provide O(h⁴) accuracy compared to O(h²) for standard central differences.
 * </p>
 *
 * <h2>Accuracy:</h2>
 * <p>
 * The extended formulas achieve fourth-order accuracy O(h⁴) for first and second derivatives,
 * providing significantly better accuracy than standard central differences for the same step size.
 * This comes at the cost of requiring more function evaluations (up to 7 points).
 * </p>
 *
 * <h2>Derivative Formulas:</h2>
 * <h3>First Derivative:</h3>
 * f'(x) ≈ [f(x-2h) - 8f(x-h) + 8f(x+h) - f(x+2h)] / (12h)
 * <br>Error: O(h⁴)
 *
 * <h3>Second Derivative:</h3>
 * f''(x) ≈ [-f(x-2h) + 16f(x-h) - 30f(x) + 16f(x+h) - f(x+2h)] / (12h²)
 * <br>Error: O(h⁴)
 *
 * <h3>Third Derivative:</h3>
 * f'''(x) ≈ [f(x-3h) - 8f(x-2h) + 13f(x-h) - 13f(x+h) + 8f(x+2h) - f(x+3h)] / (8h³)
 * <br>Error: O(h²)
 *
 * <h3>Fourth Derivative:</h3>
 * f''''(x) ≈ [-f(x-3h) + 12f(x-2h) - 39f(x-h) + 56f(x) - 39f(x+h) + 12f(x+2h) - f(x+3h)] / (6h⁴)
 * <br>Error: O(h²)
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Create function f(x) = x^4
 * Function f = new Function("x^4");
 *
 * // Using builder for custom configuration
 * ExtendedCentralDifferenceMethod method = ExtendedCentralDifferenceMethod.builder()
 *     .targetFunction(f)
 *     .targetPoint(2.0)
 *     .stepSize(0.01)
 *     .build();
 *
 * // Compute derivatives with high accuracy
 * double firstDerivative = method.deriveFirst();   // ≈ 32.0 (4x³ at x=2)
 * double secondDerivative = method.deriveSecond(); // ≈ 48.0 (12x² at x=2)
 *
 * // Using static factory with defaults
 * ExtendedCentralDifferenceMethod defaultMethod = ExtendedCentralDifferenceMethod.of(f);
 *
 * // Create modified copy with different target point
 * ExtendedCentralDifferenceMethod atX3 = method.withTargetPoint(3.0);
 * }</pre>
 *
 * <h2>When to Use:</h2>
 * <ul>
 *     <li>When higher accuracy is required and computational cost is acceptable</li>
 *     <li>When the function is smooth enough to justify more evaluation points</li>
 *     <li>When you need derivatives at interior points (not boundaries)</li>
 *     <li>When step size cannot be made arbitrarily small due to rounding errors</li>
 * </ul>
 *
 * @see DividedDifferenceMethod
 * @see RichardsonExtrapolationMethod
 */
public final class ExtendedCentralDifferenceMethod implements NumericalDifferentiationMethod {
    private final Function targetFunction;
    private final double targetPoint;
    private final double stepSize;

    // Pre-computed powers of h for efficiency
    private final double h2;  // h²
    private final double h3;  // h³
    private final double h4;  // h⁴

    /**
     * Private constructor - use builder() or static factory methods.
     *
     * @param targetFunction the function to differentiate
     * @param targetPoint    the point at which to evaluate derivatives
     * @param stepSize       the step size (h) for finite differences
     */
    private ExtendedCentralDifferenceMethod(Function targetFunction, double targetPoint, double stepSize) {
        this.targetFunction = Objects.requireNonNull(targetFunction, "Target function cannot be null");
        this.targetPoint = targetPoint;
        this.stepSize = stepSize;

        if (stepSize <= 0.0) {
            throw new IllegalArgumentException("Step size must be positive, got: " + stepSize);
        }
        if (!Double.isFinite(targetPoint)) {
            throw new IllegalArgumentException("Target point must be finite, got: " + targetPoint);
        }

        // Pre-compute powers of h
        this.h2 = stepSize * stepSize;
        this.h3 = h2 * stepSize;
        this.h4 = h2 * h2;
    }

    /**
     * Creates a new ExtendedCentralDifferenceMethod with the specified function and default settings.
     * <p>
     * Defaults: target point = 1.0, step size = 0.01
     * </p>
     *
     * @param function the function to differentiate
     * @return a new ExtendedCentralDifferenceMethod instance
     * @throws NullPointerException if function is null
     */
    public static ExtendedCentralDifferenceMethod of(Function function) {
        return new ExtendedCentralDifferenceMethod(function, DEFAULT_TARGET_POINT, DEFAULT_STEP_SIZE);
    }

    /**
     * Creates a new ExtendedCentralDifferenceMethod with the specified function and step size.
     * <p>
     * Default: target point = 1.0
     * </p>
     *
     * @param function the function to differentiate
     * @param stepSize the step size (must be positive)
     * @return a new ExtendedCentralDifferenceMethod instance
     * @throws NullPointerException     if function is null
     * @throws IllegalArgumentException if stepSize is not positive
     */
    public static ExtendedCentralDifferenceMethod of(Function function, double stepSize) {
        return new ExtendedCentralDifferenceMethod(function, DEFAULT_TARGET_POINT, stepSize);
    }

    /**
     * Creates a new builder for constructing ExtendedCentralDifferenceMethod instances.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public double deriveFirst() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;

        return (f.evaluateAt(x - 2.0 * h) - 8.0 * f.evaluateAt(x - h)
                + 8.0 * f.evaluateAt(x + h) - f.evaluateAt(x + 2.0 * h)) / (12.0 * h);
    }

    @Override
    public double deriveSecond() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;

        return (-f.evaluateAt(x - 2.0 * h) + 16.0 * f.evaluateAt(x - h) - 30.0 * f.evaluateAt(x)
                + 16.0 * f.evaluateAt(x + h) - f.evaluateAt(x + 2.0 * h)) / (12.0 * h2);
    }

    @Override
    public double deriveThird() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;

        return (f.evaluateAt(x - 3.0 * h) - 8.0 * f.evaluateAt(x - 2.0 * h)
                + 13.0 * f.evaluateAt(x - h) - 13.0 * f.evaluateAt(x + h)
                + 8.0 * f.evaluateAt(x + 2.0 * h) - f.evaluateAt(x + 3.0 * h)) / (8.0 * h3);
    }

    @Override
    public double deriveFourth() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;

        return (-f.evaluateAt(x - 3.0 * h) + 12.0 * f.evaluateAt(x - 2.0 * h)
                - 39.0 * f.evaluateAt(x - h) + 56.0 * f.evaluateAt(x)
                - 39.0 * f.evaluateAt(x + h) + 12.0 * f.evaluateAt(x + 2.0 * h)
                - f.evaluateAt(x + 3.0 * h)) / (6.0 * h4);
    }

    @Override
    public double getStepSize() {
        return stepSize;
    }

    @Override
    public Function getTargetFunction() {
        return targetFunction;
    }

    @Override
    public double getTargetPoint() {
        return targetPoint;
    }

    /**
     * Creates a new ExtendedCentralDifferenceMethod with the specified target point.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param targetPoint the new target point
     * @return a new ExtendedCentralDifferenceMethod instance with the updated target point
     * @throws IllegalArgumentException if targetPoint is not finite
     */
    public ExtendedCentralDifferenceMethod withTargetPoint(double targetPoint) {
        return new ExtendedCentralDifferenceMethod(targetFunction, targetPoint, stepSize);
    }

    /**
     * Creates a new ExtendedCentralDifferenceMethod with the specified step size.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param stepSize the new step size
     * @return a new ExtendedCentralDifferenceMethod instance with the updated step size
     * @throws IllegalArgumentException if stepSize is not positive
     */
    public ExtendedCentralDifferenceMethod withStepSize(double stepSize) {
        return new ExtendedCentralDifferenceMethod(targetFunction, targetPoint, stepSize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedCentralDifferenceMethod that)) return false;
        return Double.compare(that.targetPoint, targetPoint) == 0 &&
                Double.compare(that.stepSize, stepSize) == 0 &&
                Objects.equals(targetFunction, that.targetFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetFunction, targetPoint, stepSize);
    }

    @Override
    public String toString() {
        return String.format("ExtendedCentralDifferenceMethod(x=%.4f, h=%.6f)", targetPoint, stepSize);
    }

    /**
     * Builder for constructing {@link ExtendedCentralDifferenceMethod} instances with custom configuration.
     */
    public static final class Builder {
        private Function targetFunction;
        private double targetPoint = DEFAULT_TARGET_POINT;
        private double stepSize = DEFAULT_STEP_SIZE;

        private Builder() {
        }

        /**
         * Sets the target function to differentiate.
         *
         * @param targetFunction the function to differentiate
         * @return this builder
         * @throws NullPointerException if targetFunction is null
         */
        public Builder targetFunction(Function targetFunction) {
            this.targetFunction = Objects.requireNonNull(targetFunction, "Target function cannot be null");
            return this;
        }

        /**
         * Sets the point at which to evaluate derivatives.
         * <p>
         * Default: 1.0
         * </p>
         *
         * @param targetPoint the x-coordinate for differentiation
         * @return this builder
         * @throws IllegalArgumentException if targetPoint is not finite
         */
        public Builder targetPoint(double targetPoint) {
            if (!Double.isFinite(targetPoint)) {
                throw new IllegalArgumentException("Target point must be finite, got: " + targetPoint);
            }
            this.targetPoint = targetPoint;
            return this;
        }

        /**
         * Sets the step size for finite differences.
         * <p>
         * Default: 0.01
         * </p>
         *
         * @param stepSize the step size (must be positive)
         * @return this builder
         * @throws IllegalArgumentException if stepSize is not positive
         */
        public Builder stepSize(double stepSize) {
            if (stepSize <= 0.0) {
                throw new IllegalArgumentException("Step size must be positive, got: " + stepSize);
            }
            this.stepSize = stepSize;
            return this;
        }

        /**
         * Builds a new ExtendedCentralDifferenceMethod instance.
         *
         * @return a new ExtendedCentralDifferenceMethod
         * @throws NullPointerException if targetFunction was not set
         */
        public ExtendedCentralDifferenceMethod build() {
            Objects.requireNonNull(targetFunction, "Target function must be set");
            return new ExtendedCentralDifferenceMethod(targetFunction, targetPoint, stepSize);
        }
    }
}
