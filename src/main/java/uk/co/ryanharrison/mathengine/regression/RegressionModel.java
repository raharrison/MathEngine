package uk.co.ryanharrison.mathengine.regression;

/**
 * Interface for regression models that fit mathematical functions to data points.
 * <p>
 * Regression models analyze the relationship between independent variable(s) and a
 * dependent variable by finding the best-fit function that minimizes the error between
 * predicted and observed values. This interface defines the contract for single-variable
 * regression models.
 * </p>
 *
 * <h2>Mathematical Definition:</h2>
 * <p>
 * Given a set of data points {(x₁, y₁), (x₂, y₂), ..., (xₙ, yₙ)}, a regression model
 * finds a function f(x) that best approximates the relationship between x and y, typically
 * by minimizing the sum of squared residuals:
 * <br>
 * min Σ(yᵢ - f(xᵢ))²
 * </p>
 *
 * <h2>Key Concepts:</h2>
 * <ul>
 *     <li><b>X Values (Independent Variable)</b>: The input data points</li>
 *     <li><b>Y Values (Dependent Variable)</b>: The output/response data points</li>
 *     <li><b>Coefficients/Parameters</b>: The computed values that define the fitted function</li>
 *     <li><b>Evaluation</b>: Using the fitted function to predict y values for new x values</li>
 * </ul>
 *
 * <h2>Available Implementations:</h2>
 * <ul>
 *     <li>{@link LinearRegressionModel} - Fits a straight line: y = a + bx</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Sample data points
 * double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
 * double[] yValues = {2.1, 3.9, 6.2, 8.1, 9.9};
 *
 * // Fit a linear regression model
 * RegressionModel model = LinearRegressionModel.of(xValues, yValues);
 *
 * // Get coefficients
 * double[] coeffs = model.getCoefficients(); // {intercept, slope}
 *
 * // Predict y for a new x value
 * double predicted = model.evaluateAt(6.0); // Predicts y for x=6.0
 * }</pre>
 *
 * <h2>Implementation Guidelines:</h2>
 * <p>
 * Implementations should be immutable and thread-safe. All computation should occur
 * during construction (typically via factory methods or builders). Models should
 * validate input data during construction and throw {@link IllegalArgumentException}
 * for invalid inputs such as:
 * </p>
 * <ul>
 *     <li>Null or empty arrays</li>
 *     <li>Mismatched array lengths</li>
 *     <li>Insufficient data points for the model type</li>
 *     <li>Degenerate cases (e.g., all x values identical)</li>
 * </ul>
 *
 * @author Ryan Harrison
 */
public interface RegressionModel {
    /**
     * Returns the independent variable (X) data points used to fit this model.
     * <p>
     * The returned array is a defensive copy to preserve immutability.
     * </p>
     *
     * @return a copy of the X data points
     */
    double[] getXValues();

    /**
     * Returns the dependent variable (Y) data points used to fit this model.
     * <p>
     * The returned array is a defensive copy to preserve immutability.
     * </p>
     *
     * @return a copy of the Y data points
     */
    double[] getYValues();

    /**
     * Returns the coefficients or parameters of the fitted regression model.
     * <p>
     * The interpretation and order of coefficients depends on the specific regression
     * model implementation. For example, a linear model might return {intercept, slope},
     * while a polynomial model might return {a₀, a₁, a₂, ...}.
     * </p>
     * <p>
     * The returned array is a defensive copy to preserve immutability.
     * </p>
     *
     * @return a copy of the computed regression coefficients
     */
    double[] getCoefficients();

    /**
     * Evaluates the fitted regression function at the specified point.
     * <p>
     * This method uses the computed coefficients to predict the y value for a given x value.
     * The prediction can be made for any x value, including values outside the range
     * of the original data (extrapolation), though extrapolation should be used with caution.
     * </p>
     *
     * @param x the independent variable value at which to evaluate the model
     * @return the predicted dependent variable value y = f(x)
     * @throws ArithmeticException if evaluation produces non-finite values (NaN or infinity)
     */
    double evaluateAt(double x);
}
