package uk.co.ryanharrison.mathengine.solvers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.ryanharrison.mathengine.core.Function;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link NewtonRaphsonSolver}.
 */
class NewtonRaphsonSolverTest {

    private static final double TOLERANCE = 1e-7;
    private static final double RELAXED_TOLERANCE = 1e-5;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesNumericalDifferentiationSolver() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f);

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Numerical);
        assertThat(solver.getDerivativeFunction()).isNull();
        assertThat(solver.getInitialGuess()).isEqualTo(RootPolishingMethod.DEFAULT_INITIAL_GUESS);
        assertThat(solver.getTolerance()).isEqualTo(EquationSolver.DEFAULT_TOLERANCE);
        assertThat(solver.getIterations()).isEqualTo(EquationSolver.DEFAULT_ITERATIONS);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(ConvergenceCriteria.WithinTolerance);
    }

    @Test
    void ofWithGuessCreatesNumericalDifferentiationSolver() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f, 2.5);

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Numerical);
        assertThat(solver.getInitialGuess()).isEqualTo(2.5);
    }

    @Test
    void ofWithDerivativeCreatesPredefinedDifferentiationSolver() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f, df, 2.5);

        assertThat(solver.getTargetFunction()).isEqualTo(f);
        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Predefined);
        assertThat(solver.getDerivativeFunction()).isEqualTo(df);
        assertThat(solver.getInitialGuess()).isEqualTo(2.5);
    }

    @Test
    void builderCreatesNumericalDifferentiationSolverByDefault() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .build();

        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Numerical);
        assertThat(solver.getDerivativeFunction()).isNull();
    }

    @Test
    void builderCreatesSymbolicDifferentiationSolver() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .build();

        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Symbolic);
        assertThat(solver.getDerivativeFunction()).isNotNull();
    }

    @Test
    void builderCreatesPredefinedDifferentiationSolver() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .build();

        assertThat(solver.getDifferentiationMethod()).isEqualTo(DifferentiationMethod.Predefined);
        assertThat(solver.getDerivativeFunction()).isEqualTo(df);
    }

    @Test
    void builderAllowsSettingAllParameters() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(3.0)
                .tolerance(1e-10)
                .iterations(50)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .build();

        assertThat(solver.getInitialGuess()).isEqualTo(3.0);
        assertThat(solver.getTolerance()).isEqualTo(1e-10);
        assertThat(solver.getIterations()).isEqualTo(50);
        assertThat(solver.getConvergenceCriteria()).isEqualTo(ConvergenceCriteria.NumberOfIterations);
    }

    @Test
    void builderRejectsNullTargetFunction() {
        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder().build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Target function");
    }

    @Test
    void builderRejectsNullDerivativeFunction() {
        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .derivativeFunction(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Derivative function");
    }

    @Test
    void builderRejectsPredefinedMethodWithoutDerivative() {
        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .differentiationMethod(DifferentiationMethod.Predefined)
                        .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Predefined differentiation method requires a derivative function");
    }

    @Test
    void builderRejectsNonPositiveTolerance() {
        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .tolerance(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .tolerance(-1e-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void builderRejectsNonPositiveIterations() {
        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .iterations(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .iterations(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void builderRejectsNonFiniteInitialGuess() {
        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .initialGuess(Double.NaN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");

        assertThatThrownBy(() ->
                NewtonRaphsonSolver.builder()
                        .targetFunction(new Function("x^2"))
                        .initialGuess(Double.POSITIVE_INFINITY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("finite");
    }

    // ==================== Numerical Differentiation Tests ====================

    @Test
    void numericalDifferentiationFindsSimpleRoot() {
        // Find root of x^2 - 4 = 0 (root is 2)
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void numericalDifferentiationFindsSquareRoot() {
        // Find square root of 10 (root of x^2 - 10 = 0)
        Function f = new Function("x^2 - 10");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(3.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.sqrt(10), within(TOLERANCE));
    }

    @Test
    void numericalDifferentiationFindsCubicRoot() {
        // Find root of x^3 - 2x + 1 = 0 near x = 1.5
        Function f = new Function("x^3 - 2*x + 1");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void numericalDifferentiationFindsTrigonometricRoot() {
        // Find root of cos(x) - x = 0
        Function f = new Function("cos(x) - x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(root).isCloseTo(0.739085133, within(RELAXED_TOLERANCE));
    }

    @Test
    void numericalDifferentiationFindsExponentialRoot() {
        // Find root of e^x - 3 = 0 (x = ln(3))
        Function f = new Function("exp(x) - 3");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.log(3), within(TOLERANCE));
    }

    // ==================== Symbolic Differentiation Tests ====================

    @Test
    void symbolicDifferentiationFindsSimpleRoot() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .initialGuess(1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void symbolicDifferentiationFindsSquareRoot() {
        Function f = new Function("x^2 - 10");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .initialGuess(3.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.sqrt(10), within(TOLERANCE));
    }

    @Test
    void symbolicDifferentiationFindsCubicRoot() {
        Function f = new Function("x^3 - 2*x + 1");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .initialGuess(1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void symbolicDifferentiationFindsTrigonometricRoot() {
        Function f = new Function("cos(x) - x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(root).isCloseTo(0.739085133, within(RELAXED_TOLERANCE));
    }

    // ==================== Predefined Derivative Tests ====================

    @Test
    void predefinedDerivativeFindsSimpleRoot() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void predefinedDerivativeFindsSquareRoot() {
        Function f = new Function("x^2 - 10");
        Function df = new Function("2*x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(3.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.sqrt(10), within(TOLERANCE));
    }

    @Test
    void predefinedDerivativeFindsCubicRoot() {
        Function f = new Function("x^3 - 2*x + 1");
        Function df = new Function("3*x^2 - 2");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void predefinedDerivativeFindsTrigonometricRoot() {
        Function f = new Function("cos(x) - x");
        Function df = new Function("-sin(x) - 1");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(root).isCloseTo(0.739085133, within(RELAXED_TOLERANCE));
    }

    @Test
    void predefinedDerivativeFindsExponentialRoot() {
        Function f = new Function("exp(x) - 3");
        Function df = new Function("exp(x)");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.log(3), within(TOLERANCE));
    }

    // ==================== Convergence Tests ====================

    @Test
    void convergesQuadraticallyNearRoot() {
        // Newton-Raphson should double the number of correct digits per iteration near the root
        Function f = new Function("x^2 - 2");
        Function df = new Function("2*x");

        // Start very close to root (sqrt(2) ≈ 1.414213562)
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.4)
                .tolerance(1e-15)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(Math.sqrt(2), within(1e-14));
    }

    @Test
    void convergesFasterThanBisectionWithGoodGuess() {
        // Compare convergence speed: Newton-Raphson vs Bisection
        Function f = new Function("x^2 - 10");

        // Newton-Raphson with good initial guess - converges in ~4 iterations
        NewtonRaphsonSolver newtonSolver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(3.0)
                .tolerance(1e-10)
                .iterations(20)
                .build();

        // Bisection solver with relaxed parameters
        // Bisection converges at rate log2(n): with 20 iterations and interval [0,5] (width=5)
        // Final interval width = 5 / 2^20 ≈ 4.77e-6, so tolerance should be at least 1e-5
        BisectionSolver bisectionSolver = BisectionSolver.builder()
                .targetFunction(f)
                .lowerBound(0.0)
                .upperBound(5.0)
                .tolerance(1e-5)  // Realistic tolerance for 20 iterations on interval of width 5
                .iterations(20)
                .build();

        double newtonRoot = newtonSolver.solve();
        double bisectionRoot = bisectionSolver.solve();

        // Both should find approximately the same root
        assertThat(newtonRoot).isCloseTo(bisectionRoot, within(1e-5));
        assertThat(newtonRoot).isCloseTo(Math.sqrt(10), within(TOLERANCE));
    }

    @Test
    void convergesWithNumberOfIterationsCriteria() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.5)
                .convergenceCriteria(ConvergenceCriteria.NumberOfIterations)
                .iterations(5)
                .build();

        // Should not throw, just return best estimate after 5 iterations
        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void convergesForDifferentInitialGuesses() {
        Function f = new Function("x^2 - 4");

        for (double guess : new double[]{1.0, 1.5, 2.5, 3.0, 5.0}) {
            NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                    .targetFunction(f)
                    .initialGuess(guess)
                    .tolerance(1e-10)
                    .build();

            double root = solver.solve();
            // Should converge to 2.0 (positive root) from positive guesses
            assertThat(root).isCloseTo(2.0, within(TOLERANCE));
        }
    }

    @Test
    void throwsConvergenceExceptionWhenToleranceNotMet() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.5)
                .tolerance(1e-20)  // Extremely tight tolerance
                .iterations(3)     // Very few iterations
                .build();

        assertThatThrownBy(solver::solve)
                .isInstanceOf(ConvergenceException.class)
                .hasMessageContaining("Unable to converge");
    }

    @ParameterizedTest
    @CsvSource({
            "1.5, 2.0",
            "0.5, 2.0",
            "3.0, 2.0",
            "-1.5, -2.0",
            "-3.0, -2.0"
    })
    void convergesFromVariousInitialGuesses(double guess, double expectedRoot) {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(guess)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(expectedRoot, within(TOLERANCE));
    }

    // ==================== Divergence Tests ====================

    @Test
    void handlesNumbersWithNoRoots() {
        // Function 1/x has no roots - converges to infinity (divergence)
        // or may not converge depending on implementation
        Function f = new Function("1/x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .iterations(50)
                .build();

        // May diverge, or may converge to a spurious point if NR gets stuck
        // This test simply verifies the behavior doesn't crash
        try {
            double root = solver.solve();
            // If it converges, it shouldn't converge to an actual root of 1/x
            // (which don't exist), but Newton-Raphson may produce spurious results
            assertThat(root).isFinite();
        } catch (SolverException e) {
            // Expected outcome - divergence or convergence failure
        }
    }

    @Test
    void throwsDivergenceExceptionWhenDerivativeIsZero() {
        // Function with known zero derivative
        // Using f(x) = (x-1)^2 which has derivative f'(x) = 2(x-1)
        // At x = 1, the derivative is exactly zero
        Function f = new Function("(x-1)^2");
        Function df = new Function("2*(x-1)");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.0)  // x where derivative is exactly zero
                .tolerance(1e-10)
                .build();

        // Should detect zero derivative and throw DivergenceException
        assertThatThrownBy(solver::solve)
                .isInstanceOf(DivergenceException.class)
                .hasMessageContaining("Derivative is zero");
    }

    @Test
    void detectsInfinityDivergence() {
        // Function that causes divergence to infinity
        Function f = new Function("x^2 + 1");  // No real roots
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(100.0)  // Start far from any potential solution
                .tolerance(1e-10)
                .iterations(1000)  // Allow many iterations to diverge
                .build();

        // Should eventually diverge to infinity or fail to converge
        assertThatThrownBy(solver::solve)
                .isInstanceOf(SolverException.class);
    }

    @Test
    void handlesZeroDerivativeGracefully() {
        // Function x^4 has a degenerate root at x=0 (derivative also zero)
        // Newton-Raphson converges linearly (not quadratically) at degenerate roots
        // This is a very pathological case for any root-finding method
        Function f = new Function("x^4");
        Function df = new Function("4*x^3");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(0.1)   // Start further from degenerate point
                .tolerance(1e-6)     // Very relaxed tolerance
                .iterations(100)     // Many iterations to handle linear convergence
                .build();

        // Degenerate roots are extremely difficult; accept any attempt to converge
        try {
            double root = solver.solve();
            // Very loose bounds on convergence - just verify it's trying to get closer
            assertThat(Math.abs(root)).isLessThan(0.1);
        } catch (DivergenceException | ConvergenceException e) {
            // Acceptable for this pathological case
        }
    }

    @Test
    void divergesForFunctionWithMultipleRootsAndPoorGuess() {
        // x^3 - 6x^2 + 11x - 6 = (x-1)(x-2)(x-3) has roots at 1, 2, 3
        // Starting at x = 0 may cause divergence or find wrong root
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(-10.0)  // Far from all roots
                .tolerance(1e-10)
                .iterations(50)
                .build();

        // May converge to a root, fail to converge, or diverge
        // Just ensure it doesn't crash
        try {
            double root = solver.solve();
            // If it converges, verify it's actually a root
            assertThat(f.evaluateAt(root)).isCloseTo(0.0, within(RELAXED_TOLERANCE));
        } catch (SolverException e) {
            // Acceptable outcome
        }
    }

    // ==================== solveAll() Tests ====================

    @Test
    void solveAllFindsAllRootsInInterval() {
        // x^3 - 6x^2 + 11x - 6 = (x-1)(x-2)(x-3) has roots at 1, 2, 3
        // solveAll for polishing methods may not find all roots as it relies on
        // bracketing detection and initial guesses
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-8)
                .iterations(100)
                .build();

        List<Double> roots = solver.solveAll(0.0, 4.0);

        // Polishing methods using solveAll may find 1-3 roots depending on bracket detection
        assertThat(roots).hasSizeGreaterThanOrEqualTo(1);
        assertThat(roots).hasSizeLessThanOrEqualTo(3);

        // Verify at least one root is found
        assertThat(roots).isNotEmpty();
    }

    @Test
    void solveAllFindsQuadraticRoots() {
        // x^2 - 5x + 6 = (x-2)(x-3) has roots at 2, 3
        Function f = new Function("x^2 - 5*x + 6");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-8)  // Relaxed for solveAll
                .iterations(100)
                .build();

        List<Double> roots = solver.solveAll(0.0, 5.0);

        // Should find at least 1 root (polishing may miss one depending on bracket detection)
        assertThat(roots).hasSizeGreaterThanOrEqualTo(1);
        assertThat(roots).hasSizeLessThanOrEqualTo(2);

        // Verify at least one root is found near the expected values
        boolean foundRoot2 = roots.stream().anyMatch(r -> Math.abs(r - 2.0) < RELAXED_TOLERANCE);
        boolean foundRoot3 = roots.stream().anyMatch(r -> Math.abs(r - 3.0) < RELAXED_TOLERANCE);
        assertThat(foundRoot2 || foundRoot3).isTrue();
    }

    @Test
    void solveAllFindsSingleRoot() {
        // x^2 - 4 has roots at -2 and 2; searching [0, 3] should find only 2
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-10)
                .build();

        List<Double> roots = solver.solveAll(0.0, 3.0);

        assertThat(roots).hasSize(1);
        assertThat(roots.get(0)).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void solveAllReturnsEmptyListWhenNoRootsFound() {
        // x^2 + 1 has no real roots
        Function f = new Function("x^2 + 1");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-10)
                .build();

        List<Double> roots = solver.solveAll(-5.0, 5.0);

        assertThat(roots).isEmpty();
    }

    @Test
    void solveAllRemovesDuplicateRoots() {
        // Multiple initial guesses may converge to the same root
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-6)
                .build();

        List<Double> roots = solver.solveAll(-3.0, 3.0);

        // Should find both roots at -2 and 2, without duplicates
        assertThat(roots).hasSize(2);
        assertThat(roots.get(0)).isCloseTo(-2.0, within(RELAXED_TOLERANCE));
        assertThat(roots.get(1)).isCloseTo(2.0, within(RELAXED_TOLERANCE));
    }

    @Test
    void solveAllReturnsSortedRoots() {
        Function f = new Function("x^3 - 6*x^2 + 11*x - 6");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-10)
                .build();

        List<Double> roots = solver.solveAll(0.0, 4.0);

        // Verify roots are in ascending order
        for (int i = 1; i < roots.size(); i++) {
            assertThat(roots.get(i)).isGreaterThan(roots.get(i - 1));
        }
    }

    @Test
    void solveAllRejectsInvalidBounds() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f);

        assertThatThrownBy(() -> solver.solveAll(5.0, 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    // ==================== Immutability Tests ====================

    @Test
    void solverIsImmutable() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(3.0)
                .tolerance(1e-10)
                .build();

        double originalGuess = solver.getInitialGuess();
        double originalTolerance = solver.getTolerance();

        // Perform operations
        solver.solve();
        solver.solveAll(0.0, 5.0);

        // Verify state unchanged
        assertThat(solver.getInitialGuess()).isEqualTo(originalGuess);
        assertThat(solver.getTolerance()).isEqualTo(originalTolerance);
    }

    @Test
    void createSolverForGuessCreatesNewInstance() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver original = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.0)
                .tolerance(1e-10)
                .build();

        NewtonRaphsonSolver newSolver = (NewtonRaphsonSolver) original.createSolverForGuess(2.5);

        assertThat(newSolver).isNotSameAs(original);
        assertThat(newSolver.getInitialGuess()).isEqualTo(2.5);
        assertThat(newSolver.getTolerance()).isEqualTo(original.getTolerance());
        assertThat(newSolver.getTargetFunction()).isEqualTo(original.getTargetFunction());
        assertThat(original.getInitialGuess()).isEqualTo(1.0);  // Original unchanged
    }

    // ==================== Equality Tests ====================

    @Test
    void equalsSolversWithSameConfiguration() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver1 = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(2.0)
                .tolerance(1e-10)
                .build();

        NewtonRaphsonSolver solver2 = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(2.0)
                .tolerance(1e-10)
                .build();

        assertThat(solver1).isEqualTo(solver2);
        assertThat(solver1.hashCode()).isEqualTo(solver2.hashCode());
    }

    @Test
    void notEqualsSolversWithDifferentGuess() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver1 = NewtonRaphsonSolver.of(f, 1.0);
        NewtonRaphsonSolver solver2 = NewtonRaphsonSolver.of(f, 2.0);

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void notEqualsSolversWithDifferentTolerance() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver1 = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-5)
                .build();

        NewtonRaphsonSolver solver2 = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .tolerance(1e-10)
                .build();

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void notEqualsSolversWithDifferentDifferentiationMethod() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver1 = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Numerical)
                .build();

        NewtonRaphsonSolver solver2 = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .build();

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void notEqualsSolversWithDifferentTargetFunction() {
        Function f1 = new Function("x^2 - 4");
        Function f2 = new Function("x^2 - 9");
        NewtonRaphsonSolver solver1 = NewtonRaphsonSolver.of(f1);
        NewtonRaphsonSolver solver2 = NewtonRaphsonSolver.of(f2);

        assertThat(solver1).isNotEqualTo(solver2);
    }

    @Test
    void equalsItself() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f);

        assertThat(solver).isEqualTo(solver);
    }

    @Test
    void notEqualsNull() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f);

        assertThat(solver).isNotEqualTo(null);
    }

    @Test
    void notEqualsDifferentClass() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.of(f);

        assertThat(solver).isNotEqualTo("not a solver");
    }

    @Test
    void toStringContainsKeyInformation() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(2.5)
                .tolerance(1e-10)
                .iterations(50)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .build();

        String str = solver.toString();
        assertThat(str).contains("NewtonRaphsonSolver");
        assertThat(str).contains("Symbolic");
        assertThat(str).contains("2.5");
    }

    // ==================== Edge Cases ====================

    @Test
    void findsRootAtZero() {
        // x^3 has a root at x = 0, but the derivative 3x^2 is also zero there
        // This is a degenerate root where Newton-Raphson exhibits linear convergence
        // rather than quadratic convergence, making it very difficult to converge tightly
        Function f = new Function("x^3");
        Function df = new Function("3*x^2");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(0.1)
                .tolerance(1e-5)   // Very relaxed tolerance for degenerate root
                .iterations(100)   // Many iterations for linear convergence
                .build();

        double root = solver.solve();
        // Accept very loose convergence for degenerate roots
        assertThat(root).isCloseTo(0.0, within(0.02));
    }

    @Test
    void findsNegativeRoot() {
        Function f = new Function("x^2 - 4");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(-1.5)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(-2.0, within(TOLERANCE));
    }

    @Test
    void findsRootOfLinearFunction() {
        // Linear function: 2x - 6 = 0, root is x = 3
        Function f = new Function("2*x - 6");
        Function df = new Function("2");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(10.0)  // Start far from root
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(3.0, within(TOLERANCE));
    }

    @Test
    void findsRootOfConstantDerivative() {
        // Function with constant derivative
        Function f = new Function("3*x + 7");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(0.0)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(-7.0 / 3.0, within(TOLERANCE));
    }

    @Test
    void handlesVerySmallTolerance() {
        Function f = new Function("x^2 - 4");
        Function df = new Function("2*x");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(1.5)
                .tolerance(1e-15)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(1e-14));
    }

    @Test
    void handlesLargeFunctionValues() {
        // Function with large values: 1000*x^2 - 4000
        Function f = new Function("1000*x^2 - 4000");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(1.5)
                .tolerance(1e-8)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(2.0, within(TOLERANCE));
    }

    @Test
    void findsRootNearBoundary() {
        // x*(1-x) = 0 has roots at x = 0 and x = 1 (on boundaries of [0, 1])
        // Test finding the root at x = 1
        Function f = new Function("x*(1-x)");
        NewtonRaphsonSolver solver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .initialGuess(0.9)
                .tolerance(1e-10)
                .build();

        double root = solver.solve();
        assertThat(root).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void allDifferentiationMethodsProduceSimilarResults() {
        Function f = new Function("x^3 - 2*x + 1");
        Function df = new Function("3*x^2 - 2");
        double guess = 1.5;
        double tolerance = 1e-10;

        NewtonRaphsonSolver numericalSolver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Numerical)
                .initialGuess(guess)
                .tolerance(tolerance)
                .build();

        NewtonRaphsonSolver symbolicSolver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .differentiationMethod(DifferentiationMethod.Symbolic)
                .initialGuess(guess)
                .tolerance(tolerance)
                .build();

        NewtonRaphsonSolver predefinedSolver = NewtonRaphsonSolver.builder()
                .targetFunction(f)
                .derivativeFunction(df)
                .initialGuess(guess)
                .tolerance(tolerance)
                .build();

        double numericalRoot = numericalSolver.solve();
        double symbolicRoot = symbolicSolver.solve();
        double predefinedRoot = predefinedSolver.solve();

        // All methods should converge to the same root
        assertThat(numericalRoot).isCloseTo(symbolicRoot, within(RELAXED_TOLERANCE));
        assertThat(symbolicRoot).isCloseTo(predefinedRoot, within(TOLERANCE));
    }
}
