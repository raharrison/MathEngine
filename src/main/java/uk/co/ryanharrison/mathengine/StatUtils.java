package uk.co.ryanharrison.mathengine;

import java.util.Arrays;

/**
 * Various useful statistical functions
 *
 * @author Ryan Harrison
 */
public final class StatUtils {

    /**
     * Not permitted to create an instance of this class
     */
    private StatUtils() {
    }

    /**
     * Calculate the correlation coefficient of two data sets
     *
     * @param x The first data set
     * @param y The second data set
     * @return The correlation coefficient of the two data sets
     */
    public static double correlationCoefficient(double[] x, double[] y) {
        double xStdDev = standardDeviation(x);
        double yStdDev = standardDeviation(y);
        double cov = covariance(x, y);

        return cov / (xStdDev * yStdDev);
    }

    /**
     * Calculate the covariance of two data sets
     *
     * @param x The first data set
     * @param y The second data set
     * @return The covariance of the two data sets
     */
    public static double covariance(double[] x, double[] y) {
        double xmean = mean(x);
        double ymean = mean(y);

        double result = 0;

        for (int i = 0; i < x.length; i++) {
            result += (x[i] - xmean) * (y[i] - ymean);
        }

        result /= x.length - 1;

        return result;
    }

    /**
     * Calculate the geometric mean of a data set
     *
     * @param data The data to use
     * @return The geometric mean of the data set
     */
    public static double geometricMean(double[] data) {
        double sum = data[0];

        for (int i = 1; i < data.length; i++) {
            sum *= data[i];
        }

        return Math.pow(sum, 1.0 / data.length);
    }

    /**
     * Calculate the harmonic mean of a data set
     *
     * @param data The data to use
     * @return The harmonic mean of the data set
     */
    public static double harmonicMean(double[] data) {
        double sum = 0.0;

        for (int i = 0; i < data.length; i++) {
            sum += 1.0 / data[i];
        }

        return data.length / sum;
    }

    /**
     * Calculate the interquartile range of a data set
     * <p>
     * The interquartile range is the difference between the 0.75 percentile and
     * the 0.25 percentile
     *
     * @param data The data to use
     * @return The interquartile range of the data set
     */
    public static double interQuartileRange(double[] data) {
        return percentile(data, 0.75) - percentile(data, 0.25);
    }

    /**
     * Calculate the kurtosis of a data set
     *
     * @param data The data to use
     * @return The kurtosis of the data set
     */
    public static double kurtosis(double[] data) {
        double mean = mean(data);
        double numerator = 0, denominator = 0;

        for (int i = 0; i < data.length; i++) {
            numerator += Math.pow(data[i] - mean, 4);
            denominator += Math.pow(data[i] - mean, 2);
        }

        numerator = 1.0 / data.length * numerator;

        denominator = Math.pow(denominator * 1.0 / data.length, 2);

        return (numerator / denominator);
    }

    /**
     * Find the maximum or largest value in a data set
     *
     * @param data The data to use
     * @return The maximum or largest value in the data set
     */
    public static double max(double[] data) {
        double max = data[0];

        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }

