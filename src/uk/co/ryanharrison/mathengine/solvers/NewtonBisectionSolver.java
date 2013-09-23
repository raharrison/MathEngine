package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;

/**
 * Class representing a {@link RootBracketingMethod} that uses a mixture of the
 * bisection and Newton Raphson's algorithms to find the roots of the target
 * {@link Function} between a set of bounds
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
public final class NewtonBisectionSolver extends RootBracketingMethod
{
	/** The derivative of the target function */
	private Function derivativefunction;

	/**
	 * Whether or not to use numerical differentiation to estimate the
	 * derivative of the target function
	 */
	private boolean useNumericalDiff;

	/**
	 * Construct a new {@link NewtonBisectionSolver} with the specified target
	 * {@link Function}
	 * 
	 * This constructor will set the object to use numerical differentiation
	 * 
	 * @param targetFunction
	 *            The function to estimate the roots of
	 */
	public NewtonBisectionSolver(Function targetFunction)
	{
		super(targetFunction);
		this.setUseNumericalDiff(true);
	}

	/**
	 * Construct a new {@link NewtonBisectionSolver} with the specified target
	 * {@link Function} and derivative of the target {@link Function}
	 * 
	 * @param targetFunction
	 *            The function to estimate the roots of
	 * @param derivativeFunction
	 *            The derivative of the target function
	 */
	public NewtonBisectionSolver(Function targetFunction, Function derivativeFunction)
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
	 * Estimate the root of the target {@link Function} between the upper and
	 * lower bounds through a mixture of the bisection and Newton Raphson's
	 * algorithms
	 * 
	 * We first consider the Newton-Raphson step. If that would predict a next
	 * point that is outside of our bracketed range, then we do a bisection step
	 * instead by choosing the midpoint of the range to be the next point. We
	 * then evaluate the function at the next point and, depending on the sign
	 * of that evaluation, replace one of the bounding points with the new
	 * point. This keeps the root bracketed, while allowing us to benefit from
	 * the speed of Newton-Raphson.
	 * 
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

		double a = upperBound;
		double b = lowerBound;

		double fa = f.evaluateAt(a);

		// Calculate the midpoint of the bracket
		double x = 0.5 * (a + b);

		int iterations = 1;

		while (iterations <= this.iterations)
		{
			double fx = f.evaluateAt(x);

			// Return the current estimation if it is within the set tolerance
			// and the convergence criteria is set to accept this
			if (Math.abs(fx) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			// Bisection step
			if (fa * fx < 0.0D)
			{
				b = x;
			}
			else
			{
				a = x;
			}

			double dfx;

			// Numerically estimate the derivative if the boolean is set
			if (useNumericalDiff)
			{
				diff.setTargetPoint(x);
				dfx = diff.deriveFirst();
			}
			// Otherwise use the derivative function directly
			else
			{
				dfx = derivativefunction.evaluateAt(x);
			}

			// Newton Raphson step
			double dx = -fx / dfx;

			x = x + dx;

			// If it predicts a point outside of the range, then bisect instead
			if ((b - x) * (x - a) < 0.0D)
			{
				dx = 0.5 * (b - a);
				x = a + dx;
			}

			// Return the current estimation if it is within the set tolerance
			// and the convergence criteria is set to accept this
			if (Math.abs(dx) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			iterations++;
		}

		// If the number of iterations has been used and the convergence
		// criteria is set to accept this, return the estimation
		if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
		{
			return x;
		}

		throw new UnsupportedOperationException(
				"Unable to find the root within specified tolerance after " + iterations
						+ " iterations");
	}

}
