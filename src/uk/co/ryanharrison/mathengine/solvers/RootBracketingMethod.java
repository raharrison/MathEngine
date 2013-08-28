package uk.co.ryanharrison.mathengine.solvers;

import java.util.ArrayList;
import java.util.List;

import uk.co.ryanharrison.mathengine.Function;

public abstract class RootBracketingMethod extends EquationSolver
{
	protected double lowerBound;
	protected double upperBound;

	public RootBracketingMethod(Function targetFunction)
	{
		this.targetFunction = targetFunction;
		this.lowerBound = 0;
		this.upperBound = 0;
	}

	@Override
	protected void checkRootFindingParams()
	{
		super.checkRootFindingParams();

		if (!isValidBounds(targetFunction, lowerBound, upperBound))
			throw new IllegalArgumentException("Bounds do not surround a root");
	}

	public double getLowerBound()
	{
		return this.lowerBound;
	}

	public double getUpperBound()
	{
		return this.upperBound;
	}

	protected boolean isValidBounds(Function function, double lower, double upper)
	{
		return function.evaluateAt(lower) * function.evaluateAt(upper) < 0.0D;
	}

	public void setLowerBound(double lowerBound)
	{
		this.lowerBound = lowerBound;
	}

	public void setUpperBound(double upperBound)
	{
		this.upperBound = upperBound;
	}

	public List<Double> solveAll()
	{
		super.checkRootFindingParams();

		List<RootInterval> bracks = findBrackets(targetFunction, lowerBound, upperBound,
				iterations);
		List<Double> roots = new ArrayList<Double>();
		double tLower = lowerBound;
		double tUpper = upperBound;

		for (RootInterval interval : bracks)
		{
			lowerBound = interval.getLower();
			upperBound = interval.getUpper();
			roots.add(solve());
		}

		lowerBound = tLower;
		upperBound = tUpper;

		return roots;
	}
}
