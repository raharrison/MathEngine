# CLAUDE.md

IMPORTANT: When applicable, prefer using jetbrains-index MCP tools for code navigation and refactoring.

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Math Engine is a comprehensive Java mathematical library featuring an advanced expression parser with support for custom
functions, vectors, matrices, symbolic differentiation, numerical integration, equation solving, unit conversions, probability
distributions, and more.

The library is designed around the `Function` class (modeling f(x) equations) and the `Evaluator` class (an expression
parser/calculator). Most packages can be used standalone or integrated through the parser's operator system.

## Build and Test Commands

**Build System**: Gradle 9.2.0 with Kotlin DSL

```bash
# Build the project
./gradlew build

# Run all tests
./gradlew test

# Run all tests with detailed output
./gradlew test --info

# Run tests for a specific class
./gradlew test --tests uk.co.ryanharrison.mathengine.parser.EvaluatorCustomFunctionTest

# Run a single test method
./gradlew test --tests uk.co.ryanharrison.mathengine.parser.EvaluatorCustomFunctionTest.canDefineCustomFunction

# Run tests matching a pattern
./gradlew test --tests "*Distribution*"

# Run tests in a specific package
./gradlew test --tests "uk.co.ryanharrison.mathengine.distributions.*"

# Generate test coverage report
./gradlew jacocoTestReport
# Report will be in build/reports/jacoco/test/html/index.html

# Print test summary from XML reports (custom task)
./gradlew testSummary

# Run tests and immediately view summary
./gradlew test testSummary

# Clean build artifacts
./gradlew clean
```

### Efficient Test Workflow for AI Agents

**Critical: Use test filters to minimize feedback latency**

When developing or refactoring tests, ALWAYS use test filters instead of running all tests:

```bash
# Run specific test class during development
./gradlew test --tests NormalDistributionTest

# Run all tests in a package
./gradlew test --tests "uk.co.ryanharrison.mathengine.distributions.*"

# Run tests matching a pattern
./gradlew test --tests "*Distribution*"
```

**Get structured test feedback with testSummary**

After running tests, ALWAYS use `testSummary` to get clean, parseable output:

```bash
# Run tests and immediately view summary
./gradlew test --tests NormalDistributionTest && ./gradlew testSummary
```

The `testSummary` task outputs:

- Total test counts (passed/failed/errors/skipped)
- List of all failed test names (easy to parse and act on)
- Concise format without grepping through large console output

**Key strategies for efficient AI agent workflows**:

1. **Always filter tests** - Running all tests is slow. Use `--tests` flag to run only relevant tests.

2. **Use testSummary for failures** - Provides structured output showing exactly which tests failed without parsing verbose logs.

3. **Avoid `--info` or `--debug`** - These produce massive output that's hard to parse. Standard output plus `testSummary` is
   sufficient.

4. **Test reports location** - If you need detailed stack traces, read from:
    - XML reports: `build/test-results/test/*.xml` (machine-readable)
    - HTML report: `build/reports/tests/test/index.html` (can be read with Read tool)

## Tools

**Java Version**: Java 25 (using toolchain)

**Dependencies**:

- Apache Commons Lang 3.17.0
- JUnit Jupiter 5.11.4 (testing via BOM)
- AssertJ 3.27.2 (test assertions)

## GUI Applications

The project includes runnable GUI applications with `main` methods:

1. **MainFrame** (`uk.co.ryanharrison.mathengine.gui.MainFrame`) - Interactive expression evaluator with graphical interface
2. **Converter** (`uk.co.ryanharrison.mathengine.gui.Converter`) - Unit conversion tool with GUI
3. **Grapher** (`uk.co.ryanharrison.mathengine.plotting.Grapher`) - Function plotter with pan/zoom

## Core Architecture

### Parser Package - Expression Evaluation System

The parser is the centerpiece of the library, providing expression evaluation with multiple result types. Understanding its
architecture is critical:

