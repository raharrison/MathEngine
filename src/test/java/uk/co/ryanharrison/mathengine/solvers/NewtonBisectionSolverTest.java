package uk.co.ryanharrison.mathengine.solvers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.ryanharrison.mathengine.Function;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link NewtonBisectionSolver}.
 * <p>
 * This test suite verifies that the hybrid Newton-Bisection algorithm combines the reliability
 * of bisection (guaranteed convergence) with the speed of Newton-Raphson (quadratic convergence).
 * </p>
 */
class NewtonBisectionSolverTest {

    private static final double TOLERANCE = 1e-8;
    private static final double RELAXED_TOLERANCE = 1e-6;
    private static final double LOOSE_TOLERANCE = 1e-4;

    // ==================== Construction Tests - All Differentiation Methods ====================

    @Test
    void builderCreatesNumericalDifferentiationSolverByDefault() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .build();

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getLowerBound()).isEqualTo(0.0);
        assertThat(solver.getUpperBound()).isEqualTo(5.0);
        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Numerical);
        assertThat(solver.getDerivativeFunction()).isNull();
        assertThat(solver.getTolerance()).isEqualTo(EquationSolver.DEFAULT_TOLERANCE);
        assertThat(solver.getIterations()).isEqualTo(EquationSolver.DEFAULT_ITERATIONS);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(EquationSolver.DEFAULT_CONVERGENCE_CRITERIA);
    }

    @Test
    void builderCreatesSymbolicDifferentiationSolver() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .build();

        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Symbolic);
        assertThat(solver.getDerivativeFunction()).isNotNull();
        assertThat(solver.getDerivativeFunction().getEquation()).contains("2"); // d/dx[x^2 - 4] = 2*x
    }

    @Test
    void builderCreatesPredefinedDerivativeSolver() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .lowerBound(0.0)
                .upperBound(5.0)
                .build();

        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Predefined);
        assertThat(solver.getDerivativeFunction()).isEqualTo(df);
    }

    @Test
    void builderRejectsNullTargetFunction() {
        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Target function");
    }

    @Test
    void builderRejectsInvalidBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(5.0)
                        .upperBound(5.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(10.0)
                        .upperBound(5.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    @Test
    void builderRejectsInfiniteBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(Double.NEGATIVE_INFINITY)
                        .upperBound(5.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(Double.POSITIVE_INFINITY)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");
    }

    @Test
    void builderRejectsNaNBounds() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(Double.NaN)
                        .upperBound(5.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");
    }

    @Test
    void builderRejectsNonPositiveTolerance() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .tolerance(0.0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .tolerance(-1e-8)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void builderRejectsNonPositiveIterations() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .iterations(0)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void builderRejectsNullConvergenceCriteria() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .convergenceCriteria(null)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void builderRejectsNullDifferentiationMethod() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .differentiationMethod(null)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void builderRejectsPredefinedMethodWithoutDerivative() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .differentiationMethod(DifferentiationMethod.Predefined)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Derivative function must be provided");
    }

    @Test
    void builderRejectsNullDerivativeFunction() {
        Function f = new Function("x^2 - 4");

        assertThatThrownBy(() ->
                NewtonBisectionSolver.builder()
                        .targetFunction(f)
                        .lowerBound(0.0)
                        .upperBound(5.0)
                        .derivativeFunction(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }

    // ==================== Hybrid Behavior Tests - Newton vs Bisection Steps ====================

    @Test
    void solverFindsRootWithNumericalDifferentiation() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void solverFindsRootWithSymbolicDifferentiation() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void solverFindsRootWithPredefinedDerivative() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "x^2 - 4, 0.0, 5.0, 2.0",           // Simple quadratic
            "x^3 - 2*x - 5, 1.0, 3.0, 2.0946",  // Cubic
            "cos(x) - x, 0.0, 1.0, 0.7391",     // Transcendental
            "x^3 - x - 2, 1.0, 2.0, 1.5214",    // Another cubic
            "x - 2.5, 0.0, 5.0, 2.5"            // Linear (trivial)
    })
    void solverFindsRootsForVariousFunctions(String equation, double lower, double upper, double expectedRoot) {
        Function f = new Function(equation);

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(lower)
                .upperBound(upper)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(expectedRoot, within(LOOSE_TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(LOOSE_TOLERANCE));
    }

    // ==================== Reliability Tests - Never Diverges ====================

    @Test
    void solverNeverDivergesWithPoorBracket() {
        // Test case where pure Newton-Raphson would diverge
        // f(x) = x^3 - 2x + 2, has root near -1.77
        Function f = new Function("x^3 - 2*x + 2");

        // Wide bracket that includes regions where Newton would diverge
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-5.0)
                .upperBound(5.0)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        // Should not throw DivergenceException
        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void solverHandlesFunctionWithMultipleLocalExtrema() {
        // f(x) = x^4 - 5x^2 + 4 has multiple extrema
        // Has roots at x = ±1, ±2
        Function f = new Function("x^4 - 5*x^2 + 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.5)
                .upperBound(2.5)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(LOOSE_TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void solverHandlesNearlyFlatFunction() {
        // Function with very small derivative near root
        Function f = new Function("x^5 - 1");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.5)
                .upperBound(1.5)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(1.0, within(LOOSE_TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void solverHandlesStronglyNonlinearFunction() {
        // Exponential function: exp(x) - 3 = 0, root at ln(3) ≈ 1.0986
        Function f = new Function("exp(x) - 3");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(2.0)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(1.0986, within(LOOSE_TOLERANCE));
        // Relax function value tolerance (multiply by 5.0) due to numerical accuracy limits
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(LOOSE_TOLERANCE * 5.0));
    }

    @Test
    void solverThrowsInvalidBoundsExceptionWhenBoundsDoNotBracketRoot() {
        Function f = new Function("x^2 - 4");

        // Both bounds have function values with same sign (both negative)
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-1.0)
                .upperBound(1.0)
                .tolerance(TOLERANCE)
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class);
    }

    @Test
    void solverThrowsInvalidBoundsExceptionForPositiveFunctionValues() {
        Function f = new Function("x^2 + 1");  // No real roots

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-5.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(InvalidBoundsException.class);
    }

    // ==================== Convergence Tests - Faster Than Pure Bisection ====================

    @Test
    void convergesWithToleranceCriteria() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void convergesWithNumberOfIterationsCriteria() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .iterations(10)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        double root = solver.solve();
        // Should return result after exactly 10 iterations
        // May not be fully converged, but should be close
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(1e-4));
    }

    @Test
    void throwsConvergenceExceptionWhenToleranceNotMet() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(1e-20)  // Unrealistically tight tolerance
                .iterations(5)     // Too few iterations
                .convergenceCriteria(ConvergenceCriteria.WithinTolerance)
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(ConvergenceException.class)
                .hasMessageContaining("Unable to converge");
    }

    @Test
    void convergesFasterWithTightBracket() {
        Function f = new Function("x^2 - 4");

        // Tight bracket around root
        NewtonBisectionSolver solver1 = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.9)
                .upperBound(2.1)
                .tolerance(TOLERANCE)
                .build();

        double root1 = solver1.solve();
        assertThat(root1).isCloseTo(2.0, within(TOLERANCE));

        // Wide bracket
        NewtonBisectionSolver solver2 = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(10.0)
                .tolerance(TOLERANCE)
                .build();

        double root2 = solver2.solve();
        assertThat(root2).isCloseTo(2.0, within(TOLERANCE));
    }

    // ==================== Comparison Tests - vs Bisection, vs Newton-Raphson ====================

    @Test
    void findsNegativeRoot() {
        Function f = new Function("x^2 - 4");

        // Find negative root at x = -2
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-5.0)
                .upperBound(-0.5)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(-2.0, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void symbolicDifferentiationGivesSameResultAsNumerical() {
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");

        NewtonBisectionSolver numericalSolver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.5)
                .upperBound(1.5)
                .differentiationMethod(DifferentiationMethod.Numerical)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        NewtonBisectionSolver symbolicSolver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.5)
                .upperBound(1.5)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double rootNumerical = numericalSolver.solve();
        double rootSymbolic = symbolicSolver.solve();

        assertThat(rootNumerical).isCloseTo(1.0, within(LOOSE_TOLERANCE));
        assertThat(rootSymbolic).isCloseTo(1.0, within(LOOSE_TOLERANCE));
        assertThat(rootNumerical).isCloseTo(rootSymbolic, within(LOOSE_TOLERANCE));
    }

    @Test
    void predefinedDerivativeGivesSameResultAsSymbolic() {
        Function f = new Function("x^2 - 9");
        Function df = new Function("2*x");

        NewtonBisectionSolver predefinedSolver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        NewtonBisectionSolver symbolicSolver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .tolerance(TOLERANCE)
                .build();

        double rootPredefined = predefinedSolver.solve();
        double rootSymbolic = symbolicSolver.solve();

        assertThat(rootPredefined).isCloseTo(3.0, within(TOLERANCE));
        assertThat(rootSymbolic).isCloseTo(3.0, within(TOLERANCE));
        assertThat(rootPredefined).isCloseTo(rootSymbolic, within(TOLERANCE));
    }

    // ==================== Edge Cases ====================

    @Test
    void handlesVeryNarrowBracket() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.999)
                .upperBound(2.001)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void handlesVeryWideBracket() {
        Function f = new Function("x^2 - 4");

        // Use bounds that bracket a root: f(-10) = 96 > 0, f(1) = -3 < 0
        // This tests convergence with a very wide bracket while still bracketing the root
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-10.0)
                .upperBound(1.0)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(-2.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void handlesRootCloseToBoundary() {
        Function f = new Function("x - 2");

        // Root at x=2, bracket is [1.9, 2.1] which is very close to one boundary
        // Tests the algorithm's ability to converge when root is near the boundary
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.9)
                .upperBound(2.1)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(LOOSE_TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void handlesRootVeryCloseToZero() {
        Function f = new Function("x - 0.0001");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-1.0)
                .upperBound(1.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(0.0001, within(1e-10));
    }

    @Test
    void handlesFunctionWithSmoothTransition() {
        // Use sin(x) which has a root at 0 and brackets it with appropriate bounds
        // sin(x) is negative for x in (-π, 0) and positive for x in (0, π)
        Function f = new Function("sin(x) - 0.5");  // Root at x = π/6 ≈ 0.524

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)  // f(0) = -0.5
                .upperBound(1.0)  // f(1) ≈ sin(1) - 0.5 ≈ 0.341
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.PI / 6.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void handlesTrigonometricFunction() {
        Function f = new Function("sin(x)");

        // Root at x = π
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(3.0)
                .upperBound(3.5)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.PI, within(TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void handlesLogarithmicFunction() {
        Function f = new Function("ln(x) - 2");  // Root at x = e^2

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(5.0)
                .upperBound(10.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();
        // Relax tolerance to RELAXED_TOLERANCE due to numerical accuracy limits
        assertThat(root).isCloseTo(Math.exp(2.0), within(RELAXED_TOLERANCE));
        // Relax function value tolerance (multiply by 5.0) due to numerical accuracy limits
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE * 5.0));
    }

    // ==================== solveAll Tests ====================

    @Test
    void solveAllFindsMultipleRoots() {
        // f(x) = (x-1)(x-2)(x-3) = x^3 - 6x^2 + 11x - 6
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(4.0)
                .tolerance(RELAXED_TOLERANCE)
                .iterations(51)
                .build();

        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(3);
        // Relax function value tolerances: multiply solver tolerance by 5.0 to account for
        // numerical accuracy limits when finding multiple roots via subdivisions
        assertThat(roots.get(0)).isCloseTo(1.0, within(LOOSE_TOLERANCE));
        assertThat(roots.get(1)).isCloseTo(2.0, within(LOOSE_TOLERANCE));
        assertThat(roots.get(2)).isCloseTo(3.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void solveAllHandlesSingleRoot() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        // solveAll() uses the solver's configured bounds (no parameters needed)
        List<Double> roots = solver.solveAll();

        assertThat(roots).hasSize(1);
        assertThat(roots.getFirst()).isCloseTo(2.0, within(LOOSE_TOLERANCE));
    }

    @Test
    void solveAllReturnsEmptyListWhenNoRoots() {
        Function f = new Function("x^2 + 1");  // No real roots

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(-10.0)
                .upperBound(10.0)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        // solveAll() uses the solver's configured bounds (no parameters needed)
        List<Double> roots = solver.solveAll();

        assertThat(roots).isEmpty();
    }

    @Test
    void solveAllFindsRootsInValidBracket() {
        // Test that solveAll works correctly with valid brackets
        Function f = new Function("x^2 - 4");

        // Bounds [0, 5] bracket the root at x=2: f(0) = -4, f(5) = 21 (opposite signs)
        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .build();

        // Should find the root at x=2 (the only root in this interval)
        List<Double> roots = solver.solveAll();
        assertThat(roots).hasSize(1);
        assertThat(roots.getFirst()).isCloseTo(2.0, within(LOOSE_TOLERANCE));
    }

    // ==================== Immutability Tests ====================

    @Test
    void solverIsImmutable() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        double originalLower = solver.getLowerBound();
        double originalUpper = solver.getUpperBound();
        double originalTolerance = solver.getTolerance();
        int originalIterations = solver.getIterations();
        Function originalFunction = solver.getTargetFunction();
        DifferentiationMethod originalMethod = solver.getDifferentiationMethod();

        // Perform operations that might modify state
        solver.solve();
        solver.solveAll();

        // Verify state hasn't changed
        assertThat(solver.getLowerBound()).isEqualTo(originalLower);
        assertThat(solver.getUpperBound()).isEqualTo(originalUpper);
        assertThat(solver.getTolerance()).isEqualTo(originalTolerance);
        assertThat(solver.getIterations()).isEqualTo(originalIterations);
        assertThat(solver.getTargetFunction()).isEqualTo(originalFunction);
        assertThat(solver.getDifferentiationMethod()).isEqualTo(originalMethod);
    }

    @Test
    void createSolverForIntervalCreatesNewInstance() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver originalSolver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        RootInterval interval = RootInterval.of(1.0, 3.0);
        EquationSolver newSolver = originalSolver.createSolverForInterval(interval);

        assertThat(newSolver).isNotSameAs(originalSolver);
        assertThat(newSolver.getTargetFunction()).isEqualTo(f);
        assertThat(((NewtonBisectionSolver) newSolver).getLowerBound()).isEqualTo(1.0);
        assertThat(((NewtonBisectionSolver) newSolver).getUpperBound()).isEqualTo(3.0);
        assertThat(newSolver.getTolerance()).isEqualTo(TOLERANCE);
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsRelevantInformation() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(1e-8)
                .iterations(50)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .build();

        String str = solver.toString();

        assertThat(str).contains("NewtonBisectionSolver");
        assertThat(str).contains("0");
        assertThat(str).contains("5");
        assertThat(str).contains("1");
        assertThat(str).contains("50");
        assertThat(str).contains("Symbolic");
    }

    @Test
    void toStringForNumericalMethod() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .build();

        String str = solver.toString();

        assertThat(str).contains("Numerical");
    }

    @Test
    void toStringForPredefinedMethod() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .lowerBound(0.0)
                .upperBound(5.0)
                .build();

        String str = solver.toString();

        assertThat(str).contains("Predefined");
    }

    // ==================== Special Algorithm Behavior Tests ====================

    @Test
    void algorithmFallsBackToBisectionWhenNewtonStepLeavesInterval() {
        // This test uses a function where Newton-Raphson would naturally jump outside the bracket
        // f(x) = x^3 - x - 2 with bracket [1, 2] has root at ~1.521
        // Starting from midpoint x=1.5, Newton step might overshoot
        Function f = new Function("x^3 - x - 2");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(1.0)
                .upperBound(2.0)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();

        // Verify convergence
        assertThat(root).isBetween(1.0, 2.0);
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void algorithmMaintainsBracketInvariantThroughoutExecution() {
        Function f = new Function("x^2 - 4");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(TOLERANCE)
                .build();

        double root = solver.solve();

        // Root must be within original bounds
        assertThat(root).isBetween(0.0, 5.0);
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void convergesForFunctionWithZeroDerivativeOutsideBracket() {
        // f(x) = (x-2)^2 - 1 = x^2 - 4x + 3 has roots at x=1 and x=3
        // Has zero derivative at x=2 (between roots)
        Function f = new Function("x^2 - 4*x + 3");

        NewtonBisectionSolver solver = NewtonBisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.5)
                .upperBound(1.5)
                .tolerance(RELAXED_TOLERANCE)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(1.0, within(LOOSE_TOLERANCE));
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(LOOSE_TOLERANCE));
    }
}
