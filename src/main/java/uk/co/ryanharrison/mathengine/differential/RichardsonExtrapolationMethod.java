package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

import java.util.Objects;

/**
 * Immutable implementation of {@link NumericalDifferentiationMethod} using Richardson extrapolation.
 * <p>
 * Richardson extrapolation is a powerful technique for improving the accuracy of numerical differentiation
 * by combining estimates computed with different step sizes. Given two derivative estimates D(h) and D(h/2)
 * computed with step sizes h and h/2, the extrapolated value D_ext eliminates the leading error term.
 * </p>
 *
 * <h2>Mathematical Principle:</h2>
 * <p>
 * For a method with error term O(h²), the extrapolated formula is:
 * <br>
 * D_ext = (4·D(h/2) - D(h)) / 3
 * </p>
 * <p>
 * This eliminates the h² term and produces a result with error O(h⁴), effectively improving
 * accuracy by two orders of magnitude without reducing the step size further.
 * </p>
 *
 * <h2>Accuracy Improvement:</h2>
 * <ul>
 *     <li>Base method (central differences): O(h²) error</li>
 *     <li>After Richardson extrapolation: O(h⁴) error</li>
 *     <li>Same accuracy as extended central differences but using standard formulas</li>
 * </ul>
 *
 * <h2>Computational Cost:</h2>
 * <p>
 * Richardson extrapolation requires approximately twice the function evaluations compared to
 * the base method (one evaluation at step h and another at h/2). However, it achieves higher
 * accuracy than simply reducing the step size, making it more efficient than using h/2 alone.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Create function f(x) = sin(x)
 * Function f = new Function("sin(x)");
 *
 * // Using builder with central differences (recommended)
 * RichardsonExtrapolationMethod method = RichardsonExtrapolationMethod.builder()
 *     .targetFunction(f)
 *     .targetPoint(Math.PI / 4)
 *     .stepSize(0.01)
 *     .direction(DifferencesDirection.Central)
 *     .build();
 *
 * // Compute derivative with improved accuracy
 * double derivative = method.deriveFirst(); // ≈ 0.7071 (cos(π/4))
 *
 * // Using static factory with defaults (central direction)
 * RichardsonExtrapolationMethod defaultMethod = RichardsonExtrapolationMethod.of(f);
 *
 * // Create modified copy with different parameters
 * RichardsonExtrapolationMethod atX2 = method.withTargetPoint(2.0);
 * RichardsonExtrapolationMethod forward = method.withDirection(DifferencesDirection.Forward);
 * }</pre>
 *
 * <h2>Direction Support:</h2>
 * <p>
 * Richardson extrapolation can be applied to any finite difference direction:
 * </p>
 * <ul>
 *     <li><b>Central</b> (recommended): Best accuracy, produces O(h⁴) results</li>
 *     <li><b>Forward</b>: Useful for boundary conditions, improves to O(h⁴)</li>
 *     <li><b>Backward</b>: Useful for boundary conditions, improves to O(h⁴)</li>
 * </ul>
 *
 * <h2>When to Use:</h2>
 * <ul>
 *     <li>When higher accuracy is needed without significant code complexity</li>
 *     <li>When you can afford roughly 2x the function evaluations</li>
 *     <li>When the function is smooth enough to justify extrapolation</li>
 *     <li>As an alternative to extended central differences with similar accuracy</li>
 * </ul>
 *
 * @see DividedDifferenceMethod
 * @see ExtendedCentralDifferenceMethod
 */
public final class RichardsonExtrapolationMethod implements NumericalDifferentiationMethod {
    private final DividedDifferenceMethod baseMethod;
    private final DividedDifferenceMethod halfStepMethod;

    /**
     * Private constructor - use builder() or static factory methods.
     *
     * @param targetFunction the function to differentiate
     * @param targetPoint    the point at which to evaluate derivatives
     * @param stepSize       the step size (h) for finite differences
     * @param direction      the direction of differences to use
     */
    private RichardsonExtrapolationMethod(Function targetFunction, double targetPoint,
                                          double stepSize, DifferencesDirection direction) {
        Objects.requireNonNull(targetFunction, "Target function cannot be null");
        Objects.requireNonNull(direction, "Direction cannot be null");

        if (stepSize <= 0.0) {
            throw new IllegalArgumentException("Step size must be positive, got: " + stepSize);
        }
        if (!Double.isFinite(targetPoint)) {
            throw new IllegalArgumentException("Target point must be finite, got: " + targetPoint);
        }

        // Create base method with step size h
        this.baseMethod = DividedDifferenceMethod.builder()
                .targetFunction(targetFunction)
                .targetPoint(targetPoint)
                .stepSize(stepSize)
                .direction(direction)
                .build();

        // Create method with half step size (h/2) for extrapolation
        this.halfStepMethod = DividedDifferenceMethod.builder()
                .targetFunction(targetFunction)
                .targetPoint(targetPoint)
                .stepSize(stepSize / 2.0)
                .direction(direction)
                .build();
    }

    /**
     * Creates a new RichardsonExtrapolationMethod with the specified function and default settings.
     * <p>
     * Defaults: target point = 1.0, step size = 0.01, direction = Central
     * </p>
     *
     * @param function the function to differentiate
     * @return a new RichardsonExtrapolationMethod instance
     * @throws NullPointerException if function is null
     */
    public static RichardsonExtrapolationMethod of(Function function) {
        return new RichardsonExtrapolationMethod(function, DEFAULT_TARGET_POINT,
                DEFAULT_STEP_SIZE, DifferencesDirection.Central);
    }

