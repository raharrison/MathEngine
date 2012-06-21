package uk.co.raharrison.mathengine.integral;

import uk.co.raharrison.mathengine.Function;

public class GaussLegendreIntegrator extends IntegrationMethod
{
	private double[] x;
	private double[] w;

	public GaussLegendreIntegrator(Function function)
	{
		super(function);
		x = new double[] { -0.9324695142, -0.6612093865, -0.2386191861, 0.2386191861, 0.6612093865,
				0.9324695142 };
		w = new double[] { 0.1713244924, 0.3607615730, 0.4679139346, 0.4679139346, 0.3607615730,
				0.1713244924 };
	}

	@Override
	public double integrate()
	{
		double a = lower;
		double b = upper;

		Function f = targetFunction;

		double xa = a;
		double h = (b - a) / iterations;

		double integral = 0;
		for (int i = 1; i <= iterations; i++)
		{
			double sum = 0;
			for (int j = 0; j < 6; j++)
			{
				sum += w[j] * f.evaluateAt(xa + 0.5 * h * (x[j] + 1));
			}
			integral += 0.5 * h * sum;
			xa += h;
		}
		return integral;
	}
}
