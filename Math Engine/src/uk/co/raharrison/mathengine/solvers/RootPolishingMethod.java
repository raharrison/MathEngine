package uk.co.raharrison.mathengine.solvers;

import java.util.ArrayList;

import uk.co.raharrison.mathengine.Function;

public abstract class RootPolishingMethod extends EquationSolver
{
	protected double initialGuess;

	public RootPolishingMethod(Function targetFunction)
	{
		this.targetFunction = targetFunction;
		this.initialGuess = 1;
	}

	public double getInitialGuess()
	{
		return this.initialGuess;
	}

	public void setInitialGuess(double initialGuess)
	{
		this.initialGuess = initialGuess;
	}

	public ArrayList<Double> solveAll(double upper, double lower)
	{
		super.checkRootFindingParams();

		ArrayList<RootInterval> bracks = findBrackets(targetFunction, lower, upper, iterations);
		ArrayList<Double> roots = new ArrayList<Double>();

		double tGuess = initialGuess;

		for (RootInterval interval : bracks)
		{
			initialGuess = (interval.getUpper() + interval.getLower()) / 2;
			double result = solve();

			if (!roots.contains(result))
			{
				roots.add(result);
			}
		}

		initialGuess = tGuess;

		return roots;
	}
}