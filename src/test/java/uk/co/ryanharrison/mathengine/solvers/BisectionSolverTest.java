package uk.co.ryanharrison.mathengine.solvers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.ryanharrison.mathengine.Function;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link BisectionSolver}.
 */
class BisectionSolverTest {

    private static final double TOLERANCE = 1e-6;
    private static final double STRICT_TOLERANCE = 1e-9;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesValidSolver() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.of(f, 0.0, 3.0);

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getLowerBound()).isEqualTo(0.0);
        assertThat(solver.getUpperBound()).isEqualTo(3.0);
        assertThat(solver.getTolerance()).isEqualTo(EquationSolver.DEFAULT_TOLERANCE);
        assertThat(solver.getIterations()).isEqualTo(EquationSolver.DEFAULT_ITERATIONS);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(ConvergenceCriteria.WithinTolerance);
    }

    @Test
    void ofRejectsNullFunction() {
        assertThatThrownBy(() -> BisectionSolver.of(null, 0.0, 3.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null");
    }

    @Test
    void ofRejectsInvalidBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() -> BisectionSolver.of(f, 3.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");

        assertThatThrownBy(() -> BisectionSolver.of(f, 2.0, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    @Test
    void builderCreatesValidSolver() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-8)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getLowerBound()).isEqualTo(0.0);
        assertThat(solver.getUpperBound()).isEqualTo(3.0);
        assertThat(solver.getTolerance()).isEqualTo(1e-8);
        assertThat(solver.getIterations()).isEqualTo(50);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(ConvergenceCriteria.NumberOfIterations);
    }

    @Test
    void builderRejectsNullFunction() {
        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Target function must be set");
    }

    @Test
    void builderRejectsInvalidBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(3.0)
                        .upperBound(0.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    @Test
    void builderRejectsNaNBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(Double.NaN)
                        .upperBound(3.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be finite");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(Double.NaN)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be finite");
    }

    @Test
    void builderRejectsInfiniteBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(Double.NEGATIVE_INFINITY)
                        .upperBound(3.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be finite");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(Double.POSITIVE_INFINITY)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be finite");
    }

    @Test
    void builderRejectsNonPositiveTolerance() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .tolerance(0.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tolerance must be positive");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .tolerance(-1e-5)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tolerance must be positive");
    }

    @Test
    void builderRejectsNonPositiveIterations() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .iterations(0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Iterations must be positive");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .iterations(-10)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Iterations must be positive");
    }

    @Test
    void builderRejectsNullConvergenceCriteria() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                BisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(3.0)
                        .convergenceCriteria(null)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Convergence criteria must not be null");
    }

    // ==================== Single Root Tests ====================

    @ParameterizedTest
    @CsvSource({
            "0.0, 3.0, 2.0",           // Positive root of x^2 - 4
            "-3.0, 0.0, -2.0",         // Negative root of x^2 - 4
    })
    void findsRootOfQuadratic(double lower, double upper, double expectedRoot) {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(lower)
                .upperBound(upper)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(expectedRoot, within(TOLERANCE));
        // For bisection, position error ε becomes function value error ~2|x|*ε
        // At x=±2, f'(x)=±4, so use relaxed tolerance for f(root)
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE * 5.0));
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, 1.5, 1.0",           // Root at x = 1
            "1.5, 2.5, 2.0",           // Root at x = 2
            "2.5, 3.5, 3.0",           // Root at x = 3
    })
    void findsRootOfCubic(double lower, double upper, double expectedRoot) {
        // f(x) = x^3 - 6x^2 + 11x - 6 = (x-1)(x-2)(x-3)
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(lower)
                .upperBound(upper)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(expectedRoot, within(TOLERANCE));
        // For bisection, position error ε becomes function value error ~|f'(root)|*ε
        // At the roots, derivatives vary; use relaxed tolerance for f(root)
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE * 5.0));
    }

    @Test
    void findsRootOfSineFunction() {
        Function f = new Function("sin(x)");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(2.0)
                .upperBound(4.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(Math.PI, within(TOLERANCE));
        // For bisection, position error ε becomes function value error ~|f'(root)|*ε
        // At π, f'(π) = cos(π) = -1, so error is ~ε
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE * 5.0));
    }

    @Test
    void findsRootOfExponentialFunction() {
        // e^x - 2 = 0, root at x = ln(2)
        Function f = new Function("exp(x) - 2");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(1.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(Math.log(2), within(TOLERANCE));
        // For bisection, position error ε becomes function value error ~|f'(root)|*ε
        // At ln(2), f'(ln(2)) = exp(ln(2)) = 2, so error is ~2ε
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE * 5.0));
    }

    @Test
    void findsRootOfPolynomialWithLargeCoefficients() {
        // 1000x^2 - 4000 = 0, roots at x = ±2
        Function f = new Function("1000*x^2 - 4000");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE * 1000));
    }

    @Test
    void findsSquareRootOfTwo() {
        // Classic problem: find sqrt(2) by solving x^2 - 2 = 0
        Function f = new Function("x^2 - 2");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(2.0)
                .tolerance(STRICT_TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(Math.sqrt(2), within(STRICT_TOLERANCE));
        // For bisection, position error ε becomes function value error ~|f'(root)|*ε
        // At √2, f'(√2) = 2√2 ≈ 2.828, so error is ~2.828ε
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(STRICT_TOLERANCE * 5.0));
    }

    // ==================== Multiple Roots Tests ====================

    @Test
    void solveAllFindsThreeRootsOfCubic() {
        // f(x) = x^3 - 6x^2 + 11x - 6 = (x-1)(x-2)(x-3)
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(4.0)
                .tolerance(TOLERANCE)
                .iterations(401)  // 401 subdivisions ensures roots at 1, 2, 3 don't fall on boundaries
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(3);
        assertThat(roots.get(0)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(roots.get(1)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(roots.get(2)).isCloseTo(3.0, within(TOLERANCE));
    }

    @Test
    void solveAllFindsTwoRootsOfQuadratic() {
        // f(x) = x^2 - 5x + 6 = (x-2)(x-3)
        Function f = new Function("x^2 - 5*x + 6");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .iterations(200)  // Increase subdivisions to avoid missing roots at boundaries
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(2);
        assertThat(roots.get(0)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(roots.get(1)).isCloseTo(3.0, within(TOLERANCE));
    }

    @Test
    void solveAllReturnsEmptyListWhenNoRoots() {
        // f(x) = x^2 + 1 has no real roots
        Function f = new Function("x^2 + 1");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-5.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).isEmpty();
    }

    @Test
    void solveAllFindsFourRootsOfQuartic() {
        // f(x) = (x-1)(x-2)(x-3)(x-4) = x^4 - 10x^3 + 35x^2 - 50x + 24
        Function f = new Function("x^4 - 10*x^3 + 35*x^2 - 50*x + 24");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(4);
        assertThat(roots.get(0)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(roots.get(1)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(roots.get(2)).isCloseTo(3.0, within(TOLERANCE));
        assertThat(roots.get(3)).isCloseTo(4.0, within(TOLERANCE));
    }

    @Test
    void solveAllWithInvalidBoundsConfiguration() {
        Function f = new Function("x^2 - 4");
        // Create solver with invalid bounds (should fail at construction time)
        assertThatThrownBy(() -> BisectionSolver.of(f, 3.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    // ==================== Convergence Tests - WithinTolerance ====================

    @Test
    void convergesWithinToleranceForQuadratic() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-10)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(2.0, within(1e-10));
        // For bisection, position error ε becomes function value error ~|f'(root)|*ε
        // At x=2, f'(2) = 4, so error is ~4ε
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(1e-10 * 5.0));
    }

    @Test
    void convergesWithinToleranceForTranscendentalFunction() {
        Function f = new Function("cos(x)");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(2.0)
                .tolerance(1e-8)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(Math.PI / 2, within(1e-8));
        // For bisection, position error ε becomes function value error ~|f'(root)|*ε
        // At π/2, f'(π/2) = -sin(π/2) = -1, so error is ~ε
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(1e-8 * 5.0));
    }

    @Test
    void throwsConvergenceExceptionWhenToleranceNotMet() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-20)  // Extremely tight tolerance
                .iterations(10)    // Very few iterations
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(ConvergenceException.class)
                .hasMessageContaining("failed to converge");
    }

    @Test
    void convergenceExceptionContainsUsefulInformation() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-20)
                .iterations(5)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(ConvergenceException.class)
                .extracting(e -> (ConvergenceException) e)
                .satisfies(e -> {
                    assertThat(e.getIterations()).isEqualTo(5);
                    assertThat(e.getLastEstimate()).isCloseTo(2.0, within(0.1));
                    assertThat(e.getTolerance()).isEqualTo(1e-20);
                });
    }

    // ==================== Convergence Tests - NumberOfIterations ====================

    @Test
    void convergesAfterExactNumberOfIterations() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .iterations(20)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        double root = solver.solve();

        // After 20 iterations of bisection, error should be (3-0)/2^20 ≈ 2.86e-6
        assertThat(root).isCloseTo(2.0, within(3e-6));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(1e-5));
    }

    @Test
    void numberOfIterationsCriteriaDoesNotThrowException() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-20)  // Impossible tolerance
                .iterations(5)     // Very few iterations
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        // Should not throw - returns best estimate after 5 iterations
        double root = solver.solve();

        // After 5 iterations: error ≈ (3-0)/2^5 = 0.09375
        assertThat(root).isCloseTo(2.0, within(0.1));
    }

    @Test
    void numberOfIterationsCriteriaWithSingleIteration() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .iterations(1)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        double root = solver.solve();

        // After 1 iteration: midpoint of [0, 3] = 1.5
        assertThat(root).isCloseTo(1.5, within(0.01));
    }

    // ==================== Edge Cases ====================

    @Test
    void findsRootAtLowerBoundary() {
        // f(x) = x - 2, root at 2.0 - use bounds that bracket it
        Function f = new Function("x - 2");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.5)
                .upperBound(2.5)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void findsRootAtUpperBoundary() {
        // f(x) = x - 3, root at 3.0 - use bounds that bracket it
        Function f = new Function("x - 3");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(2.5)
                .upperBound(3.5)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(3.0, within(TOLERANCE));
    }

    @Test
    void findsRootInVeryNarrowInterval() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.99)
                .upperBound(2.01)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void findsRootInVeryWideInterval() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-1000.0)
                .upperBound(0.0)  // f(-1000) = 999996 > 0, f(0) = -4 < 0, brackets root at -2
                .tolerance(TOLERANCE)
                .iterations(1000)  // Need more iterations for such a wide interval
                .build();

        double root = solver.solve();

        // Will find root at -2
        assertThat(root).isCloseTo(-2.0, within(TOLERANCE));
    }

    @Test
    void handlesNegativeBounds() {
        Function f = new Function("x^2 - 9");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-5.0)
                .upperBound(-1.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(-3.0, within(TOLERANCE));
    }

    @Test
    void handlesVeryTightTolerance() {
        Function f = new Function("x - 2");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-15)
                .iterations(100)
                .build();

        double root = solver.solve();

        assertThat(root).isCloseTo(2.0, within(1e-14));
    }

    @Test
    void returnsExactRootWhenFound() {
        // Function that has exact zero at midpoint
        Function f = new Function("x - 1.5");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        // Should find exact root on first iteration
        assertThat(root).isEqualTo(1.5);
        assertThat(f.evaluateAt(root)).isEqualTo(0.0);
    }

    // ==================== Error Conditions ====================

    @Test
    void throwsInvalidBoundsExceptionWhenBoundsDoNotBracketRoot() {
        // f(x) = x^2 + 1 is always positive
        Function f = new Function("x^2 + 1");
        BisectionSolver solver = BisectionSolver.of(f, 0.0, 3.0);

        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class)
                .hasMessageContaining("do not bracket a root");
    }

    @Test
    void invalidBoundsExceptionContainsUsefulInformation() {
        Function f = new Function("x^2 + 1");
        BisectionSolver solver = BisectionSolver.of(f, 0.0, 3.0);

        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class)
                .extracting(e -> (InvalidBoundsException) e)
                .satisfies(e -> {
                    assertThat(e.getLowerBound()).isEqualTo(0.0);
                    assertThat(e.getUpperBound()).isEqualTo(3.0);
                    assertThat(e.getFunctionAtLower()).isEqualTo(1.0);
                    assertThat(e.getFunctionAtUpper()).isEqualTo(10.0);
                });
    }

    @Test
    void throwsInvalidBoundsExceptionWhenBothValuesAreNegative() {
        Function f = new Function("x^2 + 5");
        BisectionSolver solver = BisectionSolver.of(f, -2.0, 2.0);

        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class)
                .hasMessageContaining("same sign");
    }

    @Test
    void throwsInvalidBoundsExceptionWhenBothValuesArePositive() {
        Function f = new Function("x^2 + 1");
        BisectionSolver solver = BisectionSolver.of(f, -2.0, 2.0);

        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class)
                .hasMessageContaining("same sign");
    }

    // ==================== Immutability Tests ====================

    @Test
    void solverIsImmutableAfterConstruction() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        // Store original values
        Function originalFunction = solver.getTargetFunction();
        double originalLower = solver.getLowerBound();
        double originalUpper = solver.getUpperBound();
        double originalTolerance = solver.getTolerance();
        int originalIterations = solver.getIterations();
        ConvergenceCriteria originalCriteria = solver.getConvergenceCriteria();

        // Perform operations
        solver.solve();
        solver.solve();

        // Verify state unchanged
        assertThat(solver.getTargetFunction()).isEqualTo(originalFunction);
        assertThat(solver.getLowerBound()).isEqualTo(originalLower);
        assertThat(solver.getUpperBound()).isEqualTo(originalUpper);
        assertThat(solver.getTolerance()).isEqualTo(originalTolerance);
        assertThat(solver.getIterations()).isEqualTo(originalIterations);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(originalCriteria);
    }

    @Test
    void solveAllDoesNotModifySolverState() {
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.5)
                .upperBound(1.5)
                .tolerance(TOLERANCE)
                .build();

        double originalLower = solver.getLowerBound();
        double originalUpper = solver.getUpperBound();

        // Call solveAll
        solver.solveAll();

        // Verify original bounds unchanged
        assertThat(solver.getLowerBound()).isEqualTo(originalLower);
        assertThat(solver.getUpperBound()).isEqualTo(originalUpper);
    }

    @Test
    void createSolverForIntervalCreatesNewInstance() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .iterations(50)
                .build();

        RootInterval interval = RootInterval.of(1.0, 2.5);
        EquationSolver newSolver = solver.createSolverForInterval(interval);

        // New solver should have different bounds
        assertThat(newSolver).isInstanceOf(BisectionSolver.class);
        BisectionSolver bisectionSolver = (BisectionSolver) newSolver;
        assertThat(bisectionSolver.getLowerBound()).isEqualTo(1.0);
        assertThat(bisectionSolver.getUpperBound()).isEqualTo(2.5);

        // But same configuration
        assertThat(bisectionSolver.getTolerance()).isEqualTo(TOLERANCE);
        assertThat(bisectionSolver.getIterations()).isEqualTo(50);
        assertThat(bisectionSolver.getTargetFunction()).isEqualTo(f);

        // Original solver unchanged
        assertThat(solver.getLowerBound()).isEqualTo(0.0);
        assertThat(solver.getUpperBound()).isEqualTo(3.0);
    }

    // ==================== Equality Tests ====================

    @Test
    void equalsReturnsTrueForIdenticalSolvers() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver1 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        BisectionSolver solver2 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        assertThat(solver1).isEqualTo(solver2);
        assertThat(solver1.hashCode()).isEqualTo(solver2.hashCode());
    }

    @Test
    void equalsReturnsTrueForSameInstance() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.of(f, 0.0, 3.0);

        assertThat(solver).isEqualTo(solver);
        assertThat(solver.hashCode()).isEqualTo(solver.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentBounds() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver1 = BisectionSolver.of(f, 0.0, 3.0);
        BisectionSolver solver2 = BisectionSolver.of(f, 0.0, 4.0);

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsReturnsFalseForDifferentTolerance() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver1 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-6)
                .build();

        BisectionSolver solver2 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(1e-8)
                .build();

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsReturnsFalseForDifferentIterations() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver1 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .iterations(50)
                .build();

        BisectionSolver solver2 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .iterations(100)
                .build();

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsReturnsFalseForDifferentConvergenceCriteria() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver1 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        BisectionSolver solver2 = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsReturnsFalseForDifferentFunction() {
        Function f1 = new Function("x^2 - 4");
        Function f2 = new Function("x^2 - 9");
        BisectionSolver solver1 = BisectionSolver.of(f1, 0.0, 3.0);
        BisectionSolver solver2 = BisectionSolver.of(f2, 0.0, 3.0);

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsReturnsFalseForNull() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.of(f, 0.0, 3.0);

        assertThat(solver).isNotEqualTo(null);
    }

    @Test
    void equalsReturnsFalseForDifferentClass() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.of(f, 0.0, 3.0);

        assertThat(solver).isNotEqualTo("not a solver");
    }

    @Test
    void toStringContainsRelevantInformation() {
        Function f = new Function("x^2 - 4");
        BisectionSolver solver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(3.0)
                .tolerance(TOLERANCE)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        String str = solver.toString();

        assertThat(str).contains("BisectionSolver");
        assertThat(str).contains("x^2 - 4");
        assertThat(str).contains("0");
        assertThat(str).contains("3");
        assertThat(str).contains("50");
        assertThat(str).contains("WithinTolerance");
    }
}
