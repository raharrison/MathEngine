package uk.co.raharrison.mathengine.parser.operators;

import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.SimpleUnaryOperator;

public abstract class TrigOperator extends SimpleUnaryOperator
{
	protected AngleUnit unit;

	private double degToRad(double radians)
	{
		return Math.PI * radians / 180.0;
	}

	@Override
	public double getResult(double x)
	{
		return getResult(x, unit);
	}

	public abstract double getResult(double x, AngleUnit unit);

	private double gradToRad(double radians)
	{
		return radians * (Math.PI / 200);
	}

	protected double radiansTo(double radians, AngleUnit angleUnits)
	{
		switch (angleUnits)
		{
			case Degrees:
				return degToRad(radians);
			case Gradians:
				return gradToRad(radians);
			default:
				return radians;
		}
	}

	public void setAngleUnit(AngleUnit units)
	{
		this.unit = units;
	}
}