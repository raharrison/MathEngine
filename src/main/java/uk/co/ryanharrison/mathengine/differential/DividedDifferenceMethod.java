package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

import java.util.Objects;

/**
 * Immutable implementation of {@link NumericalDifferentiationMethod} using divided difference formulas.
 * <p>
 * The divided difference method approximates derivatives using finite differences computed from
 * function evaluations at discrete points. This implementation supports three difference directions:
 * </p>
 * <ul>
 *     <li><b>Forward</b>: Uses points at x, x+h, x+2h, etc.</li>
 *     <li><b>Central</b>: Uses points symmetrically around x (most accurate)</li>
 *     <li><b>Backward</b>: Uses points at x, x-h, x-2h, etc.</li>
 * </ul>
 *
 * <h2>Accuracy:</h2>
 * <p>
 * Central differences provide O(h²) accuracy, while forward and backward differences
 * also achieve O(h²) for the multi-point formulas used here. Central differences
 * typically have smaller error constants.
 * </p>
 *
 * <h2>Derivative Formulas:</h2>
 * <h3>First Derivative (Central):</h3>
 * f'(x) ≈ [f(x+h) - f(x-h)] / (2h)
 *
 * <h3>Second Derivative (Central):</h3>
 * f''(x) ≈ [f(x-h) - 2f(x) + f(x+h)] / h²
 *
 * <h3>Third Derivative (Central):</h3>
 * f'''(x) ≈ [-f(x-2h) + 2f(x-h) - 2f(x+h) + f(x+2h)] / (2h³)
 *
 * <h3>Fourth Derivative (Central):</h3>
 * f''''(x) ≈ [f(x-2h) - 4f(x-h) + 6f(x) - 4f(x+h) + f(x+2h)] / h⁴
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Create function f(x) = x^3
 * Function f = new Function("x^3");
 *
 * // Using builder with central differences
 * DividedDifferenceMethod method = DividedDifferenceMethod.builder()
 *     .targetFunction(f)
 *     .targetPoint(2.0)
 *     .stepSize(0.001)
 *     .direction(DifferencesDirection.Central)
 *     .build();
 *
 * // Compute derivatives
 * double firstDerivative = method.deriveFirst();   // ≈ 12.0 (3x² at x=2)
 * double secondDerivative = method.deriveSecond(); // ≈ 12.0 (6x at x=2)
 *
 * // Using static factory with defaults (central direction, h=0.01)
 * DividedDifferenceMethod defaultMethod = DividedDifferenceMethod.of(f);
 * defaultMethod.deriveFirst(); // at x=1.0 by default
 *
 * // Forward differences (for boundary conditions)
 * DividedDifferenceMethod forward = DividedDifferenceMethod.builder()
 *     .targetFunction(f)
 *     .direction(DifferencesDirection.Forward)
 *     .build();
 * }</pre>
 *
 * @see DifferencesDirection
 * @see ExtendedCentralDifferenceMethod
 * @see RichardsonExtrapolationMethod
 */
public final class DividedDifferenceMethod implements NumericalDifferentiationMethod {
    private final Function targetFunction;
    private final double targetPoint;
    private final double stepSize;
    private final DifferencesDirection direction;

    /**
     * Private constructor - use builder() or static factory methods.
     *
     * @param targetFunction the function to differentiate
     * @param targetPoint    the point at which to evaluate derivatives
     * @param stepSize       the step size (h) for finite differences
     * @param direction      the direction of differences to use
     */
    private DividedDifferenceMethod(Function targetFunction, double targetPoint, double stepSize,
                                    DifferencesDirection direction) {
        this.targetFunction = Objects.requireNonNull(targetFunction, "Target function cannot be null");
        this.targetPoint = targetPoint;
        this.stepSize = stepSize;
        this.direction = Objects.requireNonNull(direction, "Direction cannot be null");

        if (stepSize <= 0.0) {
            throw new IllegalArgumentException("Step size must be positive, got: " + stepSize);
        }
        if (!Double.isFinite(targetPoint)) {
            throw new IllegalArgumentException("Target point must be finite, got: " + targetPoint);
        }
    }