**Two-Stage Evaluation Pipeline**:

1. `ExpressionParser` converts strings → Node expression trees
2. `RecursiveDescentParser` evaluates trees → `NodeConstant` results

**Evaluator Factory Methods** (different operator sets):

- `Evaluator.newSimpleBinaryEvaluator()` - Only basic arithmetic (+, -, *, /, ^)
- `Evaluator.newSimpleEvaluator()` - Adds unary operators (sin, cos, abs, factorial, etc.)
- `Evaluator.newEvaluator()` - Full evaluator with logical, vector, matrix, custom operators

**Node Type Hierarchy** (result types):

```
NodeConstant (abstract base for all results)
├── NodeNumber (abstract numeric base)
│   ├── NodeDouble (floating point - default)
│   ├── NodeRational (exact fractions using BigRational)
│   ├── NodePercent (special double that auto-divides by 100)
│   └── NodeBoolean (1.0 for true, 0.0 for false)
├── NodeVector (1D arrays - Node[] elements)
├── NodeMatrix (2D arrays - Node[][] elements)
└── NodeFunction (user-defined functions with compiled trees)
```

**Key Design Patterns**:

- `NodeTransformer`: Type conversion interface (number→vector, vector→matrix, etc.)
- `applyUniFunc(Function)` / `applyBiFunc(BiFunction)`: Apply operations element-wise to containers
- Container normalization: Vectors/matrices auto-align sizes via broadcasting or zero-padding
- Operators are pluggable strategies registered via `OperatorProvider`

**Operator Registration**:
All operators live in `parser.operators` package:

- `BinaryOperator` subclasses (e.g., `Add`, `Multiply`)
- `UnaryOperator` subclasses (e.g., `Sine`, `Factorial`, `Sum`)
- Template classes: `SimpleBinaryOperator`, `NumberOperator`, `VectorOperator`, `MatrixOperator`, `TrigOperator`
- `CustomOperator`: Wraps user-defined `NodeFunction` instances

**Custom Functions**:
Users define functions with `:=` operator:

```java
evaluator.evaluateConstant("f(x) := x^2 + 8*x + 12");
evaluator.

evaluateDouble("f(5)"); // Returns 77.0
```

Custom functions can be redefined anytime but **cannot override system operators** (sin, cos, +, -, etc.). This is enforced to
prevent breaking built-in functionality.

**Rational vs Double Arithmetic**:

- Operations on whole numbers or rationals preserve `NodeRational` type (exact arithmetic)
- Mixing `NodeRational` + `NodeDouble` = `NodeDouble` (precision loss)
- Use `.toRational()` operator to convert or ensure inputs stay rational

**EvaluationContext**:
Central state object holding:

- Constants/variables map
- Operators registry
- AngleUnit (Radians/Degrees for trig)
- Supports scoping via `withArgs()` for function parameter binding

### Function Class - Single-Variable Equations

The `Function` class (`uk.co.ryanharrison.mathengine.Function`) is the primary interface for packages like differential, integral,
solvers, and plotting.

**Usage Pattern**:

```java
Function f = new Function("x^2 + 8*x + 12");
double result = f.evaluateAt(3.5);
Node tree = f.getCompiledExpression(); // Cached parse tree
```

**Constructor Overloads**:

- `Function(String equation)` - Defaults to variable "x", Radians
- `Function(String equation, AngleUnit angleUnit)`
- `Function(String equation, String variable)`
- `Function(String equation, String variable, AngleUnit angleUnit)`

Internally uses `Evaluator.newSimpleEvaluator()` with lazy initialization. The expression tree is cached for performance.

### Package Organization