    /**
     * Creates a new RichardsonExtrapolationMethod with the specified function and direction.
     * <p>
     * Defaults: target point = 1.0, step size = 0.01
     * </p>
     *
     * @param function  the function to differentiate
     * @param direction the direction of differences to use
     * @return a new RichardsonExtrapolationMethod instance
     * @throws NullPointerException if function or direction is null
     */
    public static RichardsonExtrapolationMethod of(Function function, DifferencesDirection direction) {
        return new RichardsonExtrapolationMethod(function, DEFAULT_TARGET_POINT, DEFAULT_STEP_SIZE, direction);
    }

    /**
     * Creates a new RichardsonExtrapolationMethod with the specified function, direction, and step size.
     * <p>
     * Default: target point = 1.0
     * </p>
     *
     * @param function  the function to differentiate
     * @param direction the direction of differences to use
     * @param stepSize  the step size (must be positive)
     * @return a new RichardsonExtrapolationMethod instance
     * @throws NullPointerException     if function or direction is null
     * @throws IllegalArgumentException if stepSize is not positive
     */
    public static RichardsonExtrapolationMethod of(Function function, DifferencesDirection direction, double stepSize) {
        return new RichardsonExtrapolationMethod(function, DEFAULT_TARGET_POINT, stepSize, direction);
    }

    /**
     * Creates a new builder for constructing RichardsonExtrapolationMethod instances.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public double deriveFirst() {
        double d1 = baseMethod.deriveFirst();       // Derivative at step h
        double d2 = halfStepMethod.deriveFirst();   // Derivative at step h/2
        return (4.0 * d2 - d1) / 3.0;               // Richardson extrapolation
    }

    @Override
    public double deriveSecond() {
        double d1 = baseMethod.deriveSecond();
        double d2 = halfStepMethod.deriveSecond();
        return (4.0 * d2 - d1) / 3.0;
    }

    @Override
    public double deriveThird() {
        double d1 = baseMethod.deriveThird();
        double d2 = halfStepMethod.deriveThird();
        return (4.0 * d2 - d1) / 3.0;
    }

    @Override
    public double deriveFourth() {
        double d1 = baseMethod.deriveFourth();
        double d2 = halfStepMethod.deriveFourth();
        return (4.0 * d2 - d1) / 3.0;
    }

    @Override
    public double getStepSize() {
        return baseMethod.getStepSize();
    }

    @Override
    public Function getTargetFunction() {
        return baseMethod.getTargetFunction();
    }

    @Override
    public double getTargetPoint() {
        return baseMethod.getTargetPoint();
    }

    /**
     * Gets the direction of finite differences used by this method.
     *
     * @return the differences direction (Forward, Central, or Backward)
     */
    public DifferencesDirection getDirection() {
        return baseMethod.getDirection();
    }

    /**
     * Creates a new RichardsonExtrapolationMethod with the specified target point.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param targetPoint the new target point
     * @return a new RichardsonExtrapolationMethod instance with the updated target point
     * @throws IllegalArgumentException if targetPoint is not finite
     */
    public RichardsonExtrapolationMethod withTargetPoint(double targetPoint) {
        return new RichardsonExtrapolationMethod(getTargetFunction(), targetPoint, getStepSize(), getDirection());
    }

    /**
     * Creates a new RichardsonExtrapolationMethod with the specified step size.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param stepSize the new step size
     * @return a new RichardsonExtrapolationMethod instance with the updated step size
     * @throws IllegalArgumentException if stepSize is not positive
     */
    public RichardsonExtrapolationMethod withStepSize(double stepSize) {
        return new RichardsonExtrapolationMethod(getTargetFunction(), getTargetPoint(), stepSize, getDirection());
    }

    /**
     * Creates a new RichardsonExtrapolationMethod with the specified direction.
     * <p>
     * All other properties remain unchanged.
     * </p>
     *
     * @param direction the new differences direction
     * @return a new RichardsonExtrapolationMethod instance with the updated direction
     * @throws NullPointerException if direction is null
     */
    public RichardsonExtrapolationMethod withDirection(DifferencesDirection direction) {
        return new RichardsonExtrapolationMethod(getTargetFunction(), getTargetPoint(), getStepSize(), direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RichardsonExtrapolationMethod that)) return false;
        return Objects.equals(baseMethod, that.baseMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseMethod);
    }

    @Override
    public String toString() {
        return String.format("RichardsonExtrapolationMethod(direction=%s, x=%.4f, h=%.6f)",
                getDirection(), getTargetPoint(), getStepSize());
    }

    /**
     * Builder for constructing {@link RichardsonExtrapolationMethod} instances with custom configuration.
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
         * Builds a new RichardsonExtrapolationMethod instance.
         *
         * @return a new RichardsonExtrapolationMethod
         * @throws NullPointerException if targetFunction was not set
         */
        public RichardsonExtrapolationMethod build() {
            Objects.requireNonNull(targetFunction, "Target function must be set");
            return new RichardsonExtrapolationMethod(targetFunction, targetPoint, stepSize, direction);
        }
    }
}
