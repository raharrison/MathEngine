package uk.co.raharrison.mathengine.differential;

import uk.co.raharrison.mathengine.Function;

public class DividedDifferenceMethod extends DifferentiationMethod
{
	private DifferencesDirection direction;

	public DividedDifferenceMethod(Function function)
	{
		this(function, DifferencesDirection.Central);
	}

	public DividedDifferenceMethod(Function function, DifferencesDirection direction)
	{
		super(function);
		this.direction = direction;
	}

	public DividedDifferenceMethod(Function function, DifferencesDirection direction, double h)
	{
		super(function, h);
		this.direction = direction;
	}

	@Override
	public double deriveFirst()
	{
		double x = targetPoint;
		double h = optimize ? optimizeH(x) : this.h;
		Function f = this.targetFunction;

		switch (this.direction)
		{
			case Forward:
				return (-3 * f.evaluate(x) + 4 * f.evaluate(x + h) - f.evaluate(x + 2 * h)) / 2 / h;
			case Central:
				return (f.evaluate(x + h) - f.evaluate(x - h)) / 2 / h;
			case Backward:
				return (3 * f.evaluate(x) - 4 * f.evaluate(x - h) + f.evaluate(x - 2 * h)) / 2 / h;
		}

		return x;
	}

	@Override
	public double deriveFourth()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		switch (this.direction)
		{
			case Forward:
				return (3 * f.evaluate(x) - 14 * f.evaluate(x + h) + 26 * f.evaluate(x + 2 * h)
						- 24 * f.evaluate(x + 3 * h) + 11 * f.evaluate(x + 4 * h) - 2 * f
						.evaluate(x + 5 * h)) / h / h / h / h;
			case Central:
				return (f.evaluate(x - 2 * h) - 4 * f.evaluate(x - h) + 6 * f.evaluate(x) - 4
						* f.evaluate(x + h) + f.evaluate(x + 2 * h))
						/ h / h / h / h;
			case Backward:
				return (3 * f.evaluate(x) - 14 * f.evaluate(x - h) + 26 * f.evaluate(x - 2 * h)
						- 24 * f.evaluate(x - 3 * h) + 11 * f.evaluate(x - 4 * h) - 2 * f
						.evaluate(x - 5 * h)) / h / h / h / h;
		}

		return x;
	}

	@Override
	public double deriveSecond()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		switch (this.direction)
		{
			case Forward:
				return (2 * f.evaluate(x) - 5 * f.evaluate(x + h) + 4 * f.evaluate(x + 2 * h) - f
						.evaluate(x + 3 * h)) / h / h;
			case Central:
				return (f.evaluate(x - h) - 2 * f.evaluate(x) + f.evaluate(x + h)) / h / h;
			case Backward:
				return (2 * f.evaluate(x) - 5 * f.evaluate(x - h) + 4 * f.evaluate(x - 2 * h) - f
						.evaluate(x - 3 * h)) / h / h;
		}

		return x;
	}

	@Override
	public double deriveThird()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		switch (this.direction)
		{
			case Forward:
				return (-5 * f.evaluate(x) + 18 * f.evaluate(x + h) - 24 * f.evaluate(x + 2 * h)
						+ 14 * f.evaluate(x + 3 * h) - 3 * f.evaluate(x + 4 * h))
						/ 2 / h / h / h;
			case Central:
				return (-f.evaluate(x - 2 * h) + 2 * f.evaluate(x - h) - 2 * f.evaluate(x + h) + f
						.evaluate(x + 2 * h)) / 2 / h / h / h;
			case Backward:
				return (5 * f.evaluate(x) - 18 * f.evaluate(x - h) + 24 * f.evaluate(x - 2 * h)
						- 14 * f.evaluate(x - 3 * h) + 3 * f.evaluate(x - 4 * h))
						/ 2 / h / h / h;
		}

		return x;
	}
}
