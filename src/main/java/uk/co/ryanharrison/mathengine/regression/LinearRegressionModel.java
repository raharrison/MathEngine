package uk.co.ryanharrison.mathengine.regression;

import uk.co.ryanharrison.mathengine.StatUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable implementation of linear regression using the least squares method.
 * <p>
 * Linear regression fits a straight line to a set of data points by minimizing
 * the sum of squared vertical distances (residuals) between the observed y values
 * and the predicted y values from the linear function.
 * </p>
 *
 * <h2>Mathematical Model:</h2>
 * <p>
 * The linear regression model has the form:
 * <br>
 * <b>y = a + bx</b>
 * <br>
 * where:
 * </p>
 * <ul>
 *     <li><b>a</b> (intercept): The y-value when x = 0</li>
 *     <li><b>b</b> (slope/gradient): The change in y for a unit change in x</li>
 * </ul>
 *
 * <h2>Least Squares Formulas:</h2>
 * <p>
 * The coefficients are computed using:
 * <br>
 * <b>b = Cov(x,y) / Var(x)</b>
 * <br>
 * <b>a = ȳ - b·x̄</b>
 * <br>
 * where:
 * </p>
 * <ul>
 *     <li>Cov(x,y) = covariance between x and y</li>
 *     <li>Var(x) = variance of x</li>
 *     <li>x̄ = mean of x values</li>
 *     <li>ȳ = mean of y values</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Example 1: Simple linear relationship (y ≈ 2x + 1)
 * double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
 * double[] y = {3.1, 4.9, 7.2, 8.8, 11.1};
 *
 * LinearRegressionModel model = LinearRegressionModel.of(x, y);
 * double intercept = model.getIntercept(); // ≈ 1.0
 * double slope = model.getSlope();         // ≈ 2.0
 *
 * double predicted = model.evaluateAt(6.0); // ≈ 13.0
 *
 * // Example 2: Using builder
 * LinearRegressionModel model2 = LinearRegressionModel.builder()
 *     .xValues(x)
 *     .yValues(y)
 *     .build();
 *
 * // Example 3: Accessing coefficients
 * double[] coeffs = model.getCoefficients(); // {intercept, slope}
 * }</pre>
 *
 * <h2>Statistical Properties:</h2>
 * <ul>
 *     <li><b>Best Linear Unbiased Estimator (BLUE)</b>: Under the Gauss-Markov assumptions,
 *         least squares provides the minimum variance among linear unbiased estimators</li>
 *     <li><b>Residuals sum to zero</b>: Σ(yᵢ - ŷᵢ) = 0</li>
 *     <li><b>Regression line passes through centroid</b>: The line passes through (x̄, ȳ)</li>
 * </ul>
 *
 * <h2>Limitations and Assumptions:</h2>
 * <ul>
 *     <li>Assumes a linear relationship between x and y</li>
 *     <li>Sensitive to outliers (squared errors magnify outlier influence)</li>
 *     <li>Requires variance in x (all x values cannot be identical)</li>
 *     <li>Requires at least 2 data points (preferably many more for reliability)</li>
 *     <li>Does not indicate goodness of fit (consider adding R² calculation)</li>
 * </ul>
 *
 * <h2>Thread Safety:</h2>
 * <p>
 * This class is immutable and thread-safe. All internal state is final and
 * defensive copies are made of input/output arrays.
 * </p>
 *
 * @author Ryan Harrison
 */
public final class LinearRegressionModel implements RegressionModel {
    /**
     * Minimum number of data points required for linear regression.
     */
    private static final int MIN_DATA_POINTS = 2;

    private final double[] xValues;
    private final double[] yValues;
    private final double intercept;  // coefficient 'a'
    private final double slope;      // coefficient 'b'

    /**
     * Private constructor for factory methods and builder.
     * Validates inputs and computes regression coefficients.
     *
     * @param xValues the independent variable data points
     * @param yValues the dependent variable data points
     * @throws IllegalArgumentException if validation fails or computation is impossible
     */
    private LinearRegressionModel(double[] xValues, double[] yValues) {
        validateInputs(xValues, yValues);

        // Store defensive copies to ensure immutability
        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);

