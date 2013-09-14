package uk.co.ryanharrison.mathengine.regression;

/**
 * Represents a regression model with one defined independent variable. Provides
 * operations to compute the regression coefficients and evaluate the resulting
 * function at a certain point.
 * 
 */
public abstract class RegressionModel
{
	/** The X values of the data set points */
	protected double[] xValues;

	/** The X values of the data set points */
	protected double[] yValues;

	/** Have the unknown parameters been calculated yet? */
	protected boolean computed;

	/**
	 * Construct a new RegressionModel object with the specified data points
	 * 
	 * @param x
	 *            The X data points
	 * @param y
	 *            The Y data points
	 */
	public RegressionModel(double[] x, double[] y)
	{
		this.xValues = x;
		this.yValues = y;
		computed = false;
	}

	/**
	 * Get the X data points
	 * 
	 * @return The X data points
	 */
	public double[] getXValues()
	{
		return this.xValues;
	}

	/**
	 * Get the Y data points
	 * 
	 * @return The Y data points
	 */
	public double[] getYValues()
	{
		return this.yValues;
	}

	/**
	 * Get the computed coefficients from the model that best fit the data set
	 * 
	 * @return The computed coefficients or parameters that have been fitted to
	 *         the data set
	 */
	public abstract double[] getCoefficients();

	/**
	 * Find the best fit values of the unknown parameters in this regression
	 * model
	 */
	public abstract void compute();

	/**
	 * Evaluate the computed model at a point x
	 * 
	 * @param x
	 *            The point to evaluate the model at
	 * @return The underlying models computed function evaluated at x
	 */
	public abstract double evaluateAt(double x);
}