**differential/** - Differentiation

- Numeric methods: `DividedDifferenceMethod`, `ExtendedCentralDifferenceMethod`, `RichardsonExtrapolationMethod`
- Symbolic: `Differentiator` (returns new `Function` representing derivative)

**integral/** - Numerical integration

- Implementations: `TrapeziumIntegrator`, `SimpsonIntegrator`, `RectangularIntegrator`
- All extend `IntegrationMethod` base class

**solvers/** - Root finding algorithms

- Implementations: `BrentSolver`, `BisectionSolver`, `NewtonRaphsonSolver`, `NewtonBisectionSolver`
- Methods: `solve()` finds one root, `solveAll()` finds all roots in bounds
- Configure via `setUpperBound()`, `setLowerBound()`, `setIterations()`, `setConvergenceCriteria()`

**linearalgebra/** - Vectors and Matrices

- `Vector` and `Matrix` classes with arithmetic operations
- `LUDecomposition`, `QRDecomposition` for advanced operations
- Parser integrates via `NodeVector` / `NodeMatrix`

**distributions/** - Probability distributions

- Base classes: `DiscreteProbabilityDistribution`, `ContinuousProbabilityDistribution`
- Implementations: Normal, Beta, Binomial, Exponential, F, Logistic, Student T
- Methods: `.density()`, `.cumulative()`

**unitconversion/** - Unit conversion engine

- `ConversionEngine` with flexible string-based unit matching
- Supports currencies (`.updateCurrencies()` for live rates)
- Supports timezones (`.updateTimeZones()` for city-based conversion)
- Unit definitions stored in external XML/JSON files

**special/** - Special mathematical functions

- `Gamma`, `Beta`, `Erf` (error function)
- `Primes`: `isPrime()`, `nextPrime()`, `primeFactors()`

**regression/** - Regression models

- `LinearRegressionModel` and base `RegressionModel`
- Returns best-fit functions from sample data

**plotting/** - Graphical function plotting

- `Grapher` provides interactive pan/zoom graphing control

**gui/** - Graphical interfaces

- `MainFrame`: Expression evaluator interface
- `Converter`: Unit conversion GUI
- `HistoricalTextField`: Reusable text field with arrow-key history

## Important Implementation Notes

### System Operator Protection

Custom functions **cannot redefine system operators**. The parser enforces this via checks in `EvaluationContext.addOperator()`.
System operators include all built-in operators registered by `OperatorProvider` methods.

Attempting to redefine system operators (e.g., `sin(x) := x + 2`) will throw an exception.

### Array Handling

When working with vectors/matrices in the parser:

- Prefer using `System.arraycopy()` for array copies (not manual loops)
- Node normalization handles size mismatches automatically
- Single-element containers auto-convert to scalars where appropriate

### Operator Precedence

Operators have precedence values (lower = higher priority):

- Power (^): 1
- Multiply (*), Divide (/): 2
- Add (+), Subtract (-): 3
- Logical operators: 4-6
- Custom operators: Configurable

### Test Organization

Tests mirror the main source structure:

- `src/test/java/uk/co/ryanharrison/mathengine/...`
- Parser tests: `EvaluatorCustomFunctionTest`, `EvaluatorSimpleBinaryTest`, node tests
- Operator tests: `parser/operators/binary/*Test.java`, `parser/operators/unary/*Test.java`
- Package tests: `differential/`, `unitconversion/`, `regression/`, etc.

Use AssertJ for assertions (`assertThat(...).isEqualTo(...)`) rather than JUnit assertions.

## Code Quality Standards & Refactoring Guidelines

This section documents the coding standards and refactoring patterns established during the distributions package refactoring.
Apply these principles when refactoring other packages.

### Immutability & Modern Java Practices

**Prefer Immutability**:

- All fields should be `private final`
- No setter methods - use builders or factory methods for construction
- Validate parameters in constructors/factory methods and throw `IllegalArgumentException` for invalid inputs
- Pre-compute expensive calculations in constructors (e.g., logarithms, normalization factors)

**Factory Methods vs Constructors**:

```java
// GOOD: Static factory methods with descriptive names
public static NormalDistribution standard() {
    return new NormalDistribution(0.0, 1.0);
}

public static NormalDistribution of(double mean, double standardDeviation) {
    if (standardDeviation <= 0.0) {
        throw new IllegalArgumentException("Standard deviation must be positive, got: " + standardDeviation);
    }
    return new NormalDistribution(mean, standardDeviation);
}

// BAD: Public constructors with unclear parameter order
public NormalDistribution(double a, double b) { ...}
```

**Builder Pattern**:

- Use builders for classes with 3+ parameters or optional configurations, otherwise for pure functions keep them static
- Builders enable named parameters and default values
- Validate in builder methods (not just in `build()`)

```java
public static Builder builder() {
    return new Builder();
}

public static final class Builder {
    private double mean = 0.0;  // Sensible defaults
    private double standardDeviation = 1.0;

    public Builder mean(double mean) {
        this.mean = mean;
        return this;
    }

    public Builder standardDeviation(double standardDeviation) {
        if (standardDeviation <= 0.0) {
            throw new IllegalArgumentException("Must be positive, got: " + standardDeviation);
        }
        this.standardDeviation = standardDeviation;
        return this;
    }

    public NormalDistribution build() {
        return new NormalDistribution(mean, standardDeviation);
    }
}
```

**Interfaces Over Abstract Classes**:

- Prefer interfaces for contracts/type hierarchies
- Use `default` methods sparingly - only for utility methods
- Abstract classes are acceptable only when sharing implementation code

```java
// GOOD: Interface defining contract
public interface ContinuousProbabilityDistribution extends ProbabilityDistribution {
    double density(double x);

    double cumulative(double x);

    double inverseCumulative(double p);
}

// GOOD: Immutable implementation
public final class NormalDistribution implements ContinuousProbabilityDistribution {
    private final double mean;
    private final double standardDeviation;
    // ...
}
```

### Comprehensive Javadoc Standards

**Class-Level Documentation**:

- Start with brief description of what the class represents
- Include mathematical formulas and definitions
- Provide parameter descriptions (what they represent mathematically)
- Include usage examples in `<pre>{@code ...}</pre>` blocks
- List key properties (mean, variance formulas)

```java
/**
 * Immutable implementation of the Normal (Gaussian) {@link ContinuousProbabilityDistribution}.
 * <p>
 * The normal distribution is a continuous probability distribution characterized by
 * its bell-shaped curve. It is defined by two parameters:
 * </p>
 * <ul>
 *     <li><b>μ (mu)</b>: the mean, which determines the center of the distribution</li>
 *     <li><b>σ (sigma)</b>: the standard deviation, which determines the spread</li>
 * </ul>
 * <p>
 * The probability density function is:
 * <br>
 * f(x) = (1 / (σ√(2π))) * exp(-((x-μ)² / (2σ²)))
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Standard normal distribution (mean=0, stddev=1)
 * NormalDistribution standard = NormalDistribution.standard();
 *
 * // Custom distribution using builder
 * NormalDistribution custom = NormalDistribution.builder()
 *     .mean(100)
 *     .standardDeviation(15)
 *     .build();
 * }</pre>
 *
 */
