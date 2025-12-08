package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for bracketing root-finding methods.
 * <p>
 * Bracketing methods require an interval [a, b] where f(a) and f(b) have opposite signs,
 * guaranteeing that at least one root exists in the interval (by the Intermediate Value Theorem
 * for continuous functions).
 * </p>
 *
 * <h2>Key Property:</h2>
 * <p>
 * For a continuous function f(x) on interval [a, b], if f(a) × f(b) < 0, then there exists
 * at least one c ∈ (a, b) such that f(c) = 0.
 * </p>
 *
 * <h2>Guaranteed Convergence:</h2>
 * <p>
 * Bracketing methods are guaranteed to converge if:
 * </p>
 * <ul>
 *     <li>The function is continuous on [a, b]</li>
 *     <li>f(a) and f(b) have opposite signs (f(a) × f(b) < 0)</li>
 * </ul>
 *
 * <h2>Available Algorithms:</h2>
 * <ul>
 *     <li><b>{@link BisectionSolver}</b>: Simple, reliable, linear convergence O(log n)</li>
 *     <li><b>{@link BrentSolver}</b>: Adaptive hybrid method, super-linear convergence</li>
 *     <li><b>{@link NewtonBisectionSolver}</b>: Combines Newton-Raphson with bisection fallback</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Find roots of x^3 - 6x^2 + 11x - 6 = (x-1)(x-2)(x-3) in [0, 4]
 * Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
 *
 * RootBracketingMethod solver = BrentSolver.builder()
 *     .targetFunction(f)
 *     .lowerBound(0.0)
 *     .upperBound(4.0)
 *     .tolerance(1e-8)
 *     .build();
 *
 * List<Double> roots = solver.solveAll();
 * // Returns [1.0, 2.0, 3.0] (within tolerance)
 * }</pre>
 *
 * @see EquationSolver
 * @see BisectionSolver
 * @see BrentSolver
 */
public interface RootBracketingMethod extends EquationSolver {

    /**
     * Returns the lower bound of the bracket.
     *
     * @return the lower bound
     */
    double getLowerBound();

    /**
     * Returns the upper bound of the bracket.
     *
     * @return the upper bound
     */
    double getUpperBound();

    /**
     * Finds all roots of the target function in the solver's configured interval.
     * <p>
     * This method subdivides the interval [lowerBound, upperBound] into smaller sub-intervals,
     * identifies which ones may contain roots (by checking for sign changes), and applies the
     * root-finding algorithm to each candidate interval.
     * </p>
     *
     * <h3>Algorithm:</h3>
     * <ol>
     *     <li>Subdivide [lowerBound, upperBound] into equal sub-intervals</li>
     *     <li>For each sub-interval where f(a) × f(b) < 0, find the root using {@link #solve()}</li>
     *     <li>Return all found roots in ascending order</li>
     * </ol>
     *
     * <h3>Important Notes:</h3>
     * <ul>
     *     <li>The number of subdivisions affects how many roots are found</li>
     *     <li>Even numbers of roots in a subdivision may be missed (signs cancel out)</li>
     *     <li>Roots at exact subdivision boundaries may be missed</li>
     *     <li>More subdivisions = higher chance of finding all roots but more computation</li>
     * </ul>
     *
     * @return a list of estimated roots in ascending order (maybe empty if no roots found)
     */
    default List<Double> solveAll() {
        // Find candidate intervals that may contain roots
        List<RootInterval> brackets = EquationSolver.findBrackets(
                getTargetFunction(), getLowerBound(), getUpperBound(), getIterations());

        List<Double> roots = new ArrayList<>();

        // For each candidate interval, create a new solver and find the root
        for (RootInterval interval : brackets) {
            try {
                // Create a new solver with the specific interval bounds
                EquationSolver intervalSolver = createSolverForInterval(interval);
                double root = intervalSolver.solve();

                // Check if this root is a duplicate (within tolerance)
                boolean isDuplicate = false;
                for (Double existingRoot : roots) {
                    if (Math.abs(root - existingRoot) < getTolerance() * 2.0) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    roots.add(root);
                }
            } catch (ConvergenceException | DivergenceException e) {
                // Skip this interval if convergence fails
                // This can happen if the sign change is due to discontinuity
            }
        }

        return roots;
    }

    /**
     * Creates a new solver instance configured for the specified interval.
     * <p>
     * This method must be implemented by concrete solver classes to support {@link #solveAll()}.
     * It should create a new solver with the same configuration as this solver but with the
     * specified interval bounds.
     * </p>
     *
     * @param interval the interval to solve in
     * @return a new solver configured for the interval
     */
    EquationSolver createSolverForInterval(RootInterval interval);

    /**
     * Validates that the bounds bracket a root and throws an exception if they don't.
     *
     * @param function the function being solved
     * @param lower    the lower bound
     * @param upper    the upper bound
     * @throws InvalidBoundsException if the bounds don't bracket a root
     */
    static void validateBounds(Function function, double lower, double upper) {
        double fLower = function.evaluateAt(lower);
        double fUpper = function.evaluateAt(upper);

        if (fLower * fUpper >= 0.0) {
            throw new InvalidBoundsException(lower, upper, fLower, fUpper);
        }
    }
}
