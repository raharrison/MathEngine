package uk.co.raharrison.mathengine.solvers;

import uk.co.raharrison.mathengine.Function;

public final class BisectionSolver extends RootBracketingMethod
{

	public BisectionSolver(Function targetFunction)
	{
		super(targetFunction);
	}

	@Override
	public double solve()
	{
		super.checkRootFindingParams();

		Function f = this.targetFunction;

		double a = lowerBound;
		double b = upperBound;

		int iteration = 1;

		double fb = f.evaluate(b);

		while (iteration <= this.iterations)
		{
			double xm = (b + a) / 2; // interval midpoint
			if (fb * f.evaluate(xm) > 0)
			{
				b = xm;
			}
			else
			{
				a = xm;
			}

			if (Math.abs(upperBound - lowerBound) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return b - (b - a) * f.evaluate(b) / (f.evaluate(b) - f.evaluate(a));
			}

			iteration++;
		}

		if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
		{
			return b - (b - a) * f.evaluate(b) / (f.evaluate(b) - f.evaluate(a));
		}

		throw new UnsupportedOperationException(
				"Unable to find the root within specified tolerance after " + iterations
						+ " iterations");
	}

}
