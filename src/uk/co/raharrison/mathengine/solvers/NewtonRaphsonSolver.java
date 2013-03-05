package uk.co.raharrison.mathengine.solvers;

import uk.co.raharrison.mathengine.Function;
import uk.co.raharrison.mathengine.differential.ExtendedCentralDifferenceMethod;

public final class NewtonRaphsonSolver extends RootPolishingMethod
{
	private Function derivativefunction;
	private boolean useNumericalDiff;

	public NewtonRaphsonSolver(Function targetFunction)
	{
		super(targetFunction);
		this.setUseNumericalDiff(true);
	}

	public NewtonRaphsonSolver(Function targetFunction, Function derivativeFunction)
	{
		super(targetFunction);
		this.setDerivativefunction(derivativeFunction);
		this.setUseNumericalDiff(false);
	}

	public Function getDerivativefunction()
	{
		return derivativefunction;
	}

	public boolean isUseNumericalDiff()
	{
		return useNumericalDiff;
	}

	public void setDerivativefunction(Function derivativefunction)
	{
		this.derivativefunction = derivativefunction;
	}

	public void setUseNumericalDiff(boolean useNumericalDiff)
	{
		if (!useNumericalDiff && derivativefunction == null)
			throw new IllegalArgumentException("Derivative function has not been set");

		this.useNumericalDiff = useNumericalDiff;
	}

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
			if (useNumericalDiff)
			{
				diff.setTargetPoint(xm);
				xm = xm - f.evaluateAt(xm) / diff.deriveFirst();
			}
			else
			{
				xm = xm - f.evaluateAt(xm) / derivativefunction.evaluateAt(xm);
			}

			if (Math.abs(f.evaluateAt(xm)) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return xm;
			}

			iteration++;
		}

		if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
		{
			return xm;
		}

		throw new UnsupportedOperationException(
				"Unable to find the root within specified tolerance after " + iterations
						+ " iterations");
	}
}
