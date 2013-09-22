package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Represents a numerical method of estimating the integral of function between
 * two points
 * 
 * @author Ryan Harrison
 * 
 */
public abstract class IntegrationMethod
{
	/** The number of iterations that can be used when estimating the integral */
	protected int iterations;

	/** The function to estimate the integral of */
	protected Function targetFunction;

	/** The lower bound of the integral to estimate */
	protected double lower;

	/** The upper bound of the integral to estimate */
	protected double upper;

	/**
	 * Construct a new IntegrationMethod object with the specified target
	 * function
	 * 
	 * This sets default values to the field 10 iterations between 1 and 0
	 * 
	 * @param function
	 *            The function to estimate the integral of
	 */
	public IntegrationMethod(Function function)
	{
		this.setTargetFunction(function);
		this.setIterations(10);
		this.setUpper(1);
		this.setLower(0);
	}

	/**
	 * Get the number of iterations used when estimating the integral
	 * 
	 * @return The number of iterations used when estimating the integral
	 */
	public int getIterations()
	{
		return iterations;
	}

	/**
	 * Get the lower bound of the integral
	 * 
	 * @return The lower bound of the integral
	 */
	public double getLower()
	{
		return lower;
	}

	/**
	 * Get the function that the integral will be estimated for
	 * 
	 * @return The function that the integral will be estimated for
	 */
	public Function getTargetFunction()
	{
		return targetFunction;
	}

	/**
	 * Get the upper bound of the integral
	 * 
	 * @return The upper bound of the integral
	 */
	public double getUpper()
	{
		return upper;
	}

	/**
	 * Perform an estimation of the integral of the target function between the
	 * lower and upper bounds within the specified number of iterations
	 * 
	 * This will use the current integral estimation method
	 * 
	 * @return An estimation of the integral of the target function between the
	 *         lower and upper bounds
	 */
	public abstract double integrate();

	/**
	 * Set the number of iterations to use when estimating the integral
	 * 
	 * @param iterations
	 *            The new number of iterations to use when estimating the
	 *            integral
	 */
	public void setIterations(int iterations)
	{
		this.iterations = Math.abs(iterations);
	}

	/**
	 * Set the lower bound of the integral to estimate
	 * 
	 * @param lower
	 *            The new lower bound
	 */
	public void setLower(double lower)
	{
		this.lower = lower;
	}

	/**
	 * Set the target function of which the integral will be estimated for
	 * 
	 * @param targetFunction
	 *            The new target function
	 */
	public void setTargetFunction(Function targetFunction)
	{
		this.targetFunction = targetFunction;
	}

	/**
	 * Set the upper bound of the integral to estimate
	 * 
	 * @param upper
	 *            The new upper bound
	 */
	public void setUpper(double upper)
	{
		this.upper = upper;
	}
}