        // Compute regression coefficients immediately
        try {
            double xVariance = StatUtils.variance(xValues);

            if (xVariance == 0.0) {
                throw new IllegalArgumentException(
                        "All x values are identical (variance = 0). Cannot perform linear regression.");
            }

            double covariance = StatUtils.covariance(xValues, yValues);
            this.slope = covariance / xVariance;
            this.intercept = StatUtils.mean(yValues) - this.slope * StatUtils.mean(xValues);

            if (!Double.isFinite(this.intercept) || !Double.isFinite(this.slope)) {
                throw new ArithmeticException(
                        "Regression computation produced non-finite coefficients: " +
                                "intercept=" + this.intercept + ", slope=" + this.slope);
            }
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Failed to compute regression coefficients: " + e.getMessage(), e);
        }
    }

    /**
     * Validates input arrays for regression computation.
     *
     * @param xValues the x data points
     * @param yValues the y data points
     * @throws IllegalArgumentException if validation fails
     */
    private static void validateInputs(double[] xValues, double[] yValues) {
        if (xValues == null) {
            throw new IllegalArgumentException("X values cannot be null");
        }
        if (yValues == null) {
            throw new IllegalArgumentException("Y values cannot be null");
        }
        if (xValues.length < MIN_DATA_POINTS) {
            throw new IllegalArgumentException(
                    "Insufficient x data points for linear regression. Required: " + MIN_DATA_POINTS +
                            ", got: " + xValues.length);
        }
        if (yValues.length < MIN_DATA_POINTS) {
            throw new IllegalArgumentException(
                    "Insufficient y data points for linear regression. Required: " + MIN_DATA_POINTS +
                            ", got: " + yValues.length);
        }
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException(
                    "X and Y arrays must have the same length. X length: " + xValues.length +
                            ", Y length: " + yValues.length);
        }

        // Check for non-finite values
        for (int i = 0; i < xValues.length; i++) {
            if (!Double.isFinite(xValues[i])) {
                throw new IllegalArgumentException(
                        "X value at index " + i + " is not finite: " + xValues[i]);
            }
            if (!Double.isFinite(yValues[i])) {
                throw new IllegalArgumentException(
                        "Y value at index " + i + " is not finite: " + yValues[i]);
            }
        }
    }

    /**
     * Creates a LinearRegressionModel from the given data points.
     * <p>
     * This method immediately computes the regression coefficients. The arrays are
     * defensively copied to ensure immutability.
     * </p>
     *
     * @param xValues the independent variable data points
     * @param yValues the dependent variable data points
     * @return a new LinearRegressionModel fitted to the data
     * @throws IllegalArgumentException if inputs are invalid or regression cannot be computed
     */
    public static LinearRegressionModel of(double[] xValues, double[] yValues) {
        return new LinearRegressionModel(xValues, yValues);
    }

    /**
     * Creates a new builder for constructing a LinearRegressionModel with named parameters.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public double[] getXValues() {
        return Arrays.copyOf(xValues, xValues.length);
    }

    @Override
    public double[] getYValues() {
        return Arrays.copyOf(yValues, yValues.length);
    }

    @Override
    public double[] getCoefficients() {
        return new double[]{intercept, slope};
    }

    /**
     * Returns the y-intercept (constant term) of the fitted line.
     * <p>
     * This is the value of y when x = 0, denoted as 'a' in the equation y = a + bx.
     * </p>
     *
     * @return the y-intercept coefficient
     */
    public double getIntercept() {
        return intercept;
    }

    /**
     * Returns the slope (gradient) of the fitted line.
     * <p>
     * This represents the change in y for a unit change in x, denoted as 'b'
     * in the equation y = a + bx.
     * </p>
     *
     * @return the slope coefficient
     */
    public double getSlope() {
        return slope;
    }

    @Override
    public double evaluateAt(double x) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException("X value must be finite, got: " + x);
        }

        double result = intercept + slope * x;

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Evaluation produced non-finite value at x = " + x + ": " + result);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinearRegressionModel that)) return false;
        return Double.compare(that.intercept, intercept) == 0 &&
                Double.compare(that.slope, slope) == 0 &&
                Arrays.equals(xValues, that.xValues) &&
                Arrays.equals(yValues, that.yValues);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(intercept, slope);
        result = 31 * result + Arrays.hashCode(xValues);
        result = 31 * result + Arrays.hashCode(yValues);
        return result;
    }

    @Override
    public String toString() {
        return String.format("LinearRegressionModel(y = %.4f + %.4f·x, n=%d)",
                intercept, slope, xValues.length);
    }

    /**
     * Builder for constructing {@link LinearRegressionModel} instances with named parameters.
     * <p>
     * Both xValues and yValues are required. The builder validates parameters and
     * computes the regression coefficients when {@link #build()} is called.
     * </p>
     *
     * <h2>Usage Example:</h2>
     * <pre>{@code
     * double[] x = {1, 2, 3, 4, 5};
     * double[] y = {2, 4, 5, 4, 5};
     *
     * LinearRegressionModel model = LinearRegressionModel.builder()
     *     .xValues(x)
     *     .yValues(y)
     *     .build();
     * }</pre>
     */
    public static final class Builder {
        private double[] xValues;
        private double[] yValues;

        private Builder() {
        }

        /**
         * Sets the independent variable (X) data points.
         * <p>
         * The array is defensively copied to ensure immutability.
         * </p>
         *
         * @param xValues the x data points
         * @return this builder for method chaining
         * @throws IllegalArgumentException if xValues is null
         */
        public Builder xValues(double[] xValues) {
            if (xValues == null) {
                throw new IllegalArgumentException("X values cannot be null");
            }
            this.xValues = Arrays.copyOf(xValues, xValues.length);
            return this;
        }

        /**
         * Sets the dependent variable (Y) data points.
         * <p>
         * The array is defensively copied to ensure immutability.
         * </p>
         *
         * @param yValues the y data points
         * @return this builder for method chaining
         * @throws IllegalArgumentException if yValues is null
         */
        public Builder yValues(double[] yValues) {
            if (yValues == null) {
                throw new IllegalArgumentException("Y values cannot be null");
            }
            this.yValues = Arrays.copyOf(yValues, yValues.length);
            return this;
        }

        /**
         * Builds the LinearRegressionModel with the configured data points.
         * <p>
         * This method validates the inputs and computes the regression coefficients.
         * </p>
         *
         * @return a new LinearRegressionModel instance
         * @throws IllegalStateException    if required parameters are not set
         * @throws IllegalArgumentException if inputs are invalid or regression cannot be computed
         */
        public LinearRegressionModel build() {
            if (xValues == null) {
                throw new IllegalStateException("X values must be set");
            }
            if (yValues == null) {
                throw new IllegalStateException("Y values must be set");
            }

            return new LinearRegressionModel(xValues, yValues);
        }
    }
}