```

**Method-Level Documentation**:

- Document what the method computes/returns
- Specify valid input ranges and what happens for invalid inputs
- Use `@throws` tags for all exception cases
- Reference related methods with `{@link}`

```java
/**
 * Calculates the cumulative distribution function (CDF) at the given point.
 * <p>
 * The CDF represents the probability that the random variable is less than or
 * equal to {@code x}. Mathematically, this is the integral of the PDF from
 * negative infinity to {@code x}.
 * </p>
 *
 * @param x the upper bound of the probability calculation
 * @return the probability that a random variable is less than or equal to {@code x},
 *         a value in the range [0, 1]
 * @throws IllegalArgumentException if {@code x} is outside the valid range for this distribution
 */
public double cumulative(double x);
```

### Comprehensive Test Suite Design

**Test Organization**:

- Group tests by functionality using comments: `// ==================== Section ====================`
- Order: Construction → Properties → Density → Cumulative → Inverse → Equality → Edge Cases → Immutability

**Parameterized Tests**:

- Use `@ParameterizedTest` with `@CsvSource` for testing multiple values
- Prefer parameterized tests over copy-pasted test methods
- Include descriptive values in CSV (actual parameter names as comments)

```java

@ParameterizedTest
@CsvSource({
        "0.0, 0.5",                 // mean, expected cumulative at mean
        "10.0, 0.5",
        "-5.0, 0.5"
})
void cumulativeAtMeanIsHalf(double mean, double expected) {
    NormalDistribution dist = NormalDistribution.of(mean, 1.0);
    assertThat(dist.cumulative(mean)).isCloseTo(expected, within(TOLERANCE));
}
```

