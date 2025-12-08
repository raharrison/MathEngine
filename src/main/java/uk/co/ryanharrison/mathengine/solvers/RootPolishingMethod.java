package uk.co.ryanharrison.mathengine.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface for polishing (iterative refinement) root-finding methods.
 * <p>
 * Polishing methods start with an initial guess and iteratively refine it until it converges
 * to a root. These methods typically have faster convergence than bracketing methods but may
 * diverge if the initial guess is poor.
 * </p>
 *
 * <h2>Convergence Properties:</h2>
 * <ul>
 *     <li><b>Newton-Raphson</b>: Quadratic convergence near root (doubles correct digits per iteration)</li>
 *     <li><b>Hybrid methods</b>: Combine fast convergence with guaranteed bounds</li>
 * </ul>
 *
 * <h2>Advantages over Bracketing:</h2>
 * <ul>
 *     <li>Faster convergence (fewer iterations required)</li>
 *     <li>Can handle complex roots (when extended to complex analysis)</li>
 *     <li>Efficient for smooth, well-behaved functions</li>
 * </ul>
 *
 * <h2>Disadvantages:</h2>
 * <ul>
 *     <li>May diverge with poor initial guess</li>
 *     <li>Requires derivative (for Newton-based methods)</li>
 *     <li>Can get stuck in cycles or overflow</li>
 *     <li>No guaranteed convergence</li>
 * </ul>
 *
 * <h2>When to Use:</h2>
 * <ul>
 *     <li>You have a good initial guess (from domain knowledge or previous computation)</li>
 *     <li>The function is smooth and differentiable</li>
 *     <li>You need high precision quickly</li>
 *     <li>You can afford the risk of divergence</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Find the square root of 10 (root of x^2 - 10 = 0)
 * Function f = new Function("x^2 - 10");
 *
 * RootPolishingMethod solver = NewtonRaphsonSolver.builder()
 *     .targetFunction(f)
 *     .initialGuess(3.0)  // Close to √10 ≈ 3.162
 *     .tolerance(1e-10)
 *     .build();
 *
 * double root = solver.solve();  // Converges to 3.162277660168379 in ~4 iterations
 * }</pre>
 *
 * @see EquationSolver
 * @see NewtonRaphsonSolver
 */
public interface RootPolishingMethod extends EquationSolver {

    /**
     * Default initial guess for root polishing.
     * <p>
     * This arbitrary value is used when no better guess is available. For best results,
     * provide a domain-specific initial guess.
     * </p>
     */
    double DEFAULT_INITIAL_GUESS = 1.0;

    /**
     * Returns the initial guess for the root.
     *
     * @return the initial guess
     */
    double getInitialGuess();

    /**
     * Finds all roots of the target function in the specified interval.
     * <p>
     * This method subdivides the interval, identifies candidate sub-intervals that may contain
     * roots, and uses the midpoint of each as an initial guess for the polishing algorithm.
     * </p>
     *
     * <h3>Algorithm:</h3>
     * <ol>
     *     <li>Subdivide [lower, upper] into sub-intervals</li>
     *     <li>For each sub-interval with a sign change, use its midpoint as initial guess</li>
     *     <li>Apply polishing algorithm to find the root</li>
     *     <li>Remove duplicates (roots found multiple times from different guesses)</li>
     *     <li>Return unique roots in ascending order</li>
     * </ol>
     *
     * <h3>Duplicate Detection:</h3>
     * <p>
     * Multiple initial guesses may converge to the same root. This method filters duplicates
     * by considering two roots equal if they differ by less than the tolerance.
     * </p>
     *
     * <h3>Important Notes:</h3>
     * <ul>
     *     <li>May miss roots that don't have a sign change in any subdivision</li>
     *     <li>May fail to converge for some initial guesses (divergence)</li>
     *     <li>More subdivisions = higher chance of finding roots but more computation</li>
     *     <li>Sensitive to function smoothness and derivative behavior</li>
     * </ul>
     *
     * @param lower the lower bound of the search interval
     * @param upper the upper bound of the search interval
     * @return a list of unique estimated roots in ascending order (may be empty)
     * @throws IllegalArgumentException if lower >= upper
     */
    default List<Double> solveAll(double lower, double upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException(
                    String.format("Lower bound must be less than upper bound, got: [%.6g, %.6g]", lower, upper));
        }

        // Find candidate intervals that may contain roots
        List<RootInterval> brackets = EquationSolver.findBrackets(
                getTargetFunction(), lower, upper, getIterations());

        Set<Double> uniqueRoots = new HashSet<>();

        // For each candidate interval, use the midpoint as initial guess
        for (RootInterval interval : brackets) {
            double guess = interval.midpoint();

            try {
                // Create a new solver with this initial guess
                EquationSolver guessSolver = createSolverForGuess(guess);
                double root = guessSolver.solve();

                // Add if not already present (within tolerance)
                if (!containsRoot(uniqueRoots, root, getTolerance())) {
                    uniqueRoots.add(root);
                }
            } catch (ConvergenceException | DivergenceException e) {
                // Skip this guess if it doesn't converge
                // This is expected for polishing methods with poor guesses
            }
        }

        // Convert to sorted list
        List<Double> roots = new ArrayList<>(uniqueRoots);
        roots.sort(Double::compareTo);
        return roots;
    }

    /**
     * Creates a new solver instance configured with the specified initial guess.
     * <p>
     * This method must be implemented by concrete solver classes to support {@link #solveAll(double, double)}.
     * It should create a new solver with the same configuration as this solver but with the
     * specified initial guess.
     * </p>
     *
     * @param initialGuess the initial guess for the new solver
     * @return a new solver configured with the guess
     */
    EquationSolver createSolverForGuess(double initialGuess);

    /**
     * Checks if the set of roots already contains the specified root within tolerance.
     * <p>
     * Two roots are considered equal if |r1 - r2| < tolerance.
     * </p>
     *
     * @param roots     the set of existing roots
     * @param newRoot   the root to check
     * @param tolerance the comparison tolerance
     * @return true if an equivalent root already exists, false otherwise
     */
    private static boolean containsRoot(Set<Double> roots, double newRoot, double tolerance) {
        for (double existingRoot : roots) {
            if (Math.abs(existingRoot - newRoot) < tolerance) {
                return true;
            }
        }
        return false;
    }
}