    /**
     * Creates a new DividedDifferenceMethod with the specified function and default settings.
     * <p>
     * Defaults: target point = 1.0, step size = 0.01, direction = Central
     * </p>
     *
     * @param function the function to differentiate
     * @return a new DividedDifferenceMethod instance
     * @throws NullPointerException if function is null
     */
    public static DividedDifferenceMethod of(Function function) {
        return new DividedDifferenceMethod(function, DEFAULT_TARGET_POINT, DEFAULT_STEP_SIZE,
                DifferencesDirection.Central);
    }

    /**
     * Creates a new DividedDifferenceMethod with the specified function and direction.
     * <p>
     * Defaults: target point = 1.0, step size = 0.01
     * </p>
     *
     * @param function  the function to differentiate
     * @param direction the direction of differences to use
     * @return a new DividedDifferenceMethod instance
     * @throws NullPointerException if function or direction is null
     */
    public static DividedDifferenceMethod of(Function function, DifferencesDirection direction) {
        return new DividedDifferenceMethod(function, DEFAULT_TARGET_POINT, DEFAULT_STEP_SIZE, direction);
    }

    /**
     * Creates a new builder for constructing DividedDifferenceMethod instances.
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

        return switch (direction) {
            case Forward -> (-3.0 * f.evaluateAt(x) + 4.0 * f.evaluateAt(x + h) - f.evaluateAt(x + 2.0 * h))
                    / (2.0 * h);
            case Central -> (f.evaluateAt(x + h) - f.evaluateAt(x - h)) / (2.0 * h);
            case Backward -> (3.0 * f.evaluateAt(x) - 4.0 * f.evaluateAt(x - h) + f.evaluateAt(x - 2.0 * h))
                    / (2.0 * h);
        };
    }

    @Override
    public double deriveSecond() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;
        double h2 = h * h;

        return switch (direction) {
            case Forward -> (2.0 * f.evaluateAt(x) - 5.0 * f.evaluateAt(x + h)
                    + 4.0 * f.evaluateAt(x + 2.0 * h) - f.evaluateAt(x + 3.0 * h)) / h2;
            case Central -> (f.evaluateAt(x - h) - 2.0 * f.evaluateAt(x) + f.evaluateAt(x + h)) / h2;
            case Backward -> (2.0 * f.evaluateAt(x) - 5.0 * f.evaluateAt(x - h)
                    + 4.0 * f.evaluateAt(x - 2.0 * h) - f.evaluateAt(x - 3.0 * h)) / h2;
        };
    }

    @Override
    public double deriveThird() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;
        double h3 = h * h * h;

        return switch (direction) {
            case Forward -> (-5.0 * f.evaluateAt(x) + 18.0 * f.evaluateAt(x + h)
                    - 24.0 * f.evaluateAt(x + 2.0 * h) + 14.0 * f.evaluateAt(x + 3.0 * h)
                    - 3.0 * f.evaluateAt(x + 4.0 * h)) / (2.0 * h3);
            case Central -> (-f.evaluateAt(x - 2.0 * h) + 2.0 * f.evaluateAt(x - h)
                    - 2.0 * f.evaluateAt(x + h) + f.evaluateAt(x + 2.0 * h)) / (2.0 * h3);
            case Backward -> (5.0 * f.evaluateAt(x) - 18.0 * f.evaluateAt(x - h)
                    + 24.0 * f.evaluateAt(x - 2.0 * h) - 14.0 * f.evaluateAt(x - 3.0 * h)
                    + 3.0 * f.evaluateAt(x - 4.0 * h)) / (2.0 * h3);
        };
    }

    @Override
    public double deriveFourth() {
        double x = targetPoint;
        double h = stepSize;
        Function f = targetFunction;
        double h4 = h * h * h * h;

        return switch (direction) {
            case Forward -> (3.0 * f.evaluateAt(x) - 14.0 * f.evaluateAt(x + h)
                    + 26.0 * f.evaluateAt(x + 2.0 * h) - 24.0 * f.evaluateAt(x + 3.0 * h)
                    + 11.0 * f.evaluateAt(x + 4.0 * h) - 2.0 * f.evaluateAt(x + 5.0 * h)) / h4;
            case Central -> (f.evaluateAt(x - 2.0 * h) - 4.0 * f.evaluateAt(x - h)
                    + 6.0 * f.evaluateAt(x) - 4.0 * f.evaluateAt(x + h)
                    + f.evaluateAt(x + 2.0 * h)) / h4;
            case Backward -> (3.0 * f.evaluateAt(x) - 14.0 * f.evaluateAt(x - h)
                    + 26.0 * f.evaluateAt(x - 2.0 * h) - 24.0 * f.evaluateAt(x - 3.0 * h)
                    + 11.0 * f.evaluateAt(x - 4.0 * h) - 2.0 * f.evaluateAt(x - 5.0 * h)) / h4;
        };
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
     * Gets the direction of finite differences used by this method.
     *
     * @return the differences direction (Forward, Central, or Backward)
     */
    public DifferencesDirection getDirection() {
        return direction;
    }

