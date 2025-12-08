package uk.co.ryanharrison.mathengine.differential;

/**
 * Represents the three commonly used forms of finite differences for numerically estimating derivatives.
 * <p>
 * Finite difference methods approximate derivatives by evaluating the function at different points
 * relative to the target point. The choice of direction affects accuracy, computational cost, and
 * applicability to boundary conditions.
 * </p>
 *
 * <h2>Accuracy Comparison:</h2>
 * <ul>
 *     <li><b>Forward</b>: Error O(h) - First order accurate</li>
 *     <li><b>Backward</b>: Error O(h) - First order accurate</li>
 *     <li><b>Central</b>: Error O(h²) - Second order accurate (most accurate)</li>
 * </ul>
 *
 * <h2>When to Use Each Direction:</h2>
 * <ul>
 *     <li><b>Forward</b>: When function values at x-h are unavailable (e.g., at left boundary)</li>
 *     <li><b>Backward</b>: When function values at x+h are unavailable (e.g., at right boundary)</li>
 *     <li><b>Central</b>: Default choice for interior points due to superior accuracy</li>
 * </ul>
 *
 * @see DividedDifferenceMethod
 * @see RichardsonExtrapolationMethod
 */
public enum DifferencesDirection {
    /**
     * Forward differences: uses points at x, x+h, x+2h, etc.
     * <p>
     * First derivative formula: f'(x) ≈ [-3f(x) + 4f(x+h) - f(x+2h)] / (2h)
     * </p>
     * <p>
     * Error term: O(h²) for the 3-point formula shown above
     * </p>
     */
    Forward,

    /**
     * Central differences: uses points symmetrically around x (x-h, x, x+h, etc.)
     * <p>
     * First derivative formula: f'(x) ≈ [f(x+h) - f(x-h)] / (2h)
     * </p>
     * <p>
     * Error term: O(h²) - provides the best accuracy among standard difference formulas
     * </p>
     * <p>
     * This is the recommended default choice for most applications.
     * </p>
     */
    Central,

    /**
     * Backward differences: uses points at x, x-h, x-2h, etc.
     * <p>
     * First derivative formula: f'(x) ≈ [3f(x) - 4f(x-h) + f(x-2h)] / (2h)
     * </p>
     * <p>
     * Error term: O(h²) for the 3-point formula shown above
     * </p>
     */
    Backward
}
