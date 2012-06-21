package uk.co.raharrison.mathengine.differential;

import uk.co.raharrison.mathengine.Function;

public class ExtendedCentralDifferenceMethod extends DifferentiationMethod
{
	public ExtendedCentralDifferenceMethod(Function function)
	{
		super(function);
	}

	public ExtendedCentralDifferenceMethod(Function function, double h)
	{
		super(function, h);
	}

	@Override
	public double deriveFirst()
	{
		double x = targetPoint;
		double h = optimize ? optimizeH(x) : this.h;
		Function f = this.targetFunction;

		return (f.evaluate(x - 2 * h) - 8 * f.evaluate(x - h) + 8 * f.evaluate(x + h) - f
				.evaluate(x + 2 * h)) / 12 / h;
	}

	@Override
	public double deriveFourth()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (-f.evaluate(x - 3 * h) + 12 * f.evaluate(x - 2 * h) - 39 * f.evaluate(x - h) + 56
				* f.evaluate(x) - 39 * f.evaluate(x + h) + 12 * f.evaluate(x + 2 * h) - f
					.evaluate(x + 3 * h)) / 6 / h / h / h / h;
	}

	@Override
	public double deriveSecond()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (-f.evaluate(x - 2 * h) + 16 * f.evaluate(x - h) - 30 * f.evaluate(x) + 16
				* f.evaluate(x + h) - f.evaluate(x + 2 * h))
				/ 12 / h / h;
	}

	@Override
	public double deriveThird()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (f.evaluate(x - 3 * h) - 8 * f.evaluate(x - 2 * h) + 13 * f.evaluate(x - h) - 13
				* f.evaluate(x + h) + 8 * f.evaluate(x + 2 * h) - f.evaluate(x + 3 * h))
				/ 8 / h / h / h;
	}
}
