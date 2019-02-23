package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing a {@link RootBracketingMethod} that uses the Bisection
 * algorithm to find the roots of the target {@link Function}
 * 
 * @author Ryan Harrison
 * 
 */
public final class BisectionSolver extends RootBracketingMethod
{
	/**
	 * Construct a new {@link BisectionSolver} {@link RootBracketingMethod} with
	 * the specified target {@link Function}
	 * 
	 * @param targetFunction
	 *            The function to find the roots of
	 */
	public BisectionSolver(Function targetFunction)
	{
		super(targetFunction);
	}

	/**
	 * Estimate the root of the target {@link Function} between the upper and
	 * lower bounds through bisection (half the size of the bracket that
	 * contains the route in each iteration whilst always keeping the root (sign
	 * change) within the brackets bounds
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

		double a = upperBound;
		double b = lowerBound;
		double x = b;

		int iterations = 1;

		while (iterations <= this.iterations)
		{
			// calculate the midpoint of the bracket
			x = (a + b) / 2;

			// Return the current estimation if it is within the set tolerance
			// and the convergence criteria is set to accept this
			if (f.evaluateAt(x) == 0 || Math.abs(b - a) / 2 < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			// Cut down the size of the bracket depending on which side of the
			// midpoint the root (sign change) lies in
			if (f.evaluateAt(a) * f.evaluateAt(x) < 0)
			{
				b = x;
			}
			else
			{
				a = x;
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
