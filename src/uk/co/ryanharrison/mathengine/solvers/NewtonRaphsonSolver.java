package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;

/**
 * Class representing a {@link RootPolishingMethod} that uses the Newton Raphson
 * method to estimate the roots of the target function.
 * 
 * This solver requires the presence of the derivative of the target function to
 * solve for roots.
 * 
 * This may be through numerical differentiation or through another function
 * from the user.
 * 
 * @author Ryan Harrison
 * 
 */
public final class NewtonRaphsonSolver extends RootPolishingMethod
{
	/** The derivative of the target function */
	private Function derivativefunction;

	/**
	 * Whether or not to use numerical differentiation to estimate the
	 * derivative of the target function
	 */
	private boolean useNumericalDiff;

	/**
	 * Construct a new {@link NewtonRaphsonSolver} with the specified target
	 * {@link Function}
	 * 
	 * This constructor will set the object to use numerical differentiation
	 * 
	 * @param targetFunction
	 *            The function to estimate the roots of
	 */
	public NewtonRaphsonSolver(Function targetFunction)
	{
		super(targetFunction);
		this.setUseNumericalDiff(true);
	}

	/**
	 * Construct a new {@link NewtonRaphsonSolver} with the specified target
	 * {@link Function} and derivative of the target {@link Function}
	 * 
	 * @param targetFunction
	 *            The function to estimate the roots of
	 * @param derivativeFunction
	 *            The derivative of the target function
	 */
	public NewtonRaphsonSolver(Function targetFunction, Function derivativeFunction)
	{
		super(targetFunction);
		this.setDerivativefunction(derivativeFunction);
		this.setUseNumericalDiff(false);
	}

	/**
	 * Get the derivative of the target function
	 * 
	 * @return The derivative of the target function or null if none has been
	 *         set
	 */
	public Function getDerivativefunction()
	{
		return derivativefunction;
	}

	/**
	 * Is the root finding algorithm going to use numerical differentiation to
	 * approximate the derivative of the target function
	 * 
	 * @return Whether or not the root finding algorithm going to use numerical
	 *         differentiation to approximate the derivative of the target
	 *         function
	 */
	public boolean isUseNumericalDiff()
	{
		return useNumericalDiff;
	}

	/**
	 * Set the derivative of the target function
	 * 
	 * @param derivativefunction
	 *            The derivative of the target function
	 */
	public void setDerivativefunction(Function derivativefunction)
	{
		this.derivativefunction = derivativefunction;
	}

	/**
	 * Set whether or not to use numerical differentiation to approximate the
	 * derivative of the target function
	 * 
	 * @param useNumericalDiff
	 *            Whether or not the root finding algorithm going to use
	 *            numerical differentiation to approximate the derivative of the
	 *            target function
	 * @exception IllegalArgumentException
	 *                If useNumericalDiff is false and the derivative function
	 *                has not been set
	 */
	public void setUseNumericalDiff(boolean useNumericalDiff)
	{
		if (!useNumericalDiff && derivativefunction == null)
			throw new IllegalArgumentException("Derivative function has not been set");

		this.useNumericalDiff = useNumericalDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If a root was not found within the required number of
	 *                iterations within the specified tolerance
	 */
	@Override
	public double solve()
	{
		super.checkRootFindingParams();

		Function f = this.targetFunction;
		ExtendedCentralDifferenceMethod diff = new ExtendedCentralDifferenceMethod(targetFunction);

		int iteration = 1;
		double xm = initialGuess;

		while (iteration <= this.iterations)
		{
			// Numerically estimate the derivative if the boolean is set
			if (useNumericalDiff)
			{
				diff.setTargetPoint(xm);
				xm = xm - f.evaluateAt(xm) / diff.deriveFirst();
			}
			// Otherwise use the derivative function
			else
			{
				xm = xm - f.evaluateAt(xm) / derivativefunction.evaluateAt(xm);
			}

			// Return the current estimation if it is within the set tolerance
			// and the convergence criteria is set to accept this
			if (Math.abs(f.evaluateAt(xm)) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return xm;
			}

			iteration++;
		}

		// If the number of iterations has been used and the convergence
		// criteria is set to accept this, return the estimation
		if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
		{
			return xm;
		}

		throw new UnsupportedOperationException(
				"Unable to find the root within specified tolerance after " + iterations
						+ " iterations");
	}
}
