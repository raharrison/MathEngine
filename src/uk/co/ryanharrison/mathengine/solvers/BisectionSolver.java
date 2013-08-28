package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

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

		double fb = f.evaluateAt(b);

		while (iteration <= this.iterations)
		{
			double xm = (b + a) / 2; // interval midpoint
			if (fb * f.evaluateAt(xm) > 0)
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
				return b - (b - a) * f.evaluateAt(b) / (f.evaluateAt(b) - f.evaluateAt(a));
			}

			iteration++;
		}

		if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
		{
			return b - (b - a) * f.evaluateAt(b) / (f.evaluateAt(b) - f.evaluateAt(a));
		}

		throw new UnsupportedOperationException(
				"Unable to find the root within specified tolerance after " + iterations
						+ " iterations");
	}

}
