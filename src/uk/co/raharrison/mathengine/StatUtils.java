package uk.co.raharrison.mathengine;

import java.util.Arrays;

public final class StatUtils
{
	private StatUtils()
	{
	}

	public static double correlationCoefficient(double[] x, double[] y)
	{
		double xStdDev = standardDeviation(x);
		double yStdDev = standardDeviation(y);
		double cov = covariance(x, y);

		return cov / (xStdDev * yStdDev);
	}

	public static double covariance(double[] x, double[] y)
	{
		double xmean = mean(x);
		double ymean = mean(y);

		double result = 0;

		for (int i = 0; i < x.length; i++)
		{
			result += (x[i] - xmean) * (y[i] - ymean);
		}

		result /= x.length - 1;

		return result;
	}

	public static double geometricMean(double[] data)
	{
		double sum = data[0];

		for (int i = 1; i < data.length; i++)
		{
			sum *= data[i];
		}

		return Math.pow(sum, 1.0 / data.length);
	}

	public static double harmonicMean(double[] data)
	{
		double sum = 0.0;

		for (int i = 0; i < data.length; i++)
		{
			sum += 1.0 / data[i];
		}

		return data.length / sum;
	}

	public static double interQuartileRange(double[] data)
	{
		return percentile(data, 75) - percentile(data, 25);
	}

	public static double kurtosis(double[] data)
	{
		double mean = mean(data);
		double numerator = 0, denominator = 0;

		for (int i = 0; i < data.length; i++)
		{
			numerator += Math.pow(data[i] - mean, 4);
			denominator += Math.pow(data[i] - mean, 2);
		}

		numerator = 1.0 / data.length * numerator;

		denominator = Math.pow(denominator * 1.0 / data.length, 2);

		return numerator / denominator - 3;
	}

	public static double max(double[] data)
	{
		double max = data[0];

		for (int i = 1; i < data.length; i++)
		{
			if (data[i] > max)
			{
				max = data[i];
			}
		}

		return max;
	}

	public static double mean(double[] data)
	{
		return sum(data) / data.length;
	}

	public static double median(double[] data)
	{
		return percentile(data, 50);
	}

	public static double min(double[] data)
	{
		double min = data[0];

		for (int i = 1; i < data.length; i++)
		{
			if (data[i] < min)
			{
				min = data[i];
			}
		}

		return min;
	}

	public static double percentile(double[] data, double n)
	{
		double[] ndata = data.clone();
		Arrays.sort(ndata);

		if (n < 0 || n > 1)
			throw new IllegalArgumentException("Percentile must be between zero and one");

		if (n == 0)
			return ndata[0];
		if (n == 1.0)
			return ndata[ndata.length - 1];

		double position = n * (ndata.length - 1);

		int lower = (int) Math.floor(position);
		int upper = (int) Math.ceil(position);

		if (lower == upper)
		{
			return ndata[lower];
		}
		return ndata[lower] + (position - lower) * (ndata[upper] - ndata[lower]);
	}

	public static double populationCovariance(double[] x, double[] y)
	{
		double xmean = mean(x);
		double ymean = mean(y);

		double result = 0;

		for (int i = 0; i < x.length; i++)
		{
			result += (x[i] - xmean) * (y[i] - ymean);
		}

		result /= x.length;

		return result;
	}

	public static double populationKurtosis(double[] data)
	{
		double mean = mean(data);
		double standardDeviation = standardDeviation(data);
		double n = data.length;

		double sum = 0;
		double multiplier = n * (n + 1) / ((n - 1) * (n - 2) * (n - 3));
		double subtractor = 3 * Math.pow(n - 1, 2) / ((n - 2) * (n - 3));

		for (int i = 0; i < data.length; i++)
		{
			sum += Math.pow((data[i] - mean) / standardDeviation, 4);
		}

		return multiplier * sum - subtractor;
	}

	public static double populationSkewness(double[] data)
	{
		double mean = mean(data);
		double standardDeviation = standardDeviation(data);

		double n = data.length;

		double multiplier = n / ((n - 1) * (n - 2));
		double sum = 0;

		for (int i = 0; i < data.length; i++)
		{
			sum += Math.pow((data[i] - mean) / standardDeviation, 3);
		}

		return multiplier * sum;
	}

	public static double populationStandardDeviation(double[] data)
	{
		return Math.sqrt(populationVariance(data));
	}

	public static double populationVariance(double[] data)
	{
		double mean = mean(data);

		double sumOfSquaredDeviations = 0;

		for (int i = 0; i < data.length; i++)
		{
			sumOfSquaredDeviations += Math.pow(data[i] - mean, 2);
		}

		return sumOfSquaredDeviations / data.length;
	}

	public static double product(double[] data)
	{
		double product = 1.0;

		for (int i = 0; i < data.length; i++)
		{
			product *= data[i];
		}

		return product;
	}

	public static double range(double[] data)
	{
		return max(data) - min(data);
	}

	public static double rootmeanSquare(double[] data)
	{
		return Math.sqrt(sumOfSquares(data) / data.length);
	}

	public static double skewness(double[] data)
	{
		double mean = mean(data);
		double numerator = 0, denominator = 0;

		for (int i = 0; i < data.length; i++)
		{
			numerator += Math.pow(data[i] - mean, 3);
			denominator += Math.pow(data[i] - mean, 2);
		}

		numerator = 1.0 / data.length * numerator;

		denominator = Math.pow(denominator * 1.0 / data.length, 3.0 / 2.0);

		return numerator / denominator;
	}

	public static double standardDeviation(double[] data)
	{
		return Math.sqrt(variance(data));
	}

	public static double sum(double[] data)
	{
		double sum = 0;

		for (int i = 0; i < data.length; i++)
		{
			sum += data[i];
		}

		return sum;
	}

	public static double sumOfSquares(double[] data)
	{
		double sum = 0;

		for (int i = 0; i < data.length; i++)
		{
			sum += Math.pow(data[i], 2);
		}

		return sum;
	}

	public static double truncatedMean(double[] data, double truncPercentage)
	{
		// get number of values to remove each side
		int numRemove = (int) Math.floor(truncPercentage / 100 * data.length / 2);

		// sort the data
		double[] sortedData = data.clone();
		Arrays.sort(sortedData);

		double sum = 0;
		int count = 0;

		// sum the values and get a count of the data, not taking into account
		// numRemove values from the start and end
		for (int i = numRemove; i < sortedData.length - numRemove; i++)
		{
			sum += sortedData[i];
			count++;
		}

		return sum / count;
	}

	public static double variance(double[] data)
	{
		double mean = mean(data);

		double sumOfSquaredDeviations = 0;

		for (int i = 0; i < data.length; i++)
		{
			sumOfSquaredDeviations += Math.pow(data[i] - mean, 2);
		}

		return sumOfSquaredDeviations / (data.length - 1);
	}

	/**
	 * Returns the weighted mean of a dataset
	 * 
	 * @param data
	 *            The dataset to average
	 * @param weights
	 *            The weights to use in the calculation
	 * @return The weighted mean
	 */
	public static double weightedMean(double[] data, double[] weights)
	{
		if (data.length != weights.length)
		{
			throw new IllegalArgumentException("Must have same number of weights as data");
		}

		double numerator = 0, denominator = 0;

		for (int i = 0; i < data.length; i++)
		{
			numerator += data[i] * weights[i];
			denominator += weights[i];
		}

		return numerator / denominator;
	}
}
