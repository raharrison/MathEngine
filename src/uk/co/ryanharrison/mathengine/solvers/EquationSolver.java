package uk.co.ryanharrison.mathengine.solvers;

import java.util.ArrayList;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing an equation solver that can be used to estimate the roots
 * of a function.
 * 
 * This class is inherited from to produce individual classes that represent
 * different numerical root approximation algorithms
 * 
 * A root of a function is where the X value upon which the Y value results in
 * zero
 * 
 * @author Ryan Harrison
 * 
 */
public abstract class EquationSolver
{
	/** The function to estimate the roots of */
	protected Function targetFunction;

	/** The tolerance in which the estimated roots must be within */
	protected double tolerance;

	/** The number of iterations that can be used when estimating the roots */
	protected int iterations;

	/**
	 * The criteria used to decide whether or not the root finding algorithm has
	 * converged
	 */
	protected ConvergenceCriteria convergenceCriteria;

	/**
	 * Construct a new equation solver. This sets default values to the
	 * numerical fields
	 */
	public EquationSolver()
	{
		iterations = 1;
		tolerance = 1E-5;
		convergenceCriteria = ConvergenceCriteria.NumberOfIterations;
	}

	/**
	 * Check that the root finding parameters are all valid
	 * 
	 * @exception IllegalArgumentException
	 *                If the number of iterations is less than or equal to zero
	 */
	protected void checkRootFindingParams()
	{
		if (iterations <= 0)
		{
			throw new IllegalArgumentException("Iterations cannot be less than zero");
		}
	}

	/**
	 * Calculate a series of {@link RootInterval} objects in which a root may
	 * lie upon which this equation solver can be used on.
	 * 
	 * The solver can then find roots across a wider spectrum
	 * 
	 * This method splits the range into subdivs intervals and calculate which
	 * intervals may contain a root that may contain roots. The higher the value
	 * of subdivs, the more accurate the brackets will be between a root of the
	 * function
	 * 
	 * @param function
	 *            The function to retrieve brackets from
	 * @param lower
	 *            The lower bound of the brackets
	 * @param upper
	 *            The upper bound of the brackets
	 * @param subdivs
	 *            The number of brackets to create
	 * @return A series of {@link RootInterval}s between the lower and upper
	 *         bounds of which roots may lie
	 */
	protected ArrayList<RootInterval> findBrackets(Function function, double lower, double upper,
			int subdivs)
	{
		ArrayList<RootInterval> bracks = new ArrayList<RootInterval>(subdivs);

		// get the midpoint
		double dx = (upper - lower) / subdivs;
		double x = lower;

		// evaluate at the lower bound
		double fp = function.evaluateAt(x);

		for (int j = 0; j < subdivs; j++)
		{
			x += dx;
			double fc = function.evaluateAt(x);

			// If the subdivision crosses the Y axis it may contain a root so
			// add it to the list
			if (fc * fp < 0.0D)
			{
				bracks.add(new RootInterval(x - dx, x));
			}

			// move on to the next interval
			fp = fc;
		}

		return bracks;
	}

	/**
	 * Get the criteria used to decide whether or not the root finding algorithm
	 * has converged
	 * 
	 * @return The criteria used to decide whether or not the root finding
	 *         algorithm has converged
	 */
	public ConvergenceCriteria getConvergenceCriteria()
	{
		return convergenceCriteria;
	}

	/**
	 * Get the number of iterations used when estimating the roots of the
	 * function
	 * 
	 * @return The number of iterations used when estimating the roots of the
	 *         function
	 */
	public int getIterations()
	{
		return this.iterations;
	}

	/**
	 * Get the function to find the roots of
	 * 
	 * @return The function to find the roots of
	 */
	public Function getTargetFunction()
	{
		return this.targetFunction;
	}

	/**
	 * Get the tolerance used to decide whether or not a found root is valid
	 * 
	 * @return The tolerance used to decide whether or not a found root is valid
	 */
	public double getTolerance()
	{
		return this.tolerance;
	}

	/**
	 * Set the criteria used to decide whether or not the root finding algorithm
	 * has converged
	 * 
	 * @param convergenceCriteria
	 *            The new convergence criteria
	 */
	public void setConvergenceCriteria(ConvergenceCriteria convergenceCriteria)
	{
		this.convergenceCriteria = convergenceCriteria;
	}

	/**
	 * Set the number of iterations used when estimating the roots of the
	 * function
	 * 
	 * @param iterations
	 *            The new number of iterations
	 */
	public void setIterations(int iterations)
	{
		this.iterations = iterations;
	}

	/**
	 * Set the function to find the roots of
	 * 
	 * @param targetFunction
	 *            The new function to find the roots of
	 */
	public void setTargetFunction(Function targetFunction)
	{
		this.targetFunction = targetFunction;
	}

	/**
	 * Set the tolerance used to decide whether or not a found root is valid
	 * 
	 * @param tolerance
	 *            The new tolerance
	 */
	public void setTolerance(double tolerance)
	{
		this.tolerance = Math.abs(tolerance);
	}

	/**
	 * Estimate a value of the root of the target function using the specified
	 * number of iterations, convergence criteria and root tolerance to decide
	 * whether or not the found root is valid
	 * 
	 * @return An estimate of the root of the target function
	 */
	public abstract double solve();
}
