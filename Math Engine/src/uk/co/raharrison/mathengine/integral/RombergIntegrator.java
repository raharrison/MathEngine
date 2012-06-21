package uk.co.raharrison.mathengine.integral;

import uk.co.raharrison.mathengine.Function;

public class RombergIntegrator extends IntegrationMethod
{
	public RombergIntegrator(Function function)
	{
		super(function);
	}

	@Override
	public double integrate()
	{
		int m = 100;
		int n = 100;

		double a = lower;
		double b = upper;

		Function f = this.targetFunction;

		double[][] R = new double[m][n];

		int iteration = 0;
		int i = 1;

		while (iteration <= iterations)
		{
			iteration++;

			R[0][0] = 0.5 * (b - a) * (f.evaluateAt(a) + f.evaluateAt(b));

			for (i = 1; i < m; i++)
			{
				double h = (b - a) / Math.pow(2, i);

				int kMax = (int) Math.pow(2, i - 1) + 1;
				double sum = 0;
				for (int k = 1; k < kMax; k++)
				{
					sum += f.evaluateAt(a + (2 * k - 1) * h);
				}

				R[i][0] = 0.5 * R[i - 1][0] + h * sum;

				for (int j = 1; j < i + 1; j++)
				{
					R[i][j] = R[i][j - 1] + (R[i][j - 1] - R[i - 1][j - 1]) / (Math.pow(4, j) - 1);
				}
			}
		}

		return R[i][i];
	}
}
