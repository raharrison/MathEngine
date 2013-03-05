package uk.co.raharrison.mathengine.differential;

import uk.co.raharrison.mathengine.Function;

public class ExtendedCentralDifferenceMethod extends NumericalDifferentiationMethod
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

		return (f.evaluateAt(x - 2 * h) - 8 * f.evaluateAt(x - h) + 8 * f.evaluateAt(x + h) - f
				.evaluateAt(x + 2 * h)) / 12 / h;
	}

	@Override
	public double deriveFourth()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (-f.evaluateAt(x - 3 * h) + 12 * f.evaluateAt(x - 2 * h) - 39 * f.evaluateAt(x - h)
				+ 56 * f.evaluateAt(x) - 39 * f.evaluateAt(x + h) + 12 * f.evaluateAt(x + 2 * h) - f
					.evaluateAt(x + 3 * h)) / 6 / h / h / h / h;
	}

	@Override
	public double deriveSecond()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (-f.evaluateAt(x - 2 * h) + 16 * f.evaluateAt(x - h) - 30 * f.evaluateAt(x) + 16
				* f.evaluateAt(x + h) - f.evaluateAt(x + 2 * h))
				/ 12 / h / h;
	}

	@Override
	public double deriveThird()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (f.evaluateAt(x - 3 * h) - 8 * f.evaluateAt(x - 2 * h) + 13 * f.evaluateAt(x - h)
				- 13 * f.evaluateAt(x + h) + 8 * f.evaluateAt(x + 2 * h) - f.evaluateAt(x + 3 * h))
				/ 8 / h / h / h;
	}
}