**Test Data & Tolerances**:

- Define tolerance constants at class level
- Use strict tolerance (1e-9) for exact calculations
- Use relaxed tolerance (1e-6 or 1e-7) for approximations (Gamma, Erf, etc.)
- Update expected values to match actual implementation (don't force incorrect values)

```java
private static final double TOLERANCE = 1e-7;          // For Gamma/Erf approximations
private static final double RELAXED_TOLERANCE = 1e-6;  // For iterative algorithms
```

**Multiple Assertions Per Test**:

- For related properties, test multiple values in one test method
- This prevents test explosion while maintaining clarity
- Use loops for monotonicity checks

```java

@Test
void cumulativeIsMonotonicallyIncreasing() {
    NormalDistribution dist = NormalDistribution.of(5.0, 2.0);

    double prev = 0.0;
    for (double x = -10.0; x <= 20.0; x += 0.5) {
        double current = dist.cumulative(x);
        assertThat(current).isGreaterThanOrEqualTo(prev);
        prev = current;
    }
}
```

**Edge Cases & Validation**:

- Test boundary conditions (0, 1, min, max values)
- Test invalid inputs throw appropriate exceptions
- Test extreme values don't produce NaN/Infinity (unless mathematically correct)
- Test immutability by calling methods and verifying state unchanged

```java

@Test
void densityRejectsNegativeX() {
    ExponentialDistribution dist = ExponentialDistribution.of(1.0);

    assertThatThrownBy(() -> dist.density(-0.1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("non-negative");
}

@Test
void distributionIsImmutable() {
    NormalDistribution dist = NormalDistribution.of(5.0, 2.0);

    double originalMean = dist.getMean();

    // Perform operations
    dist.density(10.0);
    dist.cumulative(10.0);

    // Verify state unchanged
    assertThat(dist.getMean()).isEqualTo(originalMean);
}
```

**Test Coverage Goals**:

- All public methods must have tests
- All error conditions must be tested
- Statistical properties should be verified (not just implementation details)
- Test both typical and edge cases

**AssertJ Usage**:

- Always use AssertJ over JUnit assertions
- Use descriptive assertions: `isCloseTo()`, `isEqualTo()`, `isGreaterThan()`
- Use `within()` for floating-point comparisons
- Use `assertThatThrownBy()` for exception testing

### Code Structure & Naming

**Method Naming**:

- Getters: `getMean()`, `getStandardDeviation()`, `getVariance()`
- Factory methods: `of()`, `standard()`, `withMean()`, `builder()`
- Avoid abbreviations: `standardDeviation` not `stdDev`

**Field Naming**:

- Use full mathematical names: `mean`, `standardDeviation`, `degreesOfFreedom`
- Pre-computed values: prefix with purpose: `logNormalizationFactor`, `halfDegreesPlus1`

**Constant Naming**:

- Mathematical constants: `ONE_OVER_SQRT_2PI`, `PI_OVER_SQRT3`
- Always include documentation explaining the value

**Error Messages**:

- Include parameter name and actual value in error messages
- Be specific about what's wrong and what's expected

```java
if(standardDeviation <=0.0){
        throw new

IllegalArgumentException(
        "Standard deviation must be positive, got: "+standardDeviation);
}
```

### Object Methods

**Always Override**:

- `equals()` - compare all fields with `Double.compare()` for doubles
- `hashCode()` - use `Objects.hash()` with all fields
- `toString()` - include class name and key parameters using `String.format()`

```java

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NormalDistribution that)) return false;
    return Double.compare(that.mean, mean) == 0 &&
            Double.compare(that.standardDeviation, standardDeviation) == 0;
}

@Override
public int hashCode() {
    return Objects.hash(mean, standardDeviation);
}

@Override
public String toString() {
    return String.format("NormalDistribution(μ=%.4f, σ=%.4f)", mean, standardDeviation);
}
```

## Common Patterns

### Adding New Operators

1. Extend `BinaryOperator` or `UnaryOperator`
2. Implement abstract `toResult()` method
3. Define `getAliases()` for string representations
4. Set precedence via `getPrecedence()`
5. Register in `OperatorProvider` (e.g., `customOperators()` method)
6. Add to appropriate `Evaluator` factory method

For simple numeric operations, use template classes:

- `SimpleBinaryOperator` - Binary arithmetic on numbers
- `NumberOperator` - Unary operations on numbers/vectors/matrices
- `VectorOperator` / `MatrixOperator` - Type-specific operations

### Evaluating Expressions Programmatically

```java
Evaluator eval = Evaluator.newEvaluator();
eval.

addVariable("x","5");

NodeConstant result = eval.evaluateConstant("{1,2,3} * x");
// Returns: NodeVector with {5, 10, 15}
```

### Working with Functions

```java
// Create and evaluate
Function f = new Function("x^2 + 8*x + 12");
double value = f.evaluateAt(3.5);

// Differentiate symbolically
Differentiator diff = new Differentiator();
Function derivative = diff.differentiate(f, true);
// derivative.getEquation() = "2*x+8"

// Integrate numerically
SimpsonIntegrator integrator = new SimpsonIntegrator(f);
integrator.

setLower(0.5);
integrator.

setUpper(5.0);
integrator.

setIterations(100);

double area = integrator.integrate();

// Find roots
BrentSolver solver = new BrentSolver(f);
solver.

setLowerBound(-10);
solver.

setUpperBound(10);

List<Double> roots = solver.solveAll();
```

## File Structure Quick Reference

```
src/main/java/uk/co/ryanharrison/mathengine/
├── Function.java                      # Main function abstraction
├── Utils.java, MathUtils.java         # Utility methods
├── BigRational.java                   # Arbitrary precision fractions
│
├── parser/                            # Expression parser (CORE)
│   ├── Evaluator.java                 # Entry point
│   ├── EvaluationContext.java         # State/registry
│   ├── ExpressionParser.java          # String → tree
│   ├── RecursiveDescentParser.java    # Tree → result
│   ├── AngleUnit.java                 # Radians/Degrees
│   ├── nodes/                         # Node types
│   │   ├── Node*.java                 # NodeConstant hierarchy
│   │   └── NodeTransformer.java       # Type conversions
│   └── operators/                     # Operator implementations
│       ├── OperatorProvider.java      # Factory/registry
│       ├── binary/                    # Two-argument operators
│       └── unary/                     # Single-argument operators
│
├── differential/                      # Differentiation
│   ├── NumericalDifferentiationMethod.java
│   └── symbolic/Differentiator.java
│
├── integral/                          # Integration methods
├── solvers/                           # Root finding
├── distributions/                     # Probability distributions
├── linearalgebra/                     # Vector/Matrix
├── regression/                        # Regression models
├── unitconversion/                    # Unit conversion
├── special/                           # Special functions
├── plotting/                          # Graphing
└── gui/                               # GUI applications
```
