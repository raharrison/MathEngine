package uk.co.ryanharrison.mathengine.solvers;

/**
 * Enumeration representing different methods for obtaining derivative values in Newton-based solvers.
 * <p>
 * Newton-Raphson and hybrid Newton methods require the derivative f'(x) of the target function.
 * This enum specifies how those derivative values should be computed.
 * </p>
 *
 * <h2>Method Comparison:</h2>
 * <table border="1">
 *     <tr>
 *         <th>Method</th>
 *         <th>Accuracy</th>
 *         <th>Performance</th>
 *         <th>Use When</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #Numerical}</td>
 *         <td>Good (O(h⁴))</td>
 *         <td>Moderate</td>
 *         <td>Default choice, no derivative available</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #Symbolic}</td>
 *         <td>Exact</td>
 *         <td>Fast</td>
 *         <td>Analytical derivative can be computed</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #Predefined}</td>
 *         <td>Exact</td>
 *         <td>Fastest</td>
 *         <td>Derivative function is known a priori</td>
 *     </tr>
 * </table>
 *
 * @see NewtonRaphsonSolver
 * @see NewtonBisectionSolver
 */
public enum DifferentiationMethod {

    /**
     * Compute derivatives using numerical differentiation.
     * <p>
     * Uses the {@link uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod}
     * which provides O(h⁴) accuracy through a five-point stencil.
     * </p>
     * <p>
     * This is the default method and works for any function, but is slower than symbolic or
     * predefined derivatives.
     * </p>
     *
     * <h3>Advantages:</h3>
     * <ul>
     *     <li>Works for any differentiable function</li>
     *     <li>No manual derivative specification required</li>
     *     <li>Good accuracy (better than simple finite differences)</li>
     * </ul>
     *
     * <h3>Disadvantages:</h3>
     * <ul>
     *     <li>Slower than exact derivatives (requires multiple function evaluations)</li>
     *     <li>May have numerical precision issues near discontinuities</li>
     * </ul>
     */
    Numerical,

    /**
     * Compute derivatives using symbolic differentiation.
     * <p>
     * Uses the {@link uk.co.ryanharrison.mathengine.differential.symbolic.Differentiator}
     * to analytically differentiate the target function's expression tree.
     * </p>
     * <p>
     * This provides exact derivatives for functions that can be symbolically differentiated,
     * and is faster than numerical differentiation.
     * </p>
     *
     * <h3>Advantages:</h3>
     * <ul>
     *     <li>Exact derivatives (no numerical approximation error)</li>
     *     <li>Faster than numerical differentiation</li>
     *     <li>Automatically computed from target function</li>
     * </ul>
     *
     * <h3>Disadvantages:</h3>
     * <ul>
     *     <li>Only works for functions with symbolic expressions</li>
     *     <li>May produce complex derivative expressions</li>
     * </ul>
     */
    Symbolic,

    /**
     * Use a user-provided derivative function.
     * <p>
     * The user supplies a {@link uk.co.ryanharrison.mathengine.Function} representing
     * the derivative of the target function.
     * </p>
     * <p>
     * This is the fastest option as it uses pre-computed derivatives, but requires the
     * user to provide the correct derivative function.
     * </p>
     *
     * <h3>Advantages:</h3>
     * <ul>
     *     <li>Fastest evaluation (single function call per iteration)</li>
     *     <li>Exact derivatives</li>
     *     <li>Full control over derivative implementation</li>
     * </ul>
     *
     * <h3>Disadvantages:</h3>
     * <ul>
     *     <li>Requires manual specification of derivative</li>
     *     <li>User responsible for correctness</li>
     * </ul>
     */
    Predefined
}