    /**
     * Creates a new DividedDifferenceMethod with the specified target point.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param targetPoint the new target point
     * @return a new DividedDifferenceMethod instance with the updated target point
     * @throws IllegalArgumentException if targetPoint is not finite
     */
    public DividedDifferenceMethod withTargetPoint(double targetPoint) {
        return new DividedDifferenceMethod(targetFunction, targetPoint, stepSize, direction);
    }

    /**
     * Creates a new DividedDifferenceMethod with the specified step size.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param stepSize the new step size
     * @return a new DividedDifferenceMethod instance with the updated step size
     * @throws IllegalArgumentException if stepSize is not positive
     */
    public DividedDifferenceMethod withStepSize(double stepSize) {
        return new DividedDifferenceMethod(targetFunction, targetPoint, stepSize, direction);
    }

    /**
     * Creates a new DividedDifferenceMethod with the specified direction.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param direction the new differences direction
     * @return a new DividedDifferenceMethod instance with the updated direction
     * @throws NullPointerException if direction is null
     */
    public DividedDifferenceMethod withDirection(DifferencesDirection direction) {
        return new DividedDifferenceMethod(targetFunction, targetPoint, stepSize, direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DividedDifferenceMethod that)) return false;
        return Double.compare(that.targetPoint, targetPoint) == 0 &&
                Double.compare(that.stepSize, stepSize) == 0 &&
                Objects.equals(targetFunction, that.targetFunction) &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetFunction, targetPoint, stepSize, direction);
    }

    @Override
    public String toString() {
        return String.format("DividedDifferenceMethod(direction=%s, x=%.4f, h=%.6f)",
                direction, targetPoint, stepSize);
    }

    /**
     * Builder for constructing {@link DividedDifferenceMethod} instances with custom configuration.
     */
    public static final class Builder {
        private Function targetFunction;
        private double targetPoint = DEFAULT_TARGET_POINT;
        private double stepSize = DEFAULT_STEP_SIZE;
        private DifferencesDirection direction = DifferencesDirection.Central;

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
         * Sets the direction of finite differences.
         * <p>
         * Default: Central (most accurate)
         * </p>
         *
         * @param direction the differences direction
         * @return this builder
         * @throws NullPointerException if direction is null
         */
        public Builder direction(DifferencesDirection direction) {
            this.direction = Objects.requireNonNull(direction, "Direction cannot be null");
            return this;
        }

        /**
         * Builds a new DividedDifferenceMethod instance.
         *
         * @return a new DividedDifferenceMethod
         * @throws NullPointerException if targetFunction was not set
         */
        public DividedDifferenceMethod build() {
            Objects.requireNonNull(targetFunction, "Target function must be set");
            return new DividedDifferenceMethod(targetFunction, targetPoint, stepSize, direction);
        }
    }
}
