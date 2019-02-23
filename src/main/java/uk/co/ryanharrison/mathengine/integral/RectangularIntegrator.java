package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

/**
 * Class representing an {@link IntegrationMethod} that uses rectangles to
 * estimate the integral
 * 
 * @author Ryan Harrison
 * 
 */
public class RectangularIntegrator extends IntegrationMethod
{
	/** The position of the rectangles used to estimate the integral */
	private RectanglePosition position;

	/**
	 * Construct a new {@link RectangularIntegrator} with the specified target
	 * {@link Function}
	 * 
	 * @param function
	 *            The function to estimate the integral for
	 */
	public RectangularIntegrator(Function function)
	{
		this(function, RectanglePosition.Midpoint);
	}

	/**
	 * Construct a new {@link RectangularIntegrator} with the specified target
	 * {@link Function} and {@link RectanglePosition}
	 * 
	 * @param function
	 *            The function to estimate the integral for
	 * @param position
	 *            The position of the rectangles used to estimate the integral
	 */
	public RectangularIntegrator(Function function, RectanglePosition position)
	{
		super(function);
		this.position = position;
	}

	/**
	 * {@inheritDoc}
	 */
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
