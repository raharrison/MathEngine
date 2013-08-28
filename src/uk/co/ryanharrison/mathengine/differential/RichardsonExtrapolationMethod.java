package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

public class RichardsonExtrapolationMethod extends DividedDifferenceMethod
{
	public RichardsonExtrapolationMethod(Function function)
	{
		super(function, DifferencesDirection.Central);
	}

	public RichardsonExtrapolationMethod(Function function, DifferencesDirection direction)
	{
		super(function, direction);

	}

	public RichardsonExtrapolationMethod(Function function, DifferencesDirection direction, double h)
	{
		super(function, direction, h);
	}

	@Override
	public double deriveFirst()
	{
		double d1 = super.deriveFirst();

		double oldH = getH();
		setH(oldH / 2);

		double d2 = super.deriveFirst();
		setH(oldH);

		return (4 * d2 - d1) / 3.0;
	}

	@Override
	public double deriveFourth()
	{
		double d1 = super.deriveFourth();

		double oldH = getH();
		setH(oldH / 2);

		double d2 = super.deriveFourth();
		setH(oldH);

		return (4 * d2 - d1) / 3.0;
	}

	@Override
	public double deriveSecond()
	{
		double d1 = super.deriveSecond();

		double oldH = getH();
		setH(oldH / 2);

		double d2 = super.deriveSecond();
		setH(oldH);

		return (4 * d2 - d1) / 3.0;
	}

	@Override
	public double deriveThird()
	{
		double d1 = super.deriveThird();

		double oldH = getH();
		setH(oldH / 2);

		double d2 = super.deriveThird();
		setH(oldH);

		return (4 * d2 - d1) / 3.0;
	}

}
