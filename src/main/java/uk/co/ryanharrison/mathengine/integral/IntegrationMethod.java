package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.core.Function;

/**
 * Interface for numerical integration methods that estimate the definite integral
 * of a function over a specified interval.
 * <p>
 * Numerical integration approximates the area under a curve by dividing the interval
 * into smaller segments and applying specific mathematical rules. This interface
 * defines the contract that all integration methods must follow.
 * </p>
 *
 * <h2>Mathematical Definition:</h2>
 * <p>
 * Given a function f(x), this interface provides methods to approximate:
 * <br>
 * ∫[a,b] f(x) dx
 * <br>
 * where [a,b] is the integration interval from lower bound a to upper bound b.
 * </p>
 *
 * <h2>Key Concepts:</h2>
 * <ul>
 *     <li><b>Lower Bound (a)</b>: The starting point of the integration interval</li>
 *     <li><b>Upper Bound (b)</b>: The ending point of the integration interval</li>
 *     <li><b>Iterations (n)</b>: The number of subdivisions used in the approximation.
 *         Higher values generally produce more accurate results at the cost of more computation.</li>
 *     <li><b>Target Function f(x)</b>: The mathematical function being integrated</li>
 * </ul>
 *
 * <h2>Available Implementations:</h2>
 * <ul>
 *     <li>{@link TrapeziumIntegrator} - Uses the trapezoidal rule for balanced accuracy</li>
 *     <li>{@link SimpsonIntegrator} - Uses Simpson's rule for higher accuracy with smooth functions</li>
 *     <li>{@link RectangularIntegrator} - Uses rectangular approximation with configurable positioning</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create a function to integrate: f(x) = x^2
 * Function function = new Function("x^2");
 *
 * // Integrate from 0 to 5 using 1000 iterations
 * IntegrationMethod integrator = TrapeziumIntegrator.builder()
 *     .function(function)
 *     .lowerBound(0.0)
 *     .upperBound(5.0)
 *     .iterations(1000)
 *     .build();
 *
 * double result = integrator.integrate();
 * // For f(x) = x^2, the exact integral from 0 to 5 is 125/3 ≈ 41.667
 * }</pre>
 *
 * <h2>Implementation Guidelines:</h2>
 * <p>
 * Implementations should be immutable and thread-safe. All configuration
 * (function, bounds, iterations) should be set during construction, typically
 * using the Builder pattern for clarity.
 * </p>
 *
 * @author Ryan Harrison
 */
public interface IntegrationMethod {
    /**
     * Performs numerical integration of the target function over the specified interval.
     * <p>
     * This method computes an approximation of the definite integral:
     * <br>
     * ∫[lower, upper] f(x) dx
     * <br>
     * The accuracy of the approximation depends on the number of iterations and
     * the specific integration algorithm used.
     * </p>
     *
     * @return the estimated value of the definite integral
     * @throws ArithmeticException if the function evaluation produces non-finite values
     *                             (NaN or infinity) during integration
     */
    double integrate();

    /**
     * Returns the number of iterations (subdivisions) used in the integration approximation.
     * <p>
     * Higher iteration counts generally produce more accurate results but require
     * more function evaluations and computation time. The relationship between
     * iterations and accuracy depends on the specific integration method.
     * </p>
     *
     * @return the number of iterations, always positive
     */
    int getIterations();

    /**
     * Returns the lower bound of the integration interval.
     * <p>
     * This is the starting point 'a' in the definite integral ∫[a,b] f(x) dx.
     * </p>
     *
     * @return the lower bound of the integration interval
     */
    double getLowerBound();

    /**
     * Returns the upper bound of the integration interval.
     * <p>
     * This is the ending point 'b' in the definite integral ∫[a,b] f(x) dx.
     * </p>
     *
     * @return the upper bound of the integration interval
     */
    double getUpperBound();

    /**
     * Returns the function being integrated.
     * <p>
     * This is the mathematical function f(x) in the definite integral ∫[a,b] f(x) dx.
     * </p>
     *
     * @return the target function for integration
     */
    Function getTargetFunction();
}
