package uk.co.ryanharrison.mathengine.solvers;

import java.util.ArrayList;
import java.util.List;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing an equation solver that brackets the target root and
 * sequentially shortens the bracket to converge in on the root itself
 * 
 * @author Ryan Harrison
 * 
 */
public abstract class RootBracketingMethod extends EquationSolver
{
	/** The lower bound of the bracket to search in */
	protected double lowerBound;

	/** The upper bound of the bracket to search in */
	protected double upperBound;

	/**
	 * Construct a new {@link RootBracketingMethod} with the specified target
	 * {@link Function}
	 * 
	 * This constructor sets default values of zero to the upper and lower
	 * bounds
	 * 
	 * @param targetFunction
	 *            The function to find the root of
	 */
	public RootBracketingMethod(Function targetFunction)
	{
		this.targetFunction = targetFunction;
		this.lowerBound = 0;
		this.upperBound = 0;
	}

	/**
	 * Ensure that the bounds of the bracket to search in are legal.
	 * 
	 * This is defined as the evaluation of the function at the lower bracket
	 * multiplied by the evaluation of the function at the upper bound must be
	 * less than zero i.e the bounds surround a change in sign of the function
	 * (a root)
	 * 
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If the bounds do not surround a root
	 */
	@Override
	protected void checkRootFindingParams()
	{
		super.checkRootFindingParams();

		if (!isValidBounds(targetFunction, lowerBound, upperBound))
			throw new IllegalArgumentException("Bounds do not surround a root");
	}

	/**
	 * Get the lower bound of the bracket to search in
	 * 
	 * @return The lower bound of the bracket to search in
	 */
	public double getLowerBound()
	{
		return this.lowerBound;
	}

	/**
	 * Get the upper bound of the bracket to search in
	 * 
	 * @return The upper bound of the bracket to search in
	 */
	public double getUpperBound()
	{
		return this.upperBound;
	}

	/**
	 * Ensure that the bounds surround a change in sign of the target function
	 * (a root of the function)
	 * 
	 * @param function
	 *            The target function
	 * @param lower
	 *            The lower bound of the bracket
	 * @param upper
	 *            The upper bound of the bracket
	 * @return True if the bounds surround a change in sign in the function (i.e
	 *         a root of the function if continuous), otherwise false
	 */
	protected boolean isValidBounds(Function function, double lower, double upper)
	{
		return function.evaluateAt(lower) * function.evaluateAt(upper) < 0.0D;
	}

	/**
	 * Set the lower bound of the bracket to search in
	 * 
	 * @param lowerBound
	 *            The new lower bound of the bracket
	 */
	public void setLowerBound(double lowerBound)
	{
		this.lowerBound = lowerBound;
	}

	/**
	 * Set the upper bound of the bracket to search in
	 * 
	 * @param upperBound
	 *            The new upper bound of the bracket
	 */
	public void setUpperBound(double upperBound)
	{
		this.upperBound = upperBound;
	}

	/**
	 * Attempt to find all of the roots of the target function between the upper
	 * and lower bounds of the bracket
	 * 
	 * @return A list of all of the estimated roots of the target function
	 *         between the upper and lower bounds
	 */
	public List<Double> solveAll()
	{
		super.checkRootFindingParams();

		// calculate a series of brackets that may contain the roots. These will
		// act as our brackets to search in
		// as they are already close to a possible root (a change in sign)
		List<RootInterval> bracks = findBrackets(targetFunction, lowerBound, upperBound, iterations);
		List<Double> roots = new ArrayList<Double>();
		double tLower = lowerBound;
		double tUpper = upperBound;

		// for reach interval that may contain a root, set the bounds of this
		// solver and solve for the root
		for (RootInterval interval : bracks)
		{
			lowerBound = interval.getLower();
			upperBound = interval.getUpper();
			roots.add(solve());
		}

		// reset the bounds to their initial values
		lowerBound = tLower;
		upperBound = tUpper;

		return roots;
	}
}
