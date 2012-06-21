package uk.co.raharrison.mathengine.integral;

import uk.co.raharrison.mathengine.Function;

public class TrapeziumIntegrator extends IntegrationMethod
{
	public TrapeziumIntegrator(Function function)
	{
		super(function);
	}

	@Override
	public double integrate()
	{
		Function f = this.targetFunction;
		double lower = this.lower;
		double upper = this.upper;

		double range = upper - lower;

		double sum = 0.0;

		for (int i = 1; i < iterations; i++)
		{
			double x = lower + range * i / iterations;
			sum += f.evaluate(x);
		}

		sum += (f.evaluate(lower) + f.evaluate(upper)) / 2.0;
		return sum * range / iterations;
	}

}
