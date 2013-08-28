package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

public class RectangularIntegrator extends IntegrationMethod
{
	private RectanglePosition position;

	public RectangularIntegrator(Function function)
	{
		this(function, RectanglePosition.Midpoint);
	}

	public RectangularIntegrator(Function function, RectanglePosition position)
	{
		super(function);
		this.position = position;
	}

	@Override
	public double integrate()
	{
		Function func = this.targetFunction;
		double lower = this.lower;
		double upper = this.upper;
		double iterations = this.iterations;

		double range = upper - lower;
		double modeOffset = this.position.ordinal() / 2.0;
		double sum = 0.0;

		for (int i = 0; i < this.iterations; i++)
		{
			double x = lower + range * (i + modeOffset) / iterations;
			sum += func.evaluateAt(x);
		}

		return sum * range / iterations;
	}
}
