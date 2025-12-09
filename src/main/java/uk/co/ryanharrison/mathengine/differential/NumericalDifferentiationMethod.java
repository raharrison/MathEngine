package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.core.Function;

/**
 * Interface for numerically estimating derivatives of a function using finite difference approximations.
 * <p>
 * Finite difference methods approximate derivatives by evaluating the function at points near the
 * target point and computing weighted combinations of these values. The accuracy depends on:
 * </p>
 * <ul>
 *     <li><b>h (step size)</b>: smaller values generally increase accuracy but may introduce rounding errors</li>
 *     <li><b>Method type</b>: different formulas have different orders of accuracy (error terms)</li>
 *     <li><b>Function smoothness</b>: discontinuous or rapidly varying functions require smaller step sizes</li>
 * </ul>
 * <p>
 * Implementations provide methods to compute derivatives of orders 1 through 4 at a specified point.
 * </p>
 *
 * <h2>Common Implementations:</h2>
 * <ul>
 *     <li>{@link DividedDifferenceMethod} - Standard forward/central/backward differences</li>
 *     <li>{@link ExtendedCentralDifferenceMethod} - Higher-order central differences for improved accuracy</li>
 *     <li>{@link RichardsonExtrapolationMethod} - Enhances accuracy using Richardson extrapolation</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create a function f(x) = x^2
 * Function f = new Function("x^2");
 *
 * // Create a differentiation method
 * NumericalDifferentiationMethod method = DividedDifferenceMethod.builder()
 *     .targetFunction(f)
 *     .targetPoint(3.0)
 *     .stepSize(0.001)
 *     .direction(DifferencesDirection.Central)
 *     .build();
 *
 * // Compute first derivative at x = 3.0 (should be approximately 6.0)
 * double derivative = method.deriveFirst();
 * }</pre>
 *
 * @see DividedDifferenceMethod
 * @see ExtendedCentralDifferenceMethod
 * @see RichardsonExtrapolationMethod
 */
public interface NumericalDifferentiationMethod {
    /**
     * Default step size for finite difference approximations.
     * <p>
     * This value provides a reasonable balance between accuracy and rounding errors
     * for most functions. Adjust based on the specific function characteristics.
     * </p>
     */
    double DEFAULT_STEP_SIZE = 0.01;

    /**
     * Default target point for differentiation.
     */
    double DEFAULT_TARGET_POINT = 1.0;

    /**
     * Computes the first derivative of the target function at the target point.
     * <p>
     * The first derivative represents the instantaneous rate of change of the function.
     * Mathematically: f'(x) = lim(h→0) [f(x+h) - f(x)] / h
     * </p>
     *
     * @return the estimated value of the first derivative f'(x) at the target point
     */
    double deriveFirst();

    /**
     * Computes the second derivative of the target function at the target point.
     * <p>
     * The second derivative represents the rate of change of the first derivative (curvature).
     * Mathematically: f''(x) = lim(h→0) [f'(x+h) - f'(x)] / h
     * </p>
     *
     * @return the estimated value of the second derivative f''(x) at the target point
     */
    double deriveSecond();

    /**
     * Computes the third derivative of the target function at the target point.
     * <p>
     * The third derivative represents the rate of change of the second derivative.
     * Higher-order derivatives require smaller step sizes for accurate estimation.
     * </p>
     *
     * @return the estimated value of the third derivative f'''(x) at the target point
     */
    double deriveThird();

    /**
     * Computes the fourth derivative of the target function at the target point.
     * <p>
     * The fourth derivative represents the rate of change of the third derivative.
     * This is the highest-order derivative provided by most finite difference methods
     * due to increasing error accumulation for higher orders.
     * </p>
     *
     * @return the estimated value of the fourth derivative f''''(x) at the target point
     */
    double deriveFourth();

    /**
     * Gets the step size (h) used in finite difference approximations.
     * <p>
     * The step size should be sufficiently small to increase accuracy but large enough
     * to prevent rounding errors. Typical values range from 1e-2 to 1e-8 depending on
     * the function and required precision.
     * </p>
     *
     * @return the step size used for finite difference calculations
     */
    double getStepSize();

    /**
     * Gets the target function being differentiated.
     *
     * @return the function f(x) for which derivatives are computed
     */
    Function getTargetFunction();

    /**
     * Gets the point at which derivatives are evaluated.
     *
     * @return the x-coordinate where derivatives are computed
     */
    double getTargetPoint();
}