        return max;
    }

    /**
     * Calculate the mean of a data set
     *
     * @param data The data to use
     * @return The mean of the data set
     */
    public static double mean(double[] data) {
        return sum(data) / data.length;
    }

    /**
     * Calculate the median of a data set
     * <p>
     * The median is the 'middle value' or 0.5 percentile
     *
     * @param data The data to use
     * @return The median of a data set
     */
    public static double median(double[] data) {
        return percentile(data, 0.50);
    }

    /**
     * Find the minimum or smallest value in a data set
     *
     * @param data The data to use
     * @return The minimum or smallest value in the data set
     */
    public static double min(double[] data) {
        double min = data[0];

        for (int i = 1; i < data.length; i++) {
            if (data[i] < min) {
                min = data[i];
            }
        }

        return min;
    }

    /**
     * Calculate the desired percentile of a data set
     * <p>
     * For example a value of 0.5 for n would result in the 50% percentile which
     * is the median or middle value
     *
     * @param data The data to use
     * @param n    The desired percentile
     * @return The nth percentile of the data set
     * @throws IllegalArgumentException If the percentile to calculate is not between zero and one
     *                                  inclusive
     */
    public static double percentile(double[] data, double n) {
        double[] ndata = data.clone();
        Arrays.sort(ndata);

        double result;

        // Get roughly the index
        double index = (ndata.length - 1) * n + 1;

        // Get the remainder of that index value if exists
        double remainder = index % 1;

        // Get the integer value of that index
        int indexNum = (int) Math.floor(index) - 1;

        if (remainder == 0) {
            // we have an integer value, no interpolation needed
            result = ndata[indexNum];
        } else {
            // we need to interpolate
            double value = ndata[indexNum];
            double interpolationValue = (ndata[indexNum + 1] - value) * remainder;
            result = value + interpolationValue;
        }

        return result;
    }

    /**
     * Calculate the population covariance of two data sets
     *
     * @param x The first data set
     * @param y The second data set
     * @return The population covariance of the two data sets
     */
    public static double populationCovariance(double[] x, double[] y) {
        double xmean = mean(x);
        double ymean = mean(y);

        double result = 0;

        for (int i = 0; i < x.length; i++) {
            result += (x[i] - xmean) * (y[i] - ymean);
        }

        result /= x.length;

        return result;
    }

    /**
     * Calculate the population kurtosis of a data set
     *
     * @param data The data to use
     * @return The population kurtosis of a data set
     */
    public static double populationKurtosis(double[] data) {
        double mean = mean(data);
        double standardDeviation = standardDeviation(data);
        double n = data.length;

        double sum = 0;
        double multiplier = n * (n + 1) / ((n - 1) * (n - 2) * (n - 3));
        double subtractor = 3 * Math.pow(n - 1, 2) / ((n - 2) * (n - 3));

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow((data[i] - mean) / standardDeviation, 4);
        }

        return multiplier * sum - subtractor;
    }

    /**
     * Calculate the population skewness of a data set
     *
     * @param data The data to use
     * @return The population skewness of a data set
     */
    public static double populationSkewness(double[] data) {
        double mean = mean(data);
        double standardDeviation = standardDeviation(data);

        double n = data.length;

        double multiplier = n / ((n - 1) * (n - 2));
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow((data[i] - mean) / standardDeviation, 3);
        }

        return multiplier * sum;
    }

    /**
     * Calculate the population standard deviation of a data set
     *
     * @param data The data to use
     * @return The population standard deviation of a data set
     */
    public static double populationStandardDeviation(double[] data) {
        return Math.sqrt(populationVariance(data));
    }

    /**
     * Calculate the population variance of a data set
     *
     * @param data The data to use
     * @return The population variance of a data set
     */
    public static double populationVariance(double[] data) {
        double mean = mean(data);

        double sumOfSquaredDeviations = 0;

        for (int i = 0; i < data.length; i++) {
            sumOfSquaredDeviations += Math.pow(data[i] - mean, 2);
        }

        return sumOfSquaredDeviations / data.length;
    }

    /**
     * Calculate the product of a data set
     *
     * @param data The data to use
     * @return The product of the data set
     */
    public static double product(double[] data) {
        double product = 1.0;

        for (int i = 0; i < data.length; i++) {
            product *= data[i];
        }

        return product;
    }

    /**
     * Calculate the range of a data set
     * <p>
     * The range is the difference between the largest and smallest values
     *
     * @param data The data to use
     * @return The product of the data set
     */
    public static double range(double[] data) {
        return max(data) - min(data);
    }

    /**
     * Calculate the root mean square of a data set
     * <p>
     * The root mean square is the square root if the sum of the squared values
     *
     * @param data The data to use
     * @return The root mean square of a data set
     */
    public static double rootmeanSquare(double[] data) {
        return Math.sqrt(sumOfSquares(data) / data.length);
    }

    /**
     * Calculate the skewness of a data set
     *
     * @param data The data to use
     * @return The skewness of the data set
     */
    public static double skewness(double[] data) {
        double mean = mean(data);
        double numerator = 0, denominator = 0;

        for (int i = 0; i < data.length; i++) {
            numerator += Math.pow(data[i] - mean, 3);
            denominator += Math.pow(data[i] - mean, 2);
        }

        numerator = 1.0 / data.length * numerator;

        denominator = Math.pow(denominator * 1.0 / data.length, 3.0 / 2.0);

        return numerator / denominator;
    }

    /**
     * Calculate the standard deviation of a data set
     *
     * @param data The data to use
     * @return The standard deviation of the data set
     */
    public static double standardDeviation(double[] data) {
        return Math.sqrt(variance(data));
    }

    /**
     * Calculate the sum of a data set
     *
     * @param data The data to use
     * @return The sum of the data set
     */
    public static double sum(double[] data) {
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }

        return sum;
    }

    /**
     * Calculate the sum of the squares of a data set
     *
     * @param data The data to use
     * @return The sum of the squares of the data set
     */
    public static double sumOfSquares(double[] data) {
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i], 2);
        }

        return sum;
    }

    /**
     * Calculate the truncated mean of a data set
     * <p>
     * The truncated mean allows a certain percentage of values (outliers) to be
     * removed before the calculation begins. These values will not be used to
     * calculate the mean
     *
     * @param data            The data to use
     * @param truncPercentage The percentage of values to truncate
     * @return The truncated mean of the data set
     */
    public static double truncatedMean(double[] data, double truncPercentage) {
        // get number of values to remove each side
        int numRemove = (int) Math.floor(truncPercentage / 100 * data.length / 2);

        // sort the data
        double[] sortedData = data.clone();
        Arrays.sort(sortedData);

        double sum = 0;
        int count = 0;

        // sum the values and get a count of the data, not taking into account
        // numRemove values from the start and end
        for (int i = numRemove; i < sortedData.length - numRemove; i++) {
            sum += sortedData[i];
            count++;
        }

        return sum / count;
    }

    /**
     * Calculate the variance of a data set
     *
     * @param data The data to use
     * @return The variance of the data set
     */
    public static double variance(double[] data) {
        double mean = mean(data);

        double sumOfSquaredDeviations = 0;

        for (int i = 0; i < data.length; i++) {
            sumOfSquaredDeviations += Math.pow(data[i] - mean, 2);
        }

        return sumOfSquaredDeviations / (data.length - 1);
    }

    /**
     * Calculate the weighted mean of a data set
     * <p>
     * Each data point is given a weight. The larger the weight, the more it
     * impacts the resulting value
     *
     * @param data    The data to use
     * @param weights The weights to use
     * @return The weighted mean of the data set
     * @throws IllegalArgumentException If there is not the same number of weights as there are data
     *                                  points
     */
    public static double weightedMean(double[] data, double[] weights) {
        if (data.length != weights.length) {
            throw new IllegalArgumentException("Must have same number of weights as data");
        }

        double numerator = 0, denominator = 0;

        for (int i = 0; i < data.length; i++) {
            numerator += data[i] * weights[i];
            denominator += weights[i];
        }

        return numerator / denominator;
    }
}
