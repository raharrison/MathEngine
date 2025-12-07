package uk.co.ryanharrison.mathengine.integral;

/**
 * Enumeration defining the position for rectangle sampling in {@link RectangularIntegrator}.
 * <p>
 * The rectangular integration method divides the integration interval into subintervals
 * and approximates the area using rectangles. The height of each rectangle is determined
 * by evaluating the function at a specific point within each subinterval. This enum
 * controls where that evaluation point is located.
 * </p>
 *
 * <h2>Mathematical Impact:</h2>
 * <p>
 * The choice of rectangle position significantly affects accuracy and bias:
 * </p>
 * <ul>
 *     <li><b>{@link #LEFT}</b>: Uses the left endpoint of each subinterval. Underestimates
 *         for increasing functions, overestimates for decreasing functions.</li>
 *     <li><b>{@link #MIDPOINT}</b>: Uses the center of each subinterval. Generally provides
 *         the best accuracy for rectangular methods and is unbiased for linear functions.</li>
 *     <li><b>{@link #RIGHT}</b>: Uses the right endpoint of each subinterval. Overestimates
 *         for increasing functions, underestimates for decreasing functions.</li>
 * </ul>
 *
 * <h2>Accuracy Comparison:</h2>
 * <p>
 * For the same number of subdivisions, midpoint rule typically provides:
 * </p>
 * <ul>
 *     <li>Better accuracy than left or right endpoint rules</li>
 *     <li>O(h²) error convergence (same order as trapezoidal rule)</li>
 *     <li>Opposite bias compared to trapezoidal rule for convex/concave functions</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * Function f = new Function("x^2");
 *
 * // Using left endpoint - underestimates for x^2 on [0, 1]
 * RectangularIntegrator left = RectangularIntegrator.of(f, 0.0, 1.0, RectanglePosition.LEFT);
 *
 * // Using midpoint - best accuracy
 * RectangularIntegrator mid = RectangularIntegrator.of(f, 0.0, 1.0, RectanglePosition.MIDPOINT);
 *
 * // Using right endpoint - overestimates for x^2 on [0, 1]
 * RectangularIntegrator right = RectangularIntegrator.of(f, 0.0, 1.0, RectanglePosition.RIGHT);
 * }</pre>
 *
 * @author Ryan Harrison
 * @see RectangularIntegrator
 */
public enum RectanglePosition {
    /**
     * Evaluate the function at the left endpoint of each subinterval.
     * <p>
     * For subinterval [xᵢ, xᵢ₊₁], uses f(xᵢ) as the rectangle height.
     * </p>
     * <p>
     * <b>Error characteristics</b>: Systematic underestimation for monotonically
     * increasing functions, overestimation for decreasing functions.
     * </p>
     */
    LEFT,

    /**
     * Evaluate the function at the midpoint of each subinterval (default).
     * <p>
     * For subinterval [xᵢ, xᵢ₊₁], uses f((xᵢ + xᵢ₊₁)/2) as the rectangle height.
     * </p>
     * <p>
     * <b>Error characteristics</b>: Generally the most accurate rectangular method.
     * Exact for linear functions. Error is O(h²) where h is the step size.
     * </p>
     * <p>
     * <b>Recommended</b> as the default choice for rectangular integration.
     * </p>
     */
    MIDPOINT,

    /**
     * Evaluate the function at the right endpoint of each subinterval.
     * <p>
     * For subinterval [xᵢ, xᵢ₊₁], uses f(xᵢ₊₁) as the rectangle height.
     * </p>
     * <p>
     * <b>Error characteristics</b>: Systematic overestimation for monotonically
     * increasing functions, underestimation for decreasing functions.
     * </p>
     */
    RIGHT;

    /**
     * Returns the fractional offset within a subinterval for this position.
     * <p>
     * This value is used internally to compute the x-coordinate at which
     * to evaluate the function within each subinterval.
     * </p>
     *
     * @return 0.0 for LEFT, 0.5 for MIDPOINT, 1.0 for RIGHT
     */
    double getOffset() {
        return switch (this) {
            case LEFT -> 0.0;
            case MIDPOINT -> 0.5;
            case RIGHT -> 1.0;
        };
    }
}
