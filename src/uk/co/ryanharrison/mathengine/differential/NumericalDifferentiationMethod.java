package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing a way of numerically estimating the derivatives of a
 * function using finite difference approximations
 * 
 * @author Ryan Harrison
 * 
 */
public abstract class NumericalDifferentiationMethod
{
	/** The target point of differentiation */
	protected double targetPoint;

	/**
	 * The change in the value of x to use when numerically estimating the
	 * derivative using finite difference approximations
	 */
	protected double h;

	/** The function to estimate the derivative of */
	protected Function targetFunction;

	/**
	 * Whether or not to attempt to optimize the value of h into a sufficiently
	 * small number to maximise precision whilst minimizing rounding errors
	 */
	protected boolean optimize;

	/**
	 * Construct a new instance with the specified target {@link Function}
	 * 
	 * @param function
	 *            The target function to estimate the derivative of
	 */
	public NumericalDifferentiationMethod(Function function)
	{
		this(function, 0.01);
		this.optimize = true;
	}

	/**
	 * Construct a new instance with the specified target {@link Function} and
	 * change in the value of x to use when numerically estimating the
	 * derivative using finite difference approximations
	 * 
	 * @param function
	 *            The target function to estimate the derivative of
	 * @param h
	 *            The change in the value of x to use when numerically
	 *            estimating the derivative using finite difference
	 *            approximations. This value should be sufficiently small to
	 *            increase accuracy but large enough to prevent rounding errors
	 */
	public NumericalDifferentiationMethod(Function function, double h)
	{
		this.targetFunction = function;
		this.h = h;
		this.optimize = false;
		this.targetPoint = 1;
	}

	/**
	 * Estimate the value of the first derivative of the target function at the
	 * target point
	 * 
	 * @return An estimate the value of the first derivative of the target
	 *         function at the target point
	 */
	public abstract double deriveFirst();

	/**
	 * Estimate the value of the fourth derivative of the target function at the
	 * target point
	 * 
	 * @return An estimate the value of the fourth derivative of the target
	 *         function at the target point
	 */
	public abstract double deriveFourth();

	/**
	 * Estimate the value of the second derivative of the target function at the
	 * target point
	 * 
	 * @return An estimate the value of the second derivative of the target
	 *         function at the target point
	 */
	public abstract double deriveSecond();

	/**
	 * Estimate the value of the third derivative of the target function at the
	 * target point
	 * 
	 * @return An estimate the value of the third derivative of the target
	 *         function at the target point
	 */
	public abstract double deriveThird();

	/**
	 * Get the change in the value of x to use when numerically estimating the
	 * derivative using finite difference approximations. This value should be
	 * sufficiently small to increase accuracy but large enough to prevent
	 * rounding errors
	 * 
	 * @return The change in the value of x to use when numerically estimating
	 *         the derivative using finite difference approximations
	 */
	public double getH()
	{
		return h;
	}

	/**
	 * Get the target function to derive
	 * 
	 * @return The target function to be derived
	 */
	public Function getTargetFunction()
	{
		return targetFunction;
	}

	/**
	 * Get the target point of differentiation
	 * 
	 * @return The target point of differentiation
	 */
	public double getTargetPoint()
	{
		return targetPoint;
	}

	/**
	 * Whether or not to attempt to optimize the value of h into a sufficiently
	 * small number to maximise precision whilst minimizing rounding errors
	 * 
	 * @return True if optimization of h will take place, otherwise false
	 */
	public boolean isOptimize()
	{
		return optimize;
	}

	/**
	 * Optimize the value of h into a sufficiently small number to maximise
	 * precision whilst minimizing rounding errors.
	 * 
	 * The value returned must not be zero but small enough to produce accurate
	 * results
	 * 
	 * @param x
	 *            The value to optimize
	 * @return The optimized value of h
	 */
	protected double optimizeH(double x)
	{
		if (Math.abs(x - 0) < 1E-9)
			return h;

		double h = Math.sqrt(2.2E-16) * x;
		double temp = x + h;
		return temp - x;
	}

	/**
	 * Get the change in the value of x to use when numerically estimating the
	 * derivative using finite difference approximations. This value should be
	 * sufficiently small to increase accuracy but large enough to prevent
	 * rounding errors
	 * 
	 * @param h
	 *            The new value of h
	 */
	public void setH(double h)
	{
		this.h = h;
	}

	/**
	 * Whether or not to attempt to optimize the value of h into a sufficiently
	 * small number to maximise precision whilst minimizing rounding errors
	 * 
	 * @param optimize
	 *            Whether or not to attempt to optimize the value of h
	 */
	public void setOptimize(boolean optimize)
	{
		this.optimize = optimize;
	}

	/**
	 * Set the target function to derive
	 * 
	 * @return The new target function to be derived
	 */
	public void setTargetFunction(Function targetFunction)
	{
		this.targetFunction = targetFunction;
	}

	/**
	 * Set the target point of differentiation
	 * 
	 * @return The new target point of differentiation
	 */
	public void setTargetPoint(double targetPoint)
	{
		this.targetPoint = targetPoint;
	}
}
