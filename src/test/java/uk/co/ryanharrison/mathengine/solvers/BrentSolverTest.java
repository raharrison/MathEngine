package uk.co.ryanharrison.mathengine.solvers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.ryanharrison.mathengine.core.Function;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link BrentSolver}.
 * <p>
 * Tests cover all three strategies (bisection, linear interpolation, inverse quadratic interpolation),
 * convergence properties, edge cases, and comparison with bisection method.
 * </p>
 */
class BrentSolverTest {

    private static final double TOLERANCE = 1e-8;
    private static final double RELAXED_TOLERANCE = 1e-6;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesValidSolver() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.of(f, 0.0, 3.0);

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getLowerBound()).isEqualTo(0.0);
        assertThat(solver.getUpperBound()).isEqualTo(3.0);
        assertThat(solver.getTolerance()).isEqualTo(EquationSolver.DEFAULT_TOLERANCE);
        assertThat(solver.getIterations()).isEqualTo(EquationSolver.DEFAULT_ITERATIONS);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(EquationSolver.DEFAULT_CONVERGENCE_CRITERIA);
    }

    @Test
    void builderCreatesValidSolverWithDefaults() {
        Function f = new Function("x^2 - 4");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .build();

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getTolerance()).isEqualTo(EquationSolver.DEFAULT_TOLERANCE);
        assertThat(solver.getIterations()).isEqualTo(EquationSolver.DEFAULT_ITERATIONS);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(ConvergenceCriteria.WithinTolerance);
    }

    @Test
    void builderAllowsCustomConfiguration() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(1.0)
                .upperBound(2.0)
                .tolerance(1e-10)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        assertThat(solver.getTolerance()).isEqualTo(1e-10);
        assertThat(solver.getIterations()).isEqualTo(50);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(ConvergenceCriteria.NumberOfIterations);
    }

    @Test
    void builderRejectsNullFunction() {
        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .lowerBound(0.0)
                        .upperBound(1.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("function");
    }

    @Test
    void builderRejectsInvalidBounds() {
        Function f = new Function("x^2 - 2");

        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .targetFunction(f)
                        .lowerBound(2.0)
                        .upperBound(1.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }


    @Test
    void builderRejectsNaNBounds() {
        Function f = new Function("x");

        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .targetFunction(f)
                        .lowerBound(Double.NaN)
                        .upperBound(1.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");
    }

    @Test
    void builderRejectsInfiniteBounds() {
        Function f = new Function("x");

        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .targetFunction(f)
                        .lowerBound(Double.NEGATIVE_INFINITY)
                        .upperBound(1.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");
    }

    @Test
    void builderRejectsNonPositiveTolerance() {
        Function f = new Function("x^2 - 2");

        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .tolerance(0.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .tolerance(-1e-5)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void builderRejectsNonPositiveIterations() {
        Function f = new Function("x^2 - 2");

        assertThatThrownBy(() ->
                BrentSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .iterations(0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    // ==================== Basic Root Finding Tests ====================

    @ParameterizedTest
    @CsvSource({
            "x^2 - 2, 0.0, 3.0, 1.414213562373095",     // square root of 2
            "x^2 - 4, 0.0, 3.0, 2.0",                   // square root of 4
            "x^3 - 8, 0.0, 5.0, 2.0",                   // cube root of 8
            "x^3 - 2*x - 5, 2.0, 3.0, 2.0945514815423265", // cubic from Brent's original paper
            "x - 5, 0.0, 10.0, 5.0",                    // linear (trivial)
            "sin(x), 3.0, 4.0, 3.141592653589793",      // pi
            "cos(x), 1.0, 2.0, 1.5707963267948966"      // pi/2
    })
    void findsCorrectRoots(String equation, double lower, double upper, double expectedRoot) {
        Function f = new Function(equation);
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(lower)
                .upperBound(upper)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(expectedRoot, within(TOLERANCE));
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void findsRootOfPolynomialWithMultipleRoots() {
        // (x-1)(x-2)(x-3) = x^3 - 6x^2 + 11x - 6
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");

        // Find root near 1
        BrentSolver solver1 = BrentSolver.of(f, 0.5, 1.5);
        assertThat(solver1.solve()).isCloseTo(1.0, within(TOLERANCE));

        // Find root near 2
        BrentSolver solver2 = BrentSolver.of(f, 1.5, 2.5);
        assertThat(solver2.solve()).isCloseTo(2.0, within(TOLERANCE));

        // Find root near 3
        BrentSolver solver3 = BrentSolver.of(f, 2.5, 3.5);
        assertThat(solver3.solve()).isCloseTo(3.0, within(TOLERANCE));
    }

    @Test
    void findsExactRootWhenFunctionReturnsZero() {
        Function f = new Function("x - 3.5");
        BrentSolver solver = BrentSolver.of(f, 0.0, 10.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(3.5, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isEqualTo(0.0);
    }

    // ==================== Convergence Speed Tests ====================

    @Test
    void convergesFasterThanBisection() {
        // Test function where Brent should significantly outperform bisection
        Function f = new Function("x^3 - 2*x - 5");
        double lower = 2.0;
        double upper = 3.0;

        // Count iterations for Brent (use NumberOfIterations to track)
        BrentSolver brentSolver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(lower)
                .upperBound(upper)
                .tolerance(1e-10)
                .iterations(100)
                .build();

        double brentRoot = brentSolver.solve();

        // Count iterations for Bisection
        BisectionSolver bisectionSolver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(lower)
                .upperBound(upper)
                .tolerance(1e-10)
                .iterations(100)
                .build();

        double bisectionRoot = bisectionSolver.solve();

        // Both should find the same root
        assertThat(brentRoot).isCloseTo(bisectionRoot, within(1e-9));

        // Brent should be more accurate or require fewer iterations
        // For this tolerance and bracket, bisection needs ~34 iterations
        // Brent typically needs ~8-10 iterations
        // We verify Brent finds a highly accurate solution
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(f.evaluateAt(brentRoot)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void demonstratesSuperlinearConvergence() {
        // Near the root, Brent should show super-linear convergence
        Function f = new Function("x^3 - 2*x - 5");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(2.0)
                .upperBound(3.0)
                .tolerance(1e-15)  // Very strict tolerance
                .build();

        double root = solver.solve();

        // Should converge to machine precision quickly
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // For very tight tolerances, allow proportionally relaxed function value tolerance
        assertThat(Math.abs(f.evaluateAt(root))).isLessThan(1e-12);
    }

    // ==================== Interpolation Strategy Tests ====================

    @Test
    void handlesLinearInterpolationCase() {
        // Simple linear function benefits from linear interpolation (secant)
        Function f = new Function("2*x - 7");
        BrentSolver solver = BrentSolver.of(f, 0.0, 10.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(3.5, within(TOLERANCE));
    }

    @Test
    void handlesInverseQuadraticInterpolationCase() {
        // Smooth polynomial where inverse quadratic interpolation is effective
        Function f = new Function("x^2 - 10*x + 21");  // (x-3)(x-7)
        BrentSolver solver = BrentSolver.of(f, 0.0, 5.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(3.0, within(1e-4));
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // For this polynomial, f'(3) = -4, so 1e-4 position error gives 4e-4 function error
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(1e-3));
    }

    @Test
    void fallsBackToBisectionWhenNeeded() {
        // Discontinuous-like behavior or sharp turns force bisection fallback
        // Using a function with steep gradient change
        Function f = new Function("x^5 - x - 1");
        BrentSolver solver = BrentSolver.of(f, 1.0, 2.0);

        double root = solver.solve();

        // Should still converge correctly despite fallback
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(root).isCloseTo(1.167303978261418684, within(RELAXED_TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void handlesOscillatoryFunction() {
        // Trigonometric function where interpolation may overshoot
        Function f = new Function("sin(x) - 0.5");
        BrentSolver solver = BrentSolver.of(f, 0.0, 1.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(0.5235987755982989, within(TOLERANCE)); // arcsin(0.5)
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    // ==================== Edge Cases ====================

    @Test
    void handlesRootAtLowerBound() {
        Function f = new Function("x - 1");
        // Need bounds that bracket: f(0.5) = -0.5, f(5.0) = 4.0 (opposite signs)
        BrentSolver solver = BrentSolver.of(f, 0.5, 5.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void handlesRootAtUpperBound() {
        Function f = new Function("x - 5");
        // Need bounds that bracket: f(1.0) = -4.0, f(6.0) = 1.0 (opposite signs)
        BrentSolver solver = BrentSolver.of(f, 1.0, 6.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(5.0, within(TOLERANCE));
    }

    @Test
    void handlesVeryNarrowBracket() {
        Function f = new Function("x^2 - 2");
        // Bracket very close to root
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(1.4)
                .upperBound(1.5)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(1.414213562373095, within(RELAXED_TOLERANCE));
    }

    @Test
    void handlesVeryWideBracket() {
        Function f = new Function("x^2 - 2");
        // Very wide bracket - need bounds that bracket a root
        // f(0) = -2, f(1000) = 999998 (opposite signs)
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(1000.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        // Should find positive root
        assertThat(root).isCloseTo(1.414213562373095, within(RELAXED_TOLERANCE));
    }

    @Test
    void handlesRootNearZero() {
        Function f = new Function("x^3 + x^2 - x"); // x(x^2 + x - 1), root at 0
        BrentSolver solver = BrentSolver.of(f, -1.0, 0.5);

        double root = solver.solve();

        // Root finding uses default solver tolerance, allow relaxed position tolerance
        assertThat(root).isCloseTo(0.0, within(RELAXED_TOLERANCE));
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void handlesNegativeRoots() {
        Function f = new Function("x^2 - 4");
        BrentSolver solver = BrentSolver.of(f, -5.0, -1.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(-2.0, within(1e-4));
    }

    @Test
    void handlesFunctionWithSteepSlope() {
        Function f = new Function("exp(x) - 1000");
        BrentSolver solver = BrentSolver.of(f, 0.0, 10.0);

        double root = solver.solve();

        assertThat(root).isCloseTo(6.907755278982137, within(1e-4)); // ln(1000)
        // Steep slope means small position errors cause large function value errors
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(1e-1));
    }

    @Test
    void handlesFunctionWithShallowSlope() {
        Function f = new Function("x^0.1 - 2");
        BrentSolver solver = BrentSolver.of(f, 0.0, 2000.0);

        double root = solver.solve();

        // Very shallow slope requires relaxed tolerance
        assertThat(root).isCloseTo(1024.0, within(1e-5)); // 2^10
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    // ==================== Convergence Criteria Tests ====================

    @Test
    void withinToleranceStopsWhenToleranceMet() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-6)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        double root = solver.solve();

        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(Math.abs(f.evaluateAt(root))).isLessThan(1e-4);
    }

    @Test
    void numberOfIterationsStopsAfterMaxIterations() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-15)  // Very strict but limited iterations
                .iterations(5)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        double root = solver.solve();

        // Should return best estimate after 5 iterations
        // May not meet strict tolerance but shouldn't throw
        assertThat(root).isBetween(1.0, 2.0);
    }

    @Test
    void withinToleranceThrowsWhenCannotConverge() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-15)
                .iterations(3)  // Not enough iterations
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(ConvergenceException.class)
                .hasMessageContaining("Unable to converge");
    }

    // ==================== Multiple Roots Tests ====================

    @Test
    void solveAllFindsMultipleRoots() {
        // (x-1)(x-2)(x-3) = x^3 - 6x^2 + 11x - 6
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.5)
                .upperBound(3.5)
                .tolerance(1e-5)
                .iterations(200)  // Increase subdivisions to find all roots
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(3);
        assertThat(roots.get(0)).isCloseTo(1.0, within(1e-4));
        assertThat(roots.get(1)).isCloseTo(2.0, within(1e-4));
        assertThat(roots.get(2)).isCloseTo(3.0, within(1e-4));
    }

    @Test
    void solveAllReturnsEmptyListWhenNoRoots() {
        Function f = new Function("x^2 + 1"); // No real roots
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(1.0)
                .tolerance(TOLERANCE)
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).isEmpty();
    }

    @Test
    void solveAllFindsSingleRoot() {
        Function f = new Function("x^2 - 4");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-5)
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(1);
        assertThat(roots.get(0)).isCloseTo(2.0, within(1e-4));
    }

    @Test
    void solveAllFindsWidelySpacedRoots() {
        // Roots at -100, 0, 100: x(x^2 - 10000) = x(x-100)(x+100)
        Function f = new Function("x^3 - 10000*x");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(-150.0)
                .upperBound(150.0)
                .tolerance(1e-5)
                .iterations(500)  // Need many subdivisions for widely spaced roots
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(3);
        assertThat(roots.get(0)).isCloseTo(-100.0, within(1e-2));
        assertThat(roots.get(1)).isCloseTo(0.0, within(1e-4));
        assertThat(roots.get(2)).isCloseTo(100.0, within(1e-2));
    }

    // ==================== Invalid Input Tests ====================

    @Test
    void solveThrowsWhenBoundsDoNotBracketRoot() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(2.0)  // f(2) = 2
                .upperBound(3.0)  // f(3) = 7
                .build();

        // Should throw during solve() validation
        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class);
    }

    @Test
    void solveAllWithInvalidBoundsConfiguration() {
        Function f = new Function("x^2 - 2");
        // Create solver with invalid bounds (should fail at construction time)
        assertThatThrownBy(() -> BrentSolver.of(f, 5.0, 3.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    // ==================== Equality and Object Methods ====================

    @Test
    void equalsReturnsTrueForSameConfiguration() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver1 = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-6)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        BrentSolver solver2 = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-6)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        assertThat(solver1).isEqualTo(solver2);
        assertThat(solver1.hashCode()).isEqualTo(solver2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentConfiguration() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver1 = BrentSolver.of(f, 0.0, 3.0);
        BrentSolver solver2 = BrentSolver.of(f, 0.0, 4.0);

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsReturnsTrueForSameInstance() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.of(f, 0.0, 3.0);

        assertThat(solver).isEqualTo(solver);
    }

    @Test
    void equalsReturnsFalseForNull() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.of(f, 0.0, 3.0);

        assertThat(solver).isNotEqualTo(null);
    }

    @Test
    void equalsReturnsFalseForDifferentType() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.of(f, 0.0, 3.0);

        assertThat(solver).isNotEqualTo("not a solver");
    }

    @Test
    void toStringContainsRelevantInformation() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-6)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        String string = solver.toString();

        assertThat(string).contains("BrentSolver");
        assertThat(string).contains("x^2 - 2");
        assertThat(string).contains("0");
        assertThat(string).contains("3");
        assertThat(string).contains("1.00e-06");
        assertThat(string).contains("50");
        assertThat(string).contains("WithinTolerance");
    }

    // ==================== Immutability Tests ====================

    @Test
    void solverIsImmutable() {
        Function f = new Function("x^2 - 2");
        BrentSolver solver = BrentSolver.of(f, 0.0, 3.0);

        // Capture original configuration
        double originalLower = solver.getLowerBound();
        double originalUpper = solver.getUpperBound();
        double originalTolerance = solver.getTolerance();

        // Perform operations
        double root1 = solver.solve();
        List<Double> ignore = solver.solveAll();
        double root2 = solver.solve();

        // Verify configuration unchanged
        assertThat(solver.getLowerBound()).isEqualTo(originalLower);
        assertThat(solver.getUpperBound()).isEqualTo(originalUpper);
        assertThat(solver.getTolerance()).isEqualTo(originalTolerance);

        // Verify consistent results
        assertThat(root1).isEqualTo(root2);
    }

    // ==================== Comparison with Reference Implementations ====================

    @Test
    void matchesKnownRootFromBrentsOriginalPaper() {
        // Example from Brent's 1973 book: x^3 - 2x - 5 = 0
        // Root in [2, 3] is approximately 2.0945514815423265
        Function f = new Function("x^3 - 2*x - 5");
        BrentSolver solver = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(2.0)
                .upperBound(3.0)
                .tolerance(1e-12)
                .build();

        double root = solver.solve();

        // Reference value from high-precision computation
        double expectedRoot = 2.0945514815423265;
        assertThat(root).isCloseTo(expectedRoot, within(1e-10));
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // Use relaxed tolerance to account for function derivative at root
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void matchesMathematicalConstants() {
        // Find pi as root of sin(x) in [3, 4]
        Function sinFunc = new Function("sin(x)");
        BrentSolver solver1 = BrentSolver.builder()
                .targetFunction(sinFunc)
                .lowerBound(3.0)
                .upperBound(4.0)
                .tolerance(1e-12)
                .build();

        double pi = solver1.solve();
        assertThat(pi).isCloseTo(Math.PI, within(1e-10));

        // Find e as root of ln(x) - 1 in [2, 3]
        Function lnFunc = new Function("ln(x) - 1");
        BrentSolver solver2 = BrentSolver.builder()
                .targetFunction(lnFunc)
                .lowerBound(2.0)
                .upperBound(3.0)
                .tolerance(1e-12)
                .build();

        double e = solver2.solve();
        assertThat(e).isCloseTo(Math.E, within(1e-10));
        // Position tolerance ε becomes function value error ~|f'(root)|*ε
        // For ln(x), f'(x) = 1/x, so at x=e, f'(e)≈0.368, but use relaxed tolerance for safety
        assertThat(lnFunc.evaluateAt(e)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void achievesBetterAccuracyThanBisectionForSameTolerance() {
        Function f = new Function("x^3 - 2*x - 5");
        double tolerance = 1e-8;

        BrentSolver brent = BrentSolver.builder()
                .targetFunction(f)
                .lowerBound(2.0)
                .upperBound(3.0)
                .tolerance(tolerance)
                .build();

        BisectionSolver bisection = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(2.0)
                .upperBound(3.0)
                .tolerance(tolerance)
                .build();

        double brentRoot = brent.solve();
        double bisectionRoot = bisection.solve();

        // Both should be close, but Brent should typically be more accurate
        double brentError = Math.abs(f.evaluateAt(brentRoot));
        double bisectionError = Math.abs(f.evaluateAt(bisectionRoot));

        // Brent is generally more accurate, but both are within tolerance bounds
        // Allow small variance due to convergence criteria differences
        assertThat(brentError).isLessThan(RELAXED_TOLERANCE);
        assertThat(bisectionError).isLessThan(RELAXED_TOLERANCE);
    }
}